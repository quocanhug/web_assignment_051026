<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html><head><title>Lịch sử đặt hàng</title></head><body>
<div class="container">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
        <div><h2>Lịch sử đặt hàng</h2><p class="text-muted mb-0">Theo dõi trạng thái và xem lại các đơn hàng của bạn.</p></div>
        <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/books">Tiếp tục mua sách</a>
    </div>
    <nav class="d-flex flex-wrap gap-2 mb-4" aria-label="Lọc trạng thái đơn hàng">
        <a class="btn btn-sm ${empty selectedStatus ? 'btn-primary' : 'btn-outline-secondary'}"
           href="${pageContext.request.contextPath}/orders" aria-current="${empty selectedStatus ? 'page' : 'false'}">Tất cả (${allCount})</a>
        <c:forEach items="${statuses}" var="s">
            <a class="btn btn-sm ${selectedStatus eq s.code ? 'btn-primary' : 'btn-outline-secondary'}"
               href="${pageContext.request.contextPath}/orders?status=${s.code}" aria-current="${selectedStatus eq s.code ? 'page' : 'false'}"><c:out value="${s.label}"/> (${statusCounts[s]})</a>
        </c:forEach>
    </nav>
    <p class="text-muted">Tìm thấy ${total} đơn hàng. Trang ${currentPage}/${totalPages}.</p>
    <c:choose><c:when test="${empty orders}">
        <div class="card p-5 text-center"><h4>${empty selectedStatus ? 'Bạn chưa có đơn hàng nào' : 'Chưa có đơn hàng ở trạng thái này'}</h4>
            <p class="mb-0 text-muted">Các đơn hàng mới sẽ xuất hiện tại đây sau khi đặt hàng thành công.</p></div>
    </c:when><c:otherwise>
        <div class="row g-3">
            <c:forEach items="${orders}" var="order">
                <div class="col-12"><article class="card shadow-sm p-4" data-order-id="${order.id}">
                    <div class="d-flex flex-wrap justify-content-between gap-2 border-bottom pb-3 mb-3">
                        <div><h5 class="mb-1">Đơn hàng #${order.id}</h5><span class="small text-muted">Ngày đặt: <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span></div>
                        <div><span class="badge ${order.status.badgeClass} px-3 py-2"><c:out value="${order.status.label}"/></span></div>
                    </div>
                    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3">
                        <div><p class="mb-1">Người nhận: <strong><c:out value="${order.recipient}"/></strong></p><span class="text-muted">${order.totalQuantity} cuốn sách · Thanh toán khi nhận hàng (COD)</span></div>
                        <div class="text-end"><p class="text-danger fw-bold fs-5 mb-2"><fmt:formatNumber value="${order.total}"/> VNĐ</p>
                            <a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/order?id=${order.id}">Xem chi tiết #${order.id}</a></div>
                    </div>
                </article></div>
            </c:forEach>
        </div>
        <nav class="d-flex justify-content-center align-items-center gap-3 mt-4" aria-label="Phân trang lịch sử">
            <c:if test="${currentPage > 1}"><a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/orders?status=${selectedStatus}&amp;page=${currentPage - 1}">Trang trước</a></c:if>
            <span>Trang ${currentPage}/${totalPages}</span>
            <c:if test="${currentPage < totalPages}"><a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/orders?status=${selectedStatus}&amp;page=${currentPage + 1}">Trang sau</a></c:if>
        </nav>
    </c:otherwise></c:choose>
</div></body></html>
