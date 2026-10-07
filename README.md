# BookStore — Java Servlet/JSP

Ứng dụng bán sách dùng Java 21+, Maven, Tomcat 10.1 và SQL Server. Có trang chủ, danh mục/phân trang theo tác giả, chi tiết/đánh giá sách, đăng ký qua OTP, đăng nhập, quản trị sách, giỏ hàng và đặt hàng COD.

## Chạy dự án

1. Cài JDK 21 trở lên, Maven, SQL Server và Tomcat 10.1.
2. Với database mới, chạy `database.sql` trong SQL Server Management Studio hoặc `sqlcmd`. Script tạo database `WebExamDB` và dữ liệu mẫu; không chạy lại trên database đang có.
3. Với database cũ, chạy lần lượt các script `001_cod_orders.sql`, `002_password_hash.sql`, `003_unicode_catalog.sql`, `004_order_statuses.sql` trong `database/migrations` trên database đó. Không cần tạo lại dữ liệu.
4. Cấu hình các biến môi trường như `.env.example` **trong tiến trình khởi động Tomcat**. Ứng dụng không tự đọc `.env`. Có thể dùng Java system properties cùng tên. Không đưa mật khẩu thật vào Git.
5. Chạy `mvn clean verify`, chép `target/kiemtraweb.war` vào thư mục `webapps` của Tomcat và mở `http://localhost:8080/kiemtraweb/`.

Ví dụ PowerShell (thay giá trị bằng cấu hình của bạn):

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-21'
$env:DB_URL = 'jdbc:sqlserver://localhost:1433;databaseName=WebExamDB;encrypt=true;trustServerCertificate=true;sslProtocol=TLSv1.2;loginTimeout=5;socketTimeout=10000'
$env:DB_USER = 'sa'
$env:DB_PASSWORD = 'your-local-password'
$env:SMTP_USER = 'your-email@gmail.com'
$env:SMTP_PASSWORD = 'your-app-password'
mvn clean verify
# Khởi động Tomcat từ cùng cửa sổ PowerShell để nhận các biến môi trường.
```

`trustServerCertificate=true` phục vụ SQL Server chạy local. Với server có chứng chỉ hợp lệ, dùng `trustServerCertificate=false`. Tham khảo [cấu hình kết nối JDBC của Microsoft](https://learn.microsoft.com/en-us/sql/connect/jdbc/setting-the-connection-properties).

Tài khoản mẫu: `admin@gmail.com / 123456`, `user@gmail.com / 123456`. Đây là dữ liệu học tập. Mật khẩu cũ được chuyển sang PBKDF2 khi đăng nhập thành công; tài khoản mới luôn lưu mật khẩu đã băm. Cần SMTP hợp lệ để hoàn tất đăng ký OTP; không hiển thị OTP khi gửi email thất bại.

## Giỏ hàng và COD

- Thêm sách, cộng gộp cùng mã sách, sửa số lượng, tăng/giảm, xóa một sách hoặc toàn bộ giỏ.
- Kiểm tra số nguyên dương ở server; giới hạn theo tồn kho hiện tại, xử lý số lớn và sách hết hàng/bị xóa.
- Các thao tác thay đổi dữ liệu dùng POST và CSRF token. JSP đặt dưới `WEB-INF`.
- Nhập tên, điện thoại, địa chỉ và ghi chú; xem tổng tiền trước khi xác nhận COD. Phí giao hàng hiện là 0 VNĐ.
- Đơn mới có trạng thái `PENDING`, phương thức `COD`, thanh toán `UNPAID`: khách trả tiền mặt khi nhận hàng.
- Giao dịch SQL khóa tồn kho theo thứ tự mã sách, kiểm tra lại giá/số lượng, lưu đơn và chi tiết, trừ kho rồi commit. Nếu lỗi, rollback và giữ giỏ.
- Token đặt hàng duy nhất chống gửi trùng; sửa giỏ làm hết hiệu lực biểu mẫu thanh toán cũ.
- Trang `/order?id=...` chỉ cho chủ đơn xem. Tên/giá/số lượng sách được lưu tại thời điểm đặt, giữ nguyên khi sách bị sửa hoặc xóa.

## Lịch sử đặt hàng

Đăng nhập và chọn **Lịch sử đặt hàng** trên menu hoặc mở `/orders`. Có bộ lọc Tất cả và 8 trạng thái, số đơn ở từng trạng thái, phân trang 10 đơn/trang và liên kết xem chi tiết. Mỗi tài khoản chỉ thấy đơn của mình. Tải lại trang sau khi cập nhật SQL để đọc trạng thái mới từ database.

| Giá trị `orders.order_status` | Hiển thị |
| --- | --- |
| `PENDING` | Đơn hàng mới |
| `CONFIRMED` | Đã xác nhận |
| `PREPARING` | Chuẩn bị hàng |
| `SHIPPING` | Vận chuyển |
| `OUT_FOR_DELIVERY` | Giao hàng |
| `DELIVERED` | Đã giao |
| `CANCELLED` | Đơn hàng hủy |
| `RETURNED` | Đơn hàng hoàn |

Chạy `database/demo/order_history.sql` trên database local sau migration 004 để tạo 8 đơn minh họa cho tài khoản `user@gmail.com / 123456`. Script chạy lại không tạo thêm đơn mẫu, không trừ kho và đánh dấu đơn mẫu trong cột `note`. Với `sqlcmd`, dùng `-f 65001` để đọc file UTF-8 đúng dấu tiếng Việt:

```powershell
sqlcmd -S localhost -E -C -b -f 65001 -d WebExamDB -i database/demo/order_history.sql
```

Ví dụ đổi trạng thái một đơn minh họa trong SQL Server rồi tải lại `/orders`:

```sql
UPDATE dbo.orders
SET order_status = 'DELIVERED'
WHERE user_id = (SELECT id FROM dbo.users WHERE email = 'user@gmail.com')
  AND note LIKE N'[[]DEMO_HISTORY:PENDING]%';
```

Chuyển lại `'PENDING'` bằng cùng câu lệnh để đưa đơn mẫu về bộ lọc Đơn hàng mới. Có thể thay bằng bất kỳ mã trạng thái trong bảng trên. Đơn đặt COD thực tế luôn bắt đầu ở `PENDING`.

Việc đổi `order_status` trực tiếp chỉ thay trạng thái hiển thị; không tự thu tiền, hoàn tiền hoặc hoàn kho. Chưa có quy trình vận hành các thao tác này hoặc màn hình quản trị chuyển trạng thái.

## Kiểm thử

`mvn clean verify` chạy unit tests và đóng gói WAR. Kiểm thử HTTP/JSP và SQL thật dùng profile `integration`:

1. Tạo **database riêng** có tên bắt đầu `WebAssignmentCodTest_`, dùng nội dung `database.sql` và thay tên database trong script. Không dùng database đang sử dụng.
2. Đặt `DB_URL`, `DB_USER`, `DB_PASSWORD` trỏ tới database kiểm thử.
3. Chạy `mvn clean verify -Pintegration`. Tests tự chạy Tomcat nhúng trên cổng ngẫu nhiên.

Integration tests xóa dữ liệu đơn hàng trong database kiểm thử để cô lập từng ca. Chúng kiểm tra giỏ hàng, giới hạn số lượng, CSRF, quyền truy cập, nhập thông tin COD, gửi trùng, giá/tồn kho thay đổi, snapshot đơn hàng, rollback khi lỗi SQL và hai khách tranh mua cuốn cuối.

Chi tiết kết quả thực tế và giới hạn kiểm tra: [docs/verification.md](docs/verification.md).
