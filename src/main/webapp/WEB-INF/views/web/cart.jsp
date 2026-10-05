<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html><head><title>Giỏ hàng</title></head><body>
<div class="container">
    <h2 class="mb-4"><i class="bi bi-cart3 me-2"></i>Giỏ hàng của bạn</h2>
    <c:choose><c:when test="${sessionScope.cart.itemCount > 0}">
    <div class="row g-4"><div class="col-lg-8"><div class="table-responsive card shadow-sm">
    <table class="table align-middle mb-0"><thead><tr><th>Sách</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th><th></th></tr></thead><tbody>
    <c:forEach items="${sessionScope.cart.items}" var="item"><tr>
        <td><a href="${pageContext.request.contextPath}/book/detail?id=${item.book.bookid}"><c:out value="${item.book.title}"/></a><div class="small text-muted">Kho còn ${item.book.quantity} cuốn</div></td>
        <td><fmt:formatNumber value="${item.book.price}"/> VNĐ</td>
        <td><form action="${pageContext.request.contextPath}/cart/update" method="post" class="quantity-form"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <input type="hidden" name="bookId" value="${item.book.bookid}">
            <div class="input-group input-group-sm flex-nowrap" style="min-width:150px">
                <button type="button" class="btn btn-outline-secondary" data-step="-1" aria-label="Giảm số lượng">−</button>
                <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.book.quantity}" step="1" required class="form-control text-center" aria-label="Số lượng sách">
                <button type="button" class="btn btn-outline-secondary" data-step="1" aria-label="Tăng số lượng">+</button>
            </div><button class="btn btn-link btn-sm" type="submit">Cập nhật</button>
        </form></td>
        <td><fmt:formatNumber value="${item.totalPrice}"/> VNĐ</td>
        <td><form action="${pageContext.request.contextPath}/cart/delete" method="post"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="bookId" value="${item.book.bookid}">
            <button class="btn btn-outline-danger btn-sm" aria-label="Xóa sách khỏi giỏ">Xóa</button>
        </form></td>
    </tr></c:forEach></tbody></table></div>
    <form action="${pageContext.request.contextPath}/cart/clear" method="post" class="mt-3" onsubmit="return confirm('Xóa toàn bộ giỏ hàng?')"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><button class="btn btn-outline-danger">Xóa toàn bộ</button>
    </form></div>
    <div class="col-lg-4"><div class="card p-4 shadow-sm"><h4>Tổng đơn hàng</h4>
        <p>${sessionScope.cart.totalQuantity} cuốn sách</p><p class="fs-4 text-danger fw-bold"><fmt:formatNumber value="${sessionScope.cart.totalAmount}"/> VNĐ</p>
        <p class="text-muted">Thanh toán bằng tiền mặt khi nhận hàng (COD). Phí giao hàng: 0 VNĐ.</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/cart/checkout">Tiến hành đặt hàng COD</a>
    </div></div></div>
    </c:when><c:otherwise><div class="card p-5 text-center"><h4>Giỏ hàng đang trống</h4><p>Chọn sách để bắt đầu đặt hàng.</p></div></c:otherwise></c:choose>
    <a href="${pageContext.request.contextPath}/books" class="btn btn-outline-primary mt-4">Tiếp tục mua sách</a>
</div>
<script>
document.querySelectorAll('.quantity-form').forEach(function(form) {
    var input = form.querySelector('[name="quantity"]');
    var buttons = form.querySelectorAll('[data-step]');
    function sync() {
        buttons[0].disabled = Number(input.value) <= 1;
        buttons[1].disabled = Number(input.value) >= Number(input.max);
    }
    buttons.forEach(function(button) {
        button.addEventListener('click', function() {
            var next = Number(input.value) + Number(button.dataset.step);
            if (Number.isInteger(next) && next >= 1 && next <= Number(input.max)) {
                input.value = next; sync(); form.requestSubmit();
            }
        });
    });
    input.addEventListener('input', sync); sync();
});
</script>
</body></html>
