<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<html>
<head>
    <title>Tất Cả Sản Phẩm Sách</title>
</head>
<body>
<div class="container">

    <div class="d-flex justify-content-between align-items-center border-bottom pb-2 mb-4">
        <h4 class="fw-bold text-dark mb-0">
            <i class="bi bi-grid-3x3-gap me-2 text-primary"></i>Tất Cả Sản Phẩm Sách
        </h4>
        <span class="badge bg-primary">Tổng số: ${totalBooks} cuốn</span>
    </div>

    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty bookList}">
                <c:forEach items="${bookList}" var="b">
                    <div class="col-lg-4 col-md-6">
                        <div class="card h-100 shadow-sm book-card border-0 bg-white">
                            <div class="position-relative overflow-hidden">
                                <a href="${pageContext.request.contextPath}/book/detail?id=${b.bookid}">
                                    <img src="<c:out value="${b.coverImage}"/>" class="book-cover" alt="<c:out value="${b.title}"/>"
                                         onerror="this.src='https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500';">
                                </a>
                                <span class="position-absolute top-0 end-0 bg-warning text-dark px-2 py-1 m-2 rounded-pill small fw-bold shadow-sm">
                                    <i class="bi bi-star-fill text-danger me-1"></i>${b.avgRating > 0 ? String.format("%.1f", b.avgRating) : 'Mới'}
                                </span>
                            </div>

                            <div class="card-body d-flex flex-column p-3">
                                <h5 class="card-title fw-bold mb-2">
                                    <a href="${pageContext.request.contextPath}/book/detail?id=${b.bookid}"
                                       class="text-decoration-none text-primary hover-link">
                                        Tiêu đề: <c:out value="${b.title}"/>
                                    </a>
                                </h5>

                                <p class="card-text text-secondary small mb-1">
                                    <strong>Mã isbn:</strong> ${b.isbn != null ? b.isbn : 'Đang cập nhật'}
                                </p>
                                <p class="card-text text-secondary small mb-1">
                                    <strong>Tác giả:</strong> <span class="text-dark fw-semibold"><c:out value="${not empty b.authorNames ? b.authorNames : 'Đang cập nhật'}"/></span>
                                </p>
                                <p class="card-text text-secondary small mb-1">
                                    <strong>Publisher:</strong> <c:out value="${b.publisher != null ? b.publisher : 'NXB Tổng hợp'}"/>
                                </p>
                                <p class="card-text text-secondary small mb-1">
                                    <strong>Publisher_date:</strong> ${b.publishDate != null ? b.publishDate : 'N/A'}
                                </p>
                                <p class="card-text text-secondary small mb-1">
                                    <strong>Quantity:</strong> <span class="badge ${b.quantity > 0 ? 'bg-success' : 'bg-danger'}">${b.quantity}</span>
                                </p>
                                <p class="card-text text-secondary small mb-3">
                                    <strong class="text-warning-emphasis">Review (${b.reviewCount})</strong>
                                </p>

                                <div class="mt-auto pt-2 border-top d-flex justify-content-between align-items-center">
                                    <span class="fs-5 fw-bold text-danger">${b.price} VNĐ</span>
                                    <div class="d-flex gap-1">
                                        <c:choose>
                                            <c:when test="${b.quantity > 0}">
                                                <form method="post" action="${pageContext.request.contextPath}/cart/add"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="bookId" value="${b.bookid}"><button class="btn btn-outline-warning text-dark btn-sm rounded-pill px-2" title="Thêm vào giỏ hàng"><i class="bi bi-cart-plus-fill"></i></button></form>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-outline-secondary btn-sm rounded-pill px-2" disabled title="Hết hàng">
                                                    <i class="bi bi-cart-x"></i>
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.request.contextPath}/book/detail?id=${b.bookid}" class="btn btn-outline-primary btn-sm rounded-pill px-3">
                                            Chi tiết <i class="bi bi-arrow-right"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12 text-center py-5">
                    <p class="text-muted">Không có cuốn sách nào trong kho.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Phân trang -->
    <c:if test="${totalPages > 1}">
        <div class="d-flex justify-content-center align-items-center mt-5">
            <nav aria-label="Page navigation">
                <ul class="pagination pagination-md shadow-sm mb-0">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/books?page=${currentPage - 1}">
                            « Trang trước
                        </a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/books?page=${i}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/books?page=${currentPage + 1}">
                            Trang sau »
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </c:if>

</div>
</body>
</html>
