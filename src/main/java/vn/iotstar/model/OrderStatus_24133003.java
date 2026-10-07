package vn.iotstar.model;

public enum OrderStatus_24133003 {
    PENDING("Đơn hàng mới", "bg-secondary"),
    CONFIRMED("Đã xác nhận", "bg-primary"),
    PREPARING("Chuẩn bị hàng", "bg-info text-dark"),
    SHIPPING("Vận chuyển", "bg-primary"),
    OUT_FOR_DELIVERY("Giao hàng", "bg-warning text-dark"),
    DELIVERED("Đã giao", "bg-success"),
    CANCELLED("Đơn hàng hủy", "bg-danger"),
    RETURNED("Đơn hàng hoàn", "bg-dark");

    private final String label;
    private final String badgeClass;
    OrderStatus_24133003(String label, String badgeClass) { this.label = label; this.badgeClass = badgeClass; }
    public String getCode() { return name(); }
    public String getLabel() { return label; }
    public String getBadgeClass() { return badgeClass; }
}
