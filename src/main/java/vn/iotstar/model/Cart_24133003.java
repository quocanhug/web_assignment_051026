package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart_24133003 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, CartItem_24133003> items = new LinkedHashMap<>();
    public Map<Integer, CartItem_24133003> getItemsMap() { return Collections.unmodifiableMap(items); }
    public Collection<CartItem_24133003> getItems() { return Collections.unmodifiableCollection(items.values()); }

    public String addOrUpdate(Book_24133003 book, int addedQty) {
        if (book == null || book.getBookid() <= 0) return "Lỗi: Sản phẩm không hợp lệ!";
        if (addedQty <= 0) return "Lỗi: Số lượng phải là số nguyên dương!";
        int stock = book.getQuantity() == null ? 0 : book.getQuantity();
        if (stock <= 0) return "Lỗi: Sản phẩm đã hết hàng!";
        if (book.getPrice() == null || book.getPrice().signum() < 0) return "Lỗi: Giá sách không hợp lệ!";
        CartItem_24133003 old = items.get(book.getBookid());
        long requested = (long) (old == null ? 0 : old.getQuantity()) + addedQty;
        int quantity = (int) Math.min(requested, stock);
        items.put(book.getBookid(), new CartItem_24133003(book, quantity));
        return requested > stock ? "Cảnh báo: Đã điều chỉnh về tồn kho tối đa " + stock + " cuốn."
                : "Đã thêm sách vào giỏ hàng thành công!";
    }

    public String updateQuantity(int bookId, int newQty) {
        CartItem_24133003 item = items.get(bookId);
        if (item == null) return "Lỗi: Sản phẩm không tồn tại trong giỏ hàng!";
        if (newQty <= 0) return "Lỗi: Số lượng phải là số nguyên dương!";
        int stock = item.getBook().getQuantity() == null ? 0 : item.getBook().getQuantity();
        if (stock <= 0) {
            items.remove(bookId);
            return "Cảnh báo: Sách đã hết hàng và được bỏ khỏi giỏ.";
        }
        item.setQuantity(Math.min(newQty, stock));
        return newQty > stock ? "Cảnh báo: Đã điều chỉnh về tồn kho tối đa " + stock + " cuốn."
                : "Đã cập nhật số lượng thành công!";
    }

    public boolean removeItem(int bookId) { return items.remove(bookId) != null; }
    public void clear() { items.clear(); }
    public boolean isEmpty() { return items.isEmpty(); }
    public long getTotalQuantity() {
        return items.values().stream().mapToLong(CartItem_24133003::getQuantity).sum();
    }
    public int getItemCount() { return items.size(); }
    public BigDecimal getTotalAmount() {
        return items.values().stream().map(CartItem_24133003::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
