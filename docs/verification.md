# Kết quả kiểm tra — 05/10/2026

## Kết quả thực tế

- Build WAR thành công.
- 14 unit tests + 21 integration tests = **35 tests thành công**, 0 failures, 0 errors, 0 skipped.
- Lệnh cuối: `mvn verify -Pintegration`, hoàn tất lúc 10:49:55 (UTC+7). Một lượt `mvn clean verify -Pintegration` trước đó cũng thành công với 34 tests; sau đó bổ sung kiểm tra thông báo cũ khi quay lại checkout.
- Môi trường: Windows, JDK 25 (biên dịch với `release=21`), Maven 3.9.16, Tomcat 10.1.59, Microsoft JDBC 13.6.0, SQL Server 2025.
- Integration tests chạy trên database riêng có tiền tố `WebAssignmentCodTest_`, Tomcat nhúng mở cổng ngẫu nhiên.
- Kiểm tra thủ công bằng trình duyệt trên Tomcat độc lập: thêm sách từ trang chi tiết, tăng số lượng, tổng tiền cập nhật, đăng nhập rồi quay lại checkout, nhập thông tin giao hàng, đặt COD thành công và xem xác nhận đơn.
- Đã chạy ba migration trên database local `WebExamDB`: vẫn giữ nguyên 14 sách, tổng tồn kho 508 và 6 người dùng; chưa tạo đơn thử vào database này.

## Các phần đã kiểm tra

| Phần | Kiểm tra |
| --- | --- |
| Tổng quan | Trang chủ, danh mục, phân trang với dữ liệu đầu vào sai, trang chi tiết, đăng nhập, đăng ký; JSP trong `WEB-INF` không truy cập trực tiếp được |
| Tài khoản | Đăng nhập sai/đúng, quyền quản trị, đăng xuất bằng POST, băm và nâng cấp mật khẩu cũ, giữ giỏ sau đăng nhập |
| Quản trị sách | Mở form thêm/sửa, thêm/sửa/xóa sách, chặn giá và tồn kho âm |
| Đánh giá | Chặn mã sách sai và điểm ngoài 1–5; tạo/sửa đánh giá; escape nội dung HTML |
| Giỏ hàng | Thêm mới, cộng gộp, sửa số lượng, xóa một mục, xóa tất cả, giỏ rỗng, tính tiền chính xác |
| Giới hạn | Số lượng 0/âm/phân số/chữ/vượt kiểu int, cộng số lớn không tràn, giới hạn tồn kho, tồn kho giảm, sách bị xóa |
| Bảo vệ thao tác | POST + CSRF, GET không thay đổi giỏ, form checkout cũ hết hiệu lực khi sửa giỏ |
| COD | Kiểm tra tên/điện thoại/địa chỉ/ghi chú, giữ số 0 đầu điện thoại, giữ dấu tiếng Việt, chỉ chấp nhận COD |
| Lưu đơn | Tổng tiền, chi tiết sách, thông tin giao hàng, trạng thái chờ xác nhận/chưa thanh toán, chỉ chủ đơn được xem |
| Tính toàn vẹn | Kiểm tra lại giá và tồn kho, trừ kho sau đặt thành công, rollback khi SQL lỗi, giữ giỏ khi lỗi |
| Gửi trùng/cạnh tranh | Cùng token không tạo thêm đơn hoặc trừ kho lần nữa; hai khách tranh mua cuốn cuối chỉ một người thành công |
| Lịch sử dữ liệu | Chi tiết đơn giữ nguyên khi sách bị xóa; escape dữ liệu người dùng trong giỏ/đơn |

## Những lỗi đã sửa

Checkout cũ chỉ xóa giỏ và hiển thị thành công. Nay đơn và chi tiết được lưu cùng thao tác trừ kho trong giao dịch SQL. Giỏ cũ có thể tràn số và dùng tồn kho cũ; nay kiểm tra server và cập nhật từ database. Các thao tác thay đổi dữ liệu qua GET được chuyển sang POST có CSRF.

Đã bỏ mật khẩu DB/SMTP khỏi mã nguồn, bỏ việc hiển thị OTP khi gửi email thất bại, băm mật khẩu tài khoản mới và nâng cấp mật khẩu cũ khi đăng nhập. Các dữ liệu văn bản hiển thị được escape. Những trường catalog dùng `VARCHAR`/`TEXT` được đổi sang Unicode để tránh mất dấu; tăng độ rộng giá tiền và URL ảnh.

JDBC được nâng cấp từ phiên bản cũ sang 13.6.0. Nguồn chính thức: [Microsoft JDBC release 13.6.0](https://github.com/microsoft/mssql-jdbc/releases/tag/v13.6.0). Cấu hình kết nối local dùng TLS 1.2: [Microsoft connection properties](https://learn.microsoft.com/en-us/sql/connect/jdbc/setting-the-connection-properties).

## Giới hạn của kết quả

Chưa gửi OTP tới email thật vì cần cấu hình SMTP riêng. Các trang/form đăng ký được kiểm tra, nhưng chưa xác nhận gửi và nhận email ngoài hệ thống. Không coi 35 tests là bảo đảm đã kiểm tra mọi tổ hợp dữ liệu của toàn bộ ứng dụng.

COD hiện hỗ trợ tạo và xác nhận đơn, với phí giao hàng 0 VNĐ. Chưa có màn hình vận hành chuyển trạng thái giao hàng, ghi nhận thu tiền, hủy đơn hoặc hoàn kho.

Thông tin mật khẩu SMTP từng xuất hiện trực tiếp trong mã cũ cần được chủ tài khoản thay mới. Các mật khẩu thật, cấu hình local, log, ảnh kiểm tra và bản build không được đưa lên repository.
