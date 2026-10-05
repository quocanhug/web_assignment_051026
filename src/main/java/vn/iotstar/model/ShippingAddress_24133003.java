package vn.iotstar.model;

public record ShippingAddress_24133003(String recipient, String phone, String address, String note) {
    public ShippingAddress_24133003 {
        recipient = recipient == null ? "" : recipient.trim();
        phone = phone == null ? "" : phone.trim();
        address = address == null ? "" : address.trim();
        note = note == null ? "" : note.trim();
        if (recipient.length() < 2 || recipient.length() > 100)
            throw new IllegalArgumentException("Tên người nhận phải có từ 2 đến 100 ký tự.");
        if (!phone.matches("(?:0[0-9]{9}|\\+84[0-9]{9})"))
            throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số bắt đầu bằng 0 hoặc dùng đầu +84.");
        if (address.length() < 10 || address.length() > 500)
            throw new IllegalArgumentException("Địa chỉ nhận hàng phải có từ 10 đến 500 ký tự.");
        if (note.length() > 1000)
            throw new IllegalArgumentException("Ghi chú tối đa 1000 ký tự.");
    }
}
