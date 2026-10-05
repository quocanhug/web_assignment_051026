package vn.iotstar.integration;

import java.io.File;
import java.math.BigDecimal;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import org.apache.catalina.startup.Tomcat;
import org.junit.*;
import static org.junit.Assert.*;
import vn.iotstar.connection.DBConnection_24133003;
import vn.iotstar.dao.impl.*;
import vn.iotstar.model.*;

public class CheckoutIT {
    private static Tomcat tomcat;
    private static String base;
    private final DBConnection_24133003 db = new DBConnection_24133003();
    private final OrderDaoImpl_24133003 orders = new OrderDaoImpl_24133003();
    private int bookId;
    private HttpClient browser;
    private String csrf;
    private final ShippingAddress_24133003 shipping = new ShippingAddress_24133003("Nguyễn Văn An", "0901234567", "123 Nguyễn Huệ, TP.HCM", "Giao buổi sáng");

    @BeforeClass public static void start() throws Exception {
        String url = DBConnection_24133003.setting("DB_URL", "");
        if (!url.contains("databaseName=WebAssignmentCodTest_"))
            throw new IllegalStateException("Integration tests require a separate WebAssignmentCodTest_* database.");
        tomcat = new Tomcat(); tomcat.setBaseDir("target/it-tomcat"); tomcat.setPort(0); tomcat.getConnector();
        new File("target/it-tomcat/webapps").mkdirs();
        var context = tomcat.addWebapp("/bookstore", new File("target/kiemtraweb").getAbsolutePath());
        context.setParentClassLoader(CheckoutIT.class.getClassLoader());
        tomcat.start();
        base = "http://localhost:" + tomcat.getConnector().getLocalPort() + "/bookstore";
    }
    @AfterClass public static void stop() throws Exception { if (tomcat != null) { tomcat.stop(); tomcat.destroy(); } }
    @Before public void fixture() throws Exception {
        try (Connection c = db.getConnection(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM order_items"); s.executeUpdate("DELETE FROM orders");
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO books(title, price, quantity) VALUES(N'Sách thử COD',110.25,10)", Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate(); try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); bookId = rs.getInt(1); }
            }
        }
        browser = HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL)).build();
        HttpResponse<String> response = get("/cart"); assertEquals(response.body(), 200, response.statusCode());
        csrf = token(response.body(), "csrfToken");
        // Empty cart has no form; get token from the book detail form instead.
        if (csrf == null) csrf = token(get("/book/detail?id=" + bookId).body(), "csrfToken");
        assertNotNull(csrf);
    }
    private HttpResponse<String> get(String path) throws Exception {
        return browser.send(HttpRequest.newBuilder(URI.create(base + path)).timeout(Duration.ofSeconds(30)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }
    private HttpResponse<String> post(String path, Map<String,String> fields) throws Exception {
        Map<String,String> data = new LinkedHashMap<>(fields); data.putIfAbsent("csrfToken", csrf);
        StringJoiner body = new StringJoiner("&");
        data.forEach((k,v) -> body.add(URLEncoder.encode(k, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(v, StandardCharsets.UTF_8)));
        return browser.send(HttpRequest.newBuilder(URI.create(base + path)).timeout(Duration.ofSeconds(30)).header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString())).build(), HttpResponse.BodyHandlers.ofString());
    }
    private String token(String html, String name) {
        Matcher m = Pattern.compile("name=\"" + name + "\" value=\"([^\"]+)\"").matcher(html); return m.find() ? m.group(1) : null;
    }
    private void login() throws Exception {
        assertEquals(302, post("/login", Map.of("email","user@gmail.com","passwd","123456")).statusCode());
    }
    private Cart_24133003 cart(int quantity) {
        Cart_24133003 cart = new Cart_24133003(); cart.addOrUpdate(new BookDaoImpl_24133003().findById(bookId), quantity); return cart;
    }
    private int scalar(String query) throws Exception {
        try (Connection c = db.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(query)) { rs.next(); return rs.getInt(1); }
    }
    private void execute(String sql) throws Exception { try (Connection c = db.getConnection(); Statement s = c.createStatement()) { s.executeUpdate(sql); } }
    private Map<String,String> checkoutFields(String token) {
        return Map.of("checkoutToken",token,"recipient",shipping.recipient(),"phone",shipping.phone(),"address",shipping.address(),"note",shipping.note(),"paymentMethod","COD");
    }
    private String prepareCheckout() throws Exception {
        post("/cart/add", Map.of("bookId", "" + bookId, "quantity", "2")); login();
        HttpResponse<String> response = get("/cart/checkout"); assertEquals(response.body(), 200, response.statusCode());
        String token = token(response.body(), "checkoutToken"); assertNotNull(token); return token;
    }

    @Test public void overviewPagesAndPrivateViews() throws Exception {
        for (String path : List.of("/home", "/books", "/books?page=invalid", "/login", "/register", "/book/detail?id=" + bookId))
            assertEquals(path, 200, get(path).statusCode());
        assertEquals(302, get("/admin/books").statusCode());
        assertEquals(404, get("/WEB-INF/views/web/cart.jsp").statusCode());
        assertEquals(404, get("/views/web/cart.jsp").statusCode());
    }
    @Test public void cartAddMergeUpdateDeleteClear() throws Exception {
        post("/cart/add", Map.of("bookId", ""+bookId,"quantity","2"));
        post("/cart/add", Map.of("bookId", ""+bookId,"quantity","3"));
        assertTrue(get("/cart").body().contains("value=\"5\" min=\"1\""));
        post("/cart/update", Map.of("bookId", ""+bookId,"quantity","1"));
        assertTrue(get("/cart").body().contains("value=\"1\" min=\"1\""));
        post("/cart/delete", Map.of("bookId", ""+bookId)); assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
        post("/cart/add", Map.of("bookId", ""+bookId)); post("/cart/clear", Map.of());
        assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
    }
    @Test public void cartBoundsAndMalformedRequests() throws Exception {
        post("/cart/add", Map.of("bookId", ""+bookId,"quantity","2"));
        for (String qty : List.of("0","-1","abc","1.5","2147483648")) {
            post("/cart/update", Map.of("bookId", ""+bookId,"quantity",qty));
            assertTrue(qty, get("/cart").body().contains("value=\"2\" min=\"1\""));
        }
        post("/cart/add", Map.of("bookId", ""+bookId,"quantity","2147483647"));
        assertTrue(get("/cart").body().contains("value=\"10\" min=\"1\""));
        post("/cart/add", Map.of("bookId", "not-a-number")); assertEquals(200,get("/cart").statusCode());
    }
    @Test public void csrfAndGetCannotChangeCart() throws Exception {
        for (String path : List.of("/cart/add", "/cart/delete", "/cart/clear", "/cart/update")) assertEquals(405,get(path).statusCode());
        assertEquals(403,post("/cart/add",Map.of("bookId",""+bookId,"csrfToken","bad")).statusCode());
        assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
    }
    @Test public void freshInventoryAndDeletedBook() throws Exception {
        post("/cart/add",Map.of("bookId",""+bookId,"quantity","5")); execute("UPDATE books SET quantity=2 WHERE bookid="+bookId);
        post("/cart/update",Map.of("bookId",""+bookId,"quantity","8"));
        assertTrue(get("/cart").body().contains("value=\"2\" min=\"1\""));
        execute("DELETE FROM books WHERE bookid="+bookId); assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
    }
    @Test public void checkoutRequiresLoginAndNonemptyCart() throws Exception {
        assertEquals(302,get("/cart/checkout").statusCode()); login(); assertEquals(302,get("/cart/checkout").statusCode());
        assertEquals(0,scalar("SELECT COUNT(*) FROM orders"));
    }
    @Test public void loginReturnsToCheckoutWithoutStaleError() throws Exception {
        post("/cart/add",Map.of("bookId",""+bookId));
        assertEquals(302,get("/cart/checkout").statusCode());
        HttpResponse<String> result=post("/login",Map.of("email","user@gmail.com","passwd","123456"));
        assertTrue(result.headers().firstValue("location").orElseThrow().endsWith("/cart/checkout"));
        String html=get("/cart/checkout").body(); assertFalse(html.contains("Vui lòng đăng nhập để đặt hàng."));
    }
    @Test public void codHttpFlowAndDuplicateSubmission() throws Exception {
        String token = prepareCheckout(); HttpResponse<String> result = post("/cart/checkout",checkoutFields(token));
        assertEquals(result.body(),302,result.statusCode());
        String location = result.headers().firstValue("location").orElseThrow();
        assertTrue(location.contains("/order?id="));
        String receipt = get(location.substring("/bookstore".length())).body();
        assertTrue(receipt.contains("Nguyễn Văn An")); assertTrue(receipt.contains("0901234567")); assertTrue(receipt.contains("Chưa thanh toán"));
        assertEquals(1,scalar("SELECT COUNT(*) FROM orders")); assertEquals(8,scalar("SELECT quantity FROM books WHERE bookid="+bookId));
        assertEquals(location,post("/cart/checkout",checkoutFields(token)).headers().firstValue("location").orElseThrow());
        assertEquals(1,scalar("SELECT COUNT(*) FROM orders")); assertTrue(get("/cart").body().contains("Giỏ hàng đang trống"));
    }
    @Test public void invalidShippingKeepsCartAndFields() throws Exception {
        String token = prepareCheckout(); Map<String,String> fields = new HashMap<>(checkoutFields(token)); fields.put("phone","123");
        HttpResponse<String> result = post("/cart/checkout",fields); assertEquals(200,result.statusCode());
        assertTrue(result.body().contains("Số điện thoại phải")); assertTrue(result.body().contains("Nguyễn Văn An"));
        assertEquals(0,scalar("SELECT COUNT(*) FROM orders")); assertTrue(get("/cart").body().contains("value=\"2\" min=\"1\""));
    }
    @Test public void tamperedPaymentAndExpiredCartToken() throws Exception {
        String token = prepareCheckout(); Map<String,String> fields = new HashMap<>(checkoutFields(token)); fields.put("paymentMethod","PAID");
        assertTrue(post("/cart/checkout",fields).body().contains("chỉ hỗ trợ"));
        post("/cart/update",Map.of("bookId",""+bookId,"quantity","3"));
        assertEquals(302,post("/cart/checkout",checkoutFields(token)).statusCode()); assertEquals(0,scalar("SELECT COUNT(*) FROM orders"));
    }
    @Test public void checkoutRechecksStockAndPrice() throws Exception {
        String token = prepareCheckout(); execute("UPDATE books SET quantity=1 WHERE bookid="+bookId);
        assertTrue(post("/cart/checkout",checkoutFields(token)).body().contains("không đủ tồn kho"));
        assertEquals(0,scalar("SELECT COUNT(*) FROM orders"));
        execute("UPDATE books SET quantity=10,price=120 WHERE bookid="+bookId);
        assertTrue(post("/cart/checkout",checkoutFields(token)).body().contains("Giá sách đã thay đổi"));
        assertEquals(0,scalar("SELECT COUNT(*) FROM orders"));
    }
    @Test public void databaseOrderTotalsOwnershipAndSnapshots() throws Exception {
        Cart_24133003 cart = cart(2); long id = orders.createCod(2,cart,shipping,UUID.randomUUID().toString());
        Order_24133003 order = orders.findForUser(id,2); assertNotNull(order); assertNull(orders.findForUser(id,1));
        assertEquals(new BigDecimal("220.50"),order.getTotal()); assertEquals(2,order.getItems().get(0).getQuantity());
        execute("DELETE FROM books WHERE bookid="+bookId); assertEquals("Sách thử COD",orders.findForUser(id,2).getItems().get(0).getTitle());
        login(); assertEquals(404,get("/order?id=99999999").statusCode()); assertEquals(400,get("/order?id=bad").statusCode());
    }
    @Test public void otherUserCannotReadReceipt() throws Exception {
        long id = orders.createCod(1,cart(1),shipping,UUID.randomUUID().toString()); login(); assertEquals(404,get("/order?id="+id).statusCode());
    }
    @Test public void failedOrderRollsBackAllLines() throws Exception {
        Cart_24133003 cart = cart(2);
        Book_24133003 missing = new Book_24133003(); missing.setBookid(Integer.MAX_VALUE); missing.setQuantity(1); missing.setPrice(BigDecimal.ONE); cart.addOrUpdate(missing,1);
        assertThrows(IllegalArgumentException.class,()->orders.createCod(2,cart,shipping,UUID.randomUUID().toString()));
        assertEquals(10,scalar("SELECT quantity FROM books WHERE bookid="+bookId)); assertEquals(0,scalar("SELECT COUNT(*) FROM orders"));
    }
    @Test public void insertFailureRollsBackOrderAndStock() throws Exception {
        execute("CREATE TRIGGER fail_test_order_items ON order_items AFTER INSERT AS THROW 51000, 'test failure', 1;");
        try {
            assertThrows(SQLException.class,()->orders.createCod(2,cart(2),shipping,UUID.randomUUID().toString()));
            assertEquals(0,scalar("SELECT COUNT(*) FROM orders")); assertEquals(10,scalar("SELECT quantity FROM books WHERE bookid="+bookId));
        } finally { execute("DROP TRIGGER fail_test_order_items"); }
    }
    @Test public void simultaneousCustomersCannotOversell() throws Exception {
        execute("UPDATE books SET quantity=1 WHERE bookid="+bookId);
        Cart_24133003 first = cart(1), second = cart(1); ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);
        try {
            List<Future<Boolean>> results = new ArrayList<>();
            for (Cart_24133003 cart : List.of(first,second)) results.add(pool.submit(()-> {
                start.await(); try { orders.createCod(2,cart,shipping,UUID.randomUUID().toString()); return true; }
                catch (IllegalArgumentException e) { return false; }
            }));
            start.countDown(); int successes=0; for (Future<Boolean> f : results) if (f.get(15,TimeUnit.SECONDS)) successes++;
            assertEquals(1,successes); assertEquals(0,scalar("SELECT quantity FROM books WHERE bookid="+bookId)); assertEquals(1,scalar("SELECT COUNT(*) FROM orders"));
        } finally { pool.shutdownNow(); }
    }
    @Test public void daoDuplicateTokenDoesNotSubtractStockAgain() throws Exception {
        String token = UUID.randomUUID().toString(); Cart_24133003 cart = cart(2);
        long id=orders.createCod(2,cart,shipping,token); assertEquals(id,orders.createCod(2,cart,shipping,token));
        assertEquals(8,scalar("SELECT quantity FROM books WHERE bookid="+bookId));
    }
    @Test public void userContentIsEscapedInCartAndReceipt() throws Exception {
        execute("UPDATE books SET title=N'<script>alert(1)</script>' WHERE bookid="+bookId);
        post("/cart/add",Map.of("bookId",""+bookId)); String html=get("/cart").body();
        assertTrue(html.contains("&lt;script&gt;")); assertFalse(html.contains("<script>alert(1)</script>"));
        long id=orders.createCod(2,cart(1),shipping,UUID.randomUUID().toString()); login();
        assertTrue(get("/order?id="+id).body().contains("&lt;script&gt;"));
    }
    @Test public void authenticationAdminCrudAndValidation() throws Exception {
        assertEquals(200,post("/login",Map.of("email","user@gmail.com","passwd","wrong")).statusCode());
        login(); assertEquals(302,get("/admin/books").statusCode());
        post("/logout",Map.of());
        csrf=token(get("/login").body(),"csrfToken");
        assertEquals(302,post("/login",Map.of("email","admin@gmail.com","passwd","123456")).statusCode());
        for(String path : List.of("/admin/books","/admin/book/add","/admin/book/edit?id="+bookId)) assertEquals(path,200,get(path).statusCode());
        assertEquals(405,get("/admin/book/delete?id="+bookId).statusCode());
        post("/admin/book/edit",Map.of("bookid",""+bookId,"title","Sách cập nhật","price","200.50","quantity","4"));
        assertEquals(4,scalar("SELECT quantity FROM books WHERE bookid="+bookId));
        post("/admin/book/edit",Map.of("bookid",""+bookId,"title","Sách cập nhật","price","-1","quantity","-4"));
        assertEquals(4,scalar("SELECT quantity FROM books WHERE bookid="+bookId));
        post("/admin/book/add",Map.of("title","Sách mới "+bookId,"price","150","quantity","5"));
        assertEquals(1,scalar("SELECT COUNT(*) FROM books WHERE title=N'Sách mới "+bookId+"'"));
        post("/admin/book/delete",Map.of("id",""+bookId)); assertEquals(0,scalar("SELECT COUNT(*) FROM books WHERE bookid="+bookId));
        assertEquals(2,scalar("SELECT COUNT(*) FROM users WHERE id IN(1,2) AND passwd LIKE 'pbkdf2$%'"));
    }
    @Test public void ratingsRejectInvalidInputsAndEscapeText() throws Exception {
        login();
        assertEquals(400,post("/book/detail",Map.of("bookid","bad","rating","5")).statusCode());
        post("/book/detail",Map.of("bookid",""+bookId,"rating","6","review_text","bad"));
        assertEquals(0,scalar("SELECT COUNT(*) FROM rating WHERE bookid="+bookId));
        post("/book/detail",Map.of("bookid",""+bookId,"rating","5","review_text","<script>alert(1)</script>"));
        assertEquals(1,scalar("SELECT COUNT(*) FROM rating WHERE bookid="+bookId));
        String page=get("/book/detail?id="+bookId).body(); assertFalse(page.contains("<script>alert(1)</script>")); assertTrue(page.contains("&lt;script&gt;"));
        post("/book/detail",Map.of("bookid",""+bookId,"rating","4","review_text","Updated"));
        assertEquals(4,scalar("SELECT rating FROM rating WHERE bookid="+bookId+" AND userid=2"));
    }
    @Test public void httpSqlFailureKeepsCart() throws Exception {
        String token=prepareCheckout();
        execute("CREATE TRIGGER fail_http_items ON order_items AFTER INSERT AS THROW 51000, 'test failure', 1;");
        try {
            HttpResponse<String> result=post("/cart/checkout",checkoutFields(token)); assertEquals(503,result.statusCode());
            assertTrue(result.body().contains("Giỏ hàng được giữ nguyên")); assertEquals(0,scalar("SELECT COUNT(*) FROM orders"));
            assertEquals(10,scalar("SELECT quantity FROM books WHERE bookid="+bookId));
            assertTrue(get("/cart").body().contains("value=\"2\" min=\"1\""));
        } finally { execute("DROP TRIGGER fail_http_items"); }
    }
}
