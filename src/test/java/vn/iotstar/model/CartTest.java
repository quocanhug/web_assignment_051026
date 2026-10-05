package vn.iotstar.model;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

public class CartTest {
    private Book_24133003 book(int id, int stock) {
        Book_24133003 b = new Book_24133003(); b.setBookid(id); b.setQuantity(stock);
        b.setTitle("Sách 'thử' <script>"); b.setPrice(new BigDecimal("110.25")); return b;
    }
    @Test public void addMergeAndMoney() {
        Cart_24133003 c = new Cart_24133003(); c.addOrUpdate(book(1, 10), 2); c.addOrUpdate(book(1, 10), 3);
        assertEquals(1, c.getItemCount()); assertEquals(5, c.getTotalQuantity());
        assertEquals(new BigDecimal("551.25"), c.getTotalAmount());
    }
    @Test public void addRejectsNonpositiveQuantity() {
        Cart_24133003 c = new Cart_24133003();
        assertTrue(c.addOrUpdate(book(1, 10), 0).startsWith("Lỗi"));
        assertTrue(c.addOrUpdate(book(1, 10), -1).startsWith("Lỗi")); assertTrue(c.isEmpty());
    }
    @Test public void capsStockWithoutOverflow() {
        Cart_24133003 c = new Cart_24133003(); c.addOrUpdate(book(1, 10), 1);
        assertTrue(c.addOrUpdate(book(1, 10), Integer.MAX_VALUE).startsWith("Cảnh báo"));
        assertEquals(10, c.getTotalQuantity());
    }
    @Test public void outOfStockAndNullBook() {
        Cart_24133003 c = new Cart_24133003(); c.addOrUpdate(book(1, 0), 1); c.addOrUpdate(null, 1); assertTrue(c.isEmpty());
    }
    @Test public void updateLimitsAndMissingItems() {
        Cart_24133003 c = new Cart_24133003(); c.addOrUpdate(book(1, 10), 3);
        c.updateQuantity(1, 0); assertEquals(3, c.getTotalQuantity());
        c.updateQuantity(1, -2); assertEquals(3, c.getTotalQuantity());
        c.updateQuantity(1, 100); assertEquals(10, c.getTotalQuantity());
        c.updateQuantity(1, 1); assertEquals(1, c.getTotalQuantity());
        assertTrue(c.updateQuantity(2, 1).startsWith("Lỗi"));
    }
    @Test public void freshBookReplacesSnapshot() {
        Cart_24133003 c = new Cart_24133003(); c.addOrUpdate(book(1, 10), 3);
        Book_24133003 fresh = book(1, 2); fresh.setPrice(new BigDecimal("120.00")); c.addOrUpdate(fresh, 1);
        assertEquals(2, c.getTotalQuantity()); assertEquals(new BigDecimal("240.00"), c.getTotalAmount());
    }
    @Test public void stockZeroRemovesItem() {
        Cart_24133003 c = new Cart_24133003(); Book_24133003 b = book(1, 10); c.addOrUpdate(b, 1);
        b.setQuantity(0); c.updateQuantity(1, 1); assertTrue(c.isEmpty());
    }
    @Test public void deleteClearAndTotals() {
        Cart_24133003 c = new Cart_24133003(); c.addOrUpdate(book(1, 10), 2); c.addOrUpdate(book(2, 10), 3);
        assertTrue(c.removeItem(1)); assertFalse(c.removeItem(1)); assertEquals(3, c.getTotalQuantity());
        c.clear(); assertTrue(c.isEmpty()); assertEquals(BigDecimal.ZERO, c.getTotalAmount());
    }
    @Test public void quantityTotalDoesNotOverflow() {
        Cart_24133003 c = new Cart_24133003(); c.addOrUpdate(book(1, Integer.MAX_VALUE), Integer.MAX_VALUE);
        c.addOrUpdate(book(2, Integer.MAX_VALUE), Integer.MAX_VALUE); assertEquals(4294967294L, c.getTotalQuantity());
    }
    @Test public void nullAndNegativePricesRejected() {
        Cart_24133003 c = new Cart_24133003(); Book_24133003 b = book(1, 10); b.setPrice(null);
        c.addOrUpdate(b, 1); b.setPrice(new BigDecimal("-1")); c.addOrUpdate(b, 1); assertTrue(c.isEmpty());
    }
    @Test public void shippingPreservesLeadingZeroAndUnicode() {
        ShippingAddress_24133003 s = new ShippingAddress_24133003(" Nguyễn Văn An ", "0901234567", " 123 đường Nguyễn Huệ, TP.HCM ", "");
        assertEquals("0901234567", s.phone()); assertEquals("Nguyễn Văn An", s.recipient());
        new ShippingAddress_24133003("An", "+84901234567", "123 đường Nguyễn Huệ", "");
    }
    @Test public void shippingRejectsBadFields() {
        for (String[] fields : new String[][] { {"", "0901234567", "123 Nguyễn Huệ", ""}, {"An", "123", "123 Nguyễn Huệ", ""},
                {"An", "0901234567", "abc", ""}, {"An", "0901234567", "123 Nguyễn Huệ", "x".repeat(1001)}}) {
            assertThrows(IllegalArgumentException.class, () -> new ShippingAddress_24133003(fields[0], fields[1], fields[2], fields[3]));
        }
    }
}
