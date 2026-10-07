# Lịch sử đặt hàng và kiểm tra trạng thái

Trang `/orders` yêu cầu đăng nhập, đọc các đơn của người dùng từ SQL Server, lọc theo trạng thái, đếm từng nhóm và phân trang. Thứ tự hiển thị là ngày đặt mới nhất, rồi mã đơn giảm dần. Trang chi tiết `/order?id=...` cũng đọc trạng thái thật của cùng đơn; không còn hiển thị cố định “Chờ xác nhận”.

Migration `004_order_statuses.sql` thay ràng buộc chỉ cho phép `PENDING` bằng 8 mã trạng thái. Có thể chạy migration nhiều lần; các đơn cũ giữ nguyên trạng thái. Database mới sử dụng `database.sql` đã có ràng buộc tương ứng.

Đã tạo 8 đơn minh họa trong database local `WebExamDB` cho `user@gmail.com`, đánh dấu `DEMO_HISTORY` trong ghi chú. Các đơn này không trừ kho. Mã trạng thái và ví dụ thay đổi SQL nằm trong [README](../README.md#lịch-sử-đặt-hàng).

Các ca kiểm tra bổ sung gồm: chuyển hướng đăng nhập và lịch sử rỗng; đủ 8 trạng thái và trạng thái chi tiết; đổi trạng thái bằng SQL rồi kiểm tra đơn chuyển nhóm; số đếm đúng; phân trang 10 đơn; không lộ đơn của tài khoản khác; từ chối bộ lọc/trang sai và ràng buộc SQL chặn trạng thái không hợp lệ.

Kiểm tra trên trình duyệt ngày 07/10/2026: đăng nhập tài khoản mẫu và xem đủ 8 nhóm, mỗi nhóm một đơn; đổi trực tiếp đơn mẫu `PENDING` sang `DELIVERED` trong SQL Server rồi tải lại trang. Nhóm Đơn hàng mới còn 0 đơn, nhóm Đã giao có 2 đơn và trang chi tiết hiển thị Đã giao. Sau kiểm tra, đã khôi phục đơn mẫu về `PENDING`. Chạy lại migration 004 và script demo thành công, vẫn có 8 đơn mẫu và tổng tồn kho local giữ nguyên 508 cuốn.

`mvn -B clean verify -Pintegration` hoàn tất ngày 07/10/2026 với **BUILD SUCCESS**: 14 unit tests và 25 integration tests, tổng 39 tests; không lỗi, thất bại hoặc bỏ qua. Integration tests dùng database riêng `WebAssignmentCodTest_20261005103445`, không xóa dữ liệu của `WebExamDB`.
