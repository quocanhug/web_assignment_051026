package vn.iotstar.dao.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Comparator;
import java.util.List;
import vn.iotstar.connection.DBConnection_24133003;
import vn.iotstar.model.*;

public class OrderDaoImpl_24133003 {
    private final DBConnection_24133003 db = new DBConnection_24133003();

    public long createCod(int userId, Cart_24133003 cart, ShippingAddress_24133003 shipping,
                          String requestToken) throws Exception {
        if (userId <= 0 || requestToken == null || !requestToken.matches("[a-f0-9-]{36}"))
            throw new IllegalArgumentException("Yêu cầu đặt hàng không hợp lệ.");
        if (cart == null || cart.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống.");
        // Stable lock order prevents deadlocks between carts with different insertion order.
        List<CartItem_24133003> items = cart.getItems().stream()
                .sorted(Comparator.comparingInt(i -> i.getBook().getBookid())).toList();
        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT order_id FROM orders WHERE request_token = ? AND user_id = ?")) {
                    ps.setString(1, requestToken); ps.setInt(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) { long id = rs.getLong(1); conn.commit(); return id; }
                    }
                }
                BigDecimal total = BigDecimal.ZERO;
                for (CartItem_24133003 item : items) {
                    if (item.getQuantity() <= 0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT title, price, quantity FROM books WITH (UPDLOCK, HOLDLOCK) WHERE bookid = ?")) {
                        ps.setInt(1, item.getBook().getBookid());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next() || rs.getInt("quantity") < item.getQuantity())
                                throw new IllegalArgumentException("Sách đã bị gỡ hoặc không đủ tồn kho. Vui lòng kiểm tra lại giỏ hàng.");
                            BigDecimal price = rs.getBigDecimal("price");
                            if (price == null || price.signum() < 0 || item.getBook().getPrice() == null
                                    || price.compareTo(item.getBook().getPrice()) != 0)
                                throw new IllegalArgumentException("Giá sách đã thay đổi. Vui lòng xem lại giỏ hàng trước khi đặt.");
                            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
                        }
                    }
                }
                long orderId;
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO orders(user_id, request_token, recipient, phone, address, note, total) VALUES(?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId); ps.setString(2, requestToken);
                    ps.setNString(3, shipping.recipient()); ps.setString(4, shipping.phone());
                    ps.setNString(5, shipping.address()); ps.setNString(6, shipping.note()); ps.setBigDecimal(7, total);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("Không lấy được mã đơn hàng.");
                        orderId = rs.getLong(1);
                    }
                }
                for (CartItem_24133003 item : items) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO order_items(order_id, book_id, title, unit_price, quantity) "
                            + "SELECT ?, bookid, title, price, ? FROM books WHERE bookid = ?")) {
                        ps.setLong(1, orderId); ps.setInt(2, item.getQuantity()); ps.setInt(3, item.getBook().getBookid());
                        if (ps.executeUpdate() != 1) throw new SQLException("Không lưu được chi tiết đơn.");
                    }
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE books SET quantity = quantity - ? WHERE bookid = ? AND quantity >= ?")) {
                        ps.setInt(1, item.getQuantity()); ps.setInt(2, item.getBook().getBookid()); ps.setInt(3, item.getQuantity());
                        if (ps.executeUpdate() != 1) throw new SQLException("Không cập nhật được tồn kho.");
                    }
                }
                conn.commit();
                return orderId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public Order_24133003 findForUser(long orderId, int userId) throws Exception {
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM orders WHERE order_id = ? AND user_id = ?")) {
            ps.setLong(1, orderId); ps.setInt(2, userId);
            Order_24133003 order = new Order_24133003();
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                order.id = rs.getLong("order_id"); order.recipient = rs.getNString("recipient");
                order.phone = rs.getString("phone"); order.address = rs.getNString("address");
                order.note = rs.getNString("note"); order.total = rs.getBigDecimal("total");
                order.createdAt = rs.getTimestamp("created_at");
            }
            try (PreparedStatement detail = conn.prepareStatement("SELECT * FROM order_items WHERE order_id = ? ORDER BY book_id")) {
                detail.setLong(1, orderId);
                try (ResultSet rs = detail.executeQuery()) {
                    while (rs.next()) order.items.add(new Order_24133003.Item(rs.getNString("title"),
                            rs.getInt("quantity"), rs.getBigDecimal("unit_price")));
                }
            }
            return order;
        }
    }
}
