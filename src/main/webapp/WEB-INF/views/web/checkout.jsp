<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html><head><title>Đặt hàng COD</title></head><body>
<div class="container"><h2 class="mb-4">Xác nhận đặt hàng COD</h2>
<c:if test="${not empty requestScope.error}"><div class="alert alert-danger" role="alert"><c:out value="${requestScope.error}"/> <a href="${pageContext.request.contextPath}/cart">Kiểm tra lại giỏ hàng</a></div></c:if>
<div class="row g-4"><div class="col-lg-7"><form method="post" action="${pageContext.request.contextPath}/cart/checkout" class="card p-4 shadow-sm" id="checkoutForm"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
    <input type="hidden" name="checkoutToken" value="${sessionScope.checkoutToken}">
    <input type="hidden" name="paymentMethod" value="COD">
    <label class="form-label" for="recipient">Tên người nhận</label>
    <input class="form-control mb-3" id="recipient" name="recipient" required minlength="2" maxlength="100" autocomplete="name" value="<c:out value='${submitted ? param.recipient : sessionScope.user.fullname}'/>">
    <label class="form-label" for="phone">Số điện thoại</label>
    <input class="form-control mb-3" id="phone" name="phone" type="tel" required pattern="(0[0-9]{9}|\+84[0-9]{9})" maxlength="20" autocomplete="tel" placeholder="0901234567" value="<c:out value='${param.phone}'/>">
    <label class="form-label" for="address">Địa chỉ giao hàng</label>
    <textarea class="form-control mb-3" id="address" name="address" required minlength="10" maxlength="500" autocomplete="street-address" placeholder="Số nhà, đường, phường/xã, tỉnh/thành phố"><c:out value="${param.address}"/></textarea>
    <label class="form-label" for="note">Ghi chú (không bắt buộc)</label>
    <textarea class="form-control mb-3" id="note" name="note" maxlength="1000"><c:out value="${param.note}"/></textarea>
    <div class="alert alert-info"><strong>Thanh toán khi nhận hàng (COD)</strong><br>Bạn thanh toán tiền mặt cho nhân viên giao hàng khi nhận sách.</div>
    <button type="submit" class="btn btn-primary btn-lg">Xác nhận đặt hàng COD</button>
</form></div><div class="col-lg-5"><div class="card p-4 shadow-sm"><h4>Đơn hàng của bạn</h4>
    <c:forEach items="${sessionScope.cart.items}" var="item"><div class="d-flex justify-content-between border-bottom py-2"><span><c:out value="${item.book.title}"/> × ${item.quantity}</span><strong><fmt:formatNumber value="${item.totalPrice}"/> VNĐ</strong></div></c:forEach>
    <p class="mt-3">Phí giao hàng: 0 VNĐ</p><p class="fs-4">Tổng thanh toán: <strong class="text-danger"><fmt:formatNumber value="${sessionScope.cart.totalAmount}"/> VNĐ</strong></p>
    <a href="${pageContext.request.contextPath}/cart">Quay lại giỏ hàng</a>
</div></div></div></div>
<script>document.getElementById('checkoutForm').addEventListener('submit', function() { this.querySelector('button[type="submit"]').disabled = true; });</script>
</body></html>
