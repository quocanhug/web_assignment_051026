package vn.iotstar.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.model.*;
import vn.iotstar.dao.impl.OrderDaoImpl_24133003;
import vn.iotstar.service.IBookService_24133003;
import vn.iotstar.service.impl.BookServiceImpl_24133003;

@WebServlet(urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/delete", "/cart/clear", "/cart/checkout", "/order"})
public class CartController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IBookService_24133003 bookService = new BookServiceImpl_24133003();
    private final OrderDaoImpl_24133003 orders = new OrderDaoImpl_24133003();

    private Cart_24133003 cart(HttpSession session) {
        Cart_24133003 cart = (Cart_24133003) session.getAttribute("cart");
        if (cart == null) { cart = new Cart_24133003(); session.setAttribute("cart", cart); }
        return cart;
    }
    private void message(HttpSession session, String text) {
        session.setAttribute(text.startsWith("Lỗi") || text.startsWith("Cảnh báo") ? "error" : "success", text);
    }
    private int positive(String value) {
        try {
            int number = Integer.parseInt(value == null ? "" : value.trim());
            if (number > 0) return number;
        } catch (NumberFormatException ignored) { }
        throw new IllegalArgumentException("Mã sách và số lượng phải là số nguyên dương hợp lệ.");
    }
    private void refresh(HttpSession session, Cart_24133003 cart) {
        boolean changed = false;
        Map<Integer, Book_24133003> books = new LinkedHashMap<>();
        // Load all first so a database outage cannot partially empty the cart.
        for (CartItem_24133003 item : cart.getItems())
            books.put(item.getBook().getBookid(), bookService.findById(item.getBook().getBookid()));
        for (CartItem_24133003 item : new ArrayList<>(cart.getItems())) {
            Book_24133003 fresh = books.get(item.getBook().getBookid());
            if (fresh == null || fresh.getQuantity() == null || fresh.getQuantity() <= 0
                    || fresh.getPrice() == null || fresh.getPrice().signum() < 0) {
                cart.removeItem(item.getBook().getBookid()); changed = true;
            } else {
                changed |= item.getQuantity() > fresh.getQuantity() || item.getBook().getPrice().compareTo(fresh.getPrice()) != 0;
                item.setBook(fresh);
                cart.updateQuantity(fresh.getBookid(), item.getQuantity());
            }
        }
        if (changed) {
            session.removeAttribute("checkoutToken");
            session.setAttribute("error", "Giá hoặc tồn kho đã thay đổi. Giỏ hàng đã được cập nhật; vui lòng kiểm tra lại.");
        }
    }
    private boolean requireUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getSession().getAttribute("user") != null) return true;
        req.getSession().setAttribute("returnToCheckout", true);
        req.getSession().setAttribute("error", "Vui lòng đăng nhập để đặt hàng.");
        resp.sendRedirect(req.getContextPath() + "/login");
        return false;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (!path.equals("/cart") && !path.equals("/cart/checkout") && !path.equals("/order")) {
            resp.setHeader("Allow", "POST"); resp.sendError(405); return;
        }
        HttpSession session = req.getSession();
        resp.setHeader("Cache-Control", "no-store");
        synchronized (session) {
            if (path.equals("/order")) {
                if (!requireUser(req, resp)) return;
                try {
                    long id = Long.parseLong(req.getParameter("id"));
                    Order_24133003 order = orders.findForUser(id, ((User_24133003) session.getAttribute("user")).getId());
                    if (order == null) { resp.sendError(404); return; }
                    req.setAttribute("order", order);
                    req.getRequestDispatcher("/WEB-INF/views/web/order.jsp").forward(req, resp);
                } catch (NumberFormatException e) { resp.sendError(400); }
                catch (Exception e) { log("Không thể đọc đơn hàng", e); resp.sendError(503); }
                return;
            }
            Cart_24133003 cart = cart(session);
            try { refresh(session, cart); }
            catch (IllegalStateException e) {
                log("Không thể cập nhật giỏ hàng", e);
                resp.sendError(503, "Không thể tải tồn kho. Giỏ hàng vẫn được giữ nguyên."); return;
            }
            if (path.equals("/cart/checkout")) {
                if (!requireUser(req, resp)) return;
                if (cart.isEmpty()) {
                    session.setAttribute("error", "Giỏ hàng đang trống, không thể đặt hàng.");
                    resp.sendRedirect(req.getContextPath() + "/cart"); return;
                }
                if (session.getAttribute("checkoutToken") == null)
                    session.setAttribute("checkoutToken", UUID.randomUUID().toString());
                req.getRequestDispatcher("/WEB-INF/views/web/checkout.jsp").forward(req, resp);
            } else req.getRequestDispatcher("/WEB-INF/views/web/cart.jsp").forward(req, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        synchronized (session) {
            if (req.getServletPath().equals("/cart/checkout")) { checkout(req, resp, session); return; }
            Cart_24133003 cart = cart(session);
            try {
                switch (req.getServletPath()) {
                    case "/cart/add" -> {
                        int id = positive(req.getParameter("bookId"));
                        int quantity = req.getParameter("quantity") == null ? 1 : positive(req.getParameter("quantity"));
                        message(session, cart.addOrUpdate(bookService.findById(id), quantity));
                    }
                    case "/cart/update" -> {
                        int id = positive(req.getParameter("bookId"));
                        int quantity = positive(req.getParameter("quantity"));
                        Book_24133003 fresh = bookService.findById(id);
                        CartItem_24133003 item = cart.getItemsMap().get(id);
                        if (item == null) throw new IllegalArgumentException("Sản phẩm không có trong giỏ.");
                        if (fresh == null) { cart.removeItem(id); throw new IllegalArgumentException("Sách đã bị gỡ khỏi cửa hàng."); }
                        item.setBook(fresh);
                        message(session, cart.updateQuantity(id, quantity));
                    }
                    case "/cart/delete" -> message(session, cart.removeItem(positive(req.getParameter("bookId")))
                            ? "Đã xóa sản phẩm khỏi giỏ." : "Lỗi: Sản phẩm không có trong giỏ.");
                    case "/cart/clear" -> { cart.clear(); message(session, "Đã xóa toàn bộ giỏ hàng."); }
                    default -> { resp.sendError(405); return; }
                }
                session.removeAttribute("checkoutToken");
            } catch (IllegalArgumentException e) { session.setAttribute("error", e.getMessage()); }
            catch (IllegalStateException e) { log("Không thể cập nhật giỏ", e); session.setAttribute("error", "Không thể tải thông tin sách. Vui lòng thử lại sau."); }
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }
    private void checkout(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws ServletException, IOException {
        if (!requireUser(req, resp)) return;
        String token = req.getParameter("checkoutToken");
        if (token != null && token.equals(session.getAttribute("completedCheckoutToken"))) {
            resp.sendRedirect(req.getContextPath() + "/order?id=" + session.getAttribute("completedOrderId")); return;
        }
        if (token == null || !token.equals(session.getAttribute("checkoutToken"))) {
            session.setAttribute("error", "Thông tin giỏ hàng đã thay đổi hoặc phiên đặt hàng hết hạn. Vui lòng kiểm tra lại.");
            resp.sendRedirect(req.getContextPath() + "/cart"); return;
        }
        Cart_24133003 cart = cart(session);
        if (cart.isEmpty()) { resp.sendRedirect(req.getContextPath() + "/cart"); return; }
        try {
            if (!"COD".equals(req.getParameter("paymentMethod")))
                throw new IllegalArgumentException("Hiện chỉ hỗ trợ thanh toán khi nhận hàng (COD).");
            ShippingAddress_24133003 shipping = new ShippingAddress_24133003(req.getParameter("recipient"),
                    req.getParameter("phone"), req.getParameter("address"), req.getParameter("note"));
            long id = orders.createCod(((User_24133003) session.getAttribute("user")).getId(), cart, shipping, token);
            // Only clear after the database transaction has committed.
            cart.clear();
            session.removeAttribute("checkoutToken");
            session.setAttribute("completedCheckoutToken", token);
            session.setAttribute("completedOrderId", id);
            resp.sendRedirect(req.getContextPath() + "/order?id=" + id);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("submitted", true);
            req.getRequestDispatcher("/WEB-INF/views/web/checkout.jsp").forward(req, resp);
        } catch (Exception e) {
            log("Đặt hàng COD thất bại", e);
            req.setAttribute("error", "Chưa thể lưu đơn hàng. Giỏ hàng được giữ nguyên; vui lòng thử lại sau.");
            req.setAttribute("submitted", true);
            resp.setStatus(503);
            req.getRequestDispatcher("/WEB-INF/views/web/checkout.jsp").forward(req, resp);
        }
    }
}
