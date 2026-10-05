<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html><head><title>Đơn hàng #${order.id}</title></head><body>
<div class="container"><div class="alert alert-success"><h3>Đặt hàng thành công!</h3>Đơn hàng #${order.id} đã được lưu. Bạn sẽ thanh toán khi nhận hàng.</div>
<div class="row g-4"><div class="col-lg-5"><div class="card p-4"><h4>Thông tin giao hàng</h4>
<p><strong>Người nhận:</strong> <c:out value="${order.recipient}"/></p><p><strong>Điện thoại:</strong> <c:out value="${order.phone}"/></p>
<p><strong>Địa chỉ:</strong> <c:out value="${order.address}"/></p><p><strong>Ghi chú:</strong> <c:out value="${order.note}"/></p>
<p>Trạng thái đơn: <strong>Chờ xác nhận</strong></p><p>Thanh toán: <strong>COD — Chưa thanh toán</strong></p>
<p>Ngày đặt: <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p></div></div>
<div class="col-lg-7"><div class="card p-4"><h4>Sách đã đặt</h4><div class="table-responsive"><table class="table"><thead><tr><th>Sách</th><th>Số lượng</th><th>Thành tiền</th></tr></thead><tbody>
<c:forEach items="${order.items}" var="item"><tr><td><c:out value="${item.title}"/></td><td>${item.quantity}</td><td><fmt:formatNumber value="${item.total}"/> VNĐ</td></tr></c:forEach>
</tbody></table></div><p>Phí giao hàng: 0 VNĐ</p><p class="fs-4 fw-bold text-danger">Tổng thanh toán: <fmt:formatNumber value="${order.total}"/> VNĐ</p></div></div></div>
<a href="${pageContext.request.contextPath}/books" class="btn btn-primary mt-4">Tiếp tục mua sách</a></div>
</body></html>
