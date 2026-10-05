package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem_24133003 implements Serializable {
    private static final long serialVersionUID = 1L;

    private Book_24133003 book;
    private int quantity;

    public CartItem_24133003() {
    }

    public CartItem_24133003(Book_24133003 book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book_24133003 getBook() {
        return book;
    }

    public void setBook(Book_24133003 book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Tính tổng tiền cho từng sản phẩm: đơn giá * số lượng
     */
    public BigDecimal getTotalPrice() {
        if (book == null || book.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
