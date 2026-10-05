package vn.iotstar.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order_24133003 {
    public long id;
    public String recipient, phone, address, note;
    public BigDecimal total;
    public Timestamp createdAt;
    public List<Item> items = new ArrayList<>();
    public long getId() { return id; }
    public String getRecipient() { return recipient; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getNote() { return note; }
    public BigDecimal getTotal() { return total; }
    public Timestamp getCreatedAt() { return createdAt; }
    public List<Item> getItems() { return items; }
    public static class Item {
        private final String title;
        private final int quantity;
        private final BigDecimal price;
        public Item(String title, int quantity, BigDecimal price) {
            this.title = title; this.quantity = quantity; this.price = price;
        }
        public String getTitle() { return title; }
        public int getQuantity() { return quantity; }
        public BigDecimal getPrice() { return price; }
        public BigDecimal getTotal() { return price.multiply(BigDecimal.valueOf(quantity)); }
    }
}
