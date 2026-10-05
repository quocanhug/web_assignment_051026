<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<html>
<head>
    <title><c:out value="${authorId > 0 ? ('Tác giả: ' += currentAuthor.authorName) : 'Trang Chủ - Sách Nổi Bật'}"/></title>
</head>
<body>
<div class="container">

    <!-- Bộ lọc chọn Tác giả & Tất cả sách -->
    <div class="card shadow-sm border-0 mb-4 bg-white">
        <div class="card-body p-3">
            <div class="d-flex align-items-center flex-wrap gap-2">
                <span class="fw-bold me-2 text-secondary"><i class="bi bi-funnel-fill me-1 text-primary"></i>Danh mục lọc:</span>

                <!-- Nút Tất cả sách (Hiển thị tất cả sản phẩm) -->
                <a href="${pageContext.request.contextPath}/home?authorId=0"
                   class="btn btn-sm ${authorId == 0 ? 'btn-primary shadow-sm fw-bold' : 'btn-outline-secondary'} rounded-pill px-3">
                    <i class="bi bi-grid-fill me-1"></i>Tất cả sách
                </a>

                <!-- Các nút Tác giả -->
                <c:forEach items="${authors}" var="author">
                    <a href="${pageContext.request.contextPath}/home?authorId=${author.authorId}"
                       class="btn btn-sm ${author.authorId == authorId ? 'btn-primary shadow-sm fw-bold' : 'btn-outline-secondary'} rounded-pill px-3">
                        <i class="bi bi-person me-1"></i><c:out value="${author.authorName}"/>
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- Tiêu đề theo chế độ hiển thị -->
    <div class="d-flex justify-content-between align-items-center border-bottom pb-2 mb-4">
        <c:choose>
            <c:when test="${authorId > 0}">
                <h4 class="fw-bold text-dark mb-0">
                    Tác giả :<span class="text-primary"><c:out value="${currentAuthor.authorName}"/></span>
                </h4>
            </c:when>
            <c:otherwise>
                <h4 class="fw-bold text-dark mb-0">
                    <i class="bi bi-stars text-warning me-1"></i>Sản Phẩm Nổi Bật
                </h4>
            </c:otherwise>
        </c:choose>
        <span class="badge bg-secondary fs-6">Tổng số: ${totalBooks} cuốn sách</span>
    </div>

    <!-- Lưới sản phẩm -->
    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty bookList}">
                <c:forEach items="${bookList}" var="b">
                    <div class="col-lg-4 col-md-6">
                        <div class="card h-100 shadow-sm book-card border-0 bg-white">
                            <!-- [cover_image] -->
                            <div class="position-relative overflow-hidden">
                                <a href="${pageContext.request.contextPath}/book/detail?id=${b.bookid}">
                                    <img src="<c:out value="${b.coverImage}"/>" class="book-cover" alt="<c:out value="${b.title}"/>"
                                         onerror="this.src='https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500';">
                                </a>
                                <span class="position-absolute top-0 end-0 bg-warning text-dark px-2 py-1 m-2 rounded-pill small fw-bold shadow-sm">
                                    <i class="bi bi-star-fill text-danger me-1"></i>${b.avgRating > 0 ? String.format("%.1f", b.avgRating) : 'Mới'}
                                </span>
                            </div>

                            <!-- Thông tin sách theo đúng mẫu đề bài -->
                            <div class="card-body d-flex flex-column p-3">
                                <!-- Tiêu đề: (gắn link dẫn sang trang Chi tiết sách ở Câu 4) -->
                                <h5 class="card-title fw-bold mb-2">
                                    <a href="${pageContext.request.contextPath}/book/detail?id=${b.bookid}"
                                       class="text-decoration-none text-primary">
                                        Tiêu đề: <c:out value="${b.title}"/>
                                    </a>
                                </h5>

                                <p class="card-text text-secondary small mb-1">
                                    <strong>Mã isbn:</strong> ${b.isbn != null ? b.isbn : 'Đang cập nhật'}
                                </p>
                                <p class="card-text text-secondary small mb-1">
                                    <strong>Tác giả:</strong> <span class="text-dark fw-semibold"><c:out value="${not empty b.authorNames ? b.authorNames : (currentAuthor != null ? currentAuthor.authorName : 'Đang cập nhật')}"/></span>
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
                                            Xem chi tiết <i class="bi bi-arrow-right"></i>
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
                    <i class="bi bi-inbox fs-1 text-muted"></i>
                    <p class="text-muted mt-2">Hiện chưa có cuốn sách nào trong mục này.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Thanh phân trang chuẩn format: Trang trước – 1 2 3 4 – Trang sau -->
    <c:if test="${totalPages >= 1}">
        <div class="d-flex justify-content-center align-items-center mt-5">
            <nav aria-label="Page navigation">
                <ul class="pagination pagination-md shadow-sm mb-0">
                    <!-- Trang trước -->
                    <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/home?authorId=${authorId}&page=${currentPage - 1}">
                            « Trang trước
                        </a>
                    </li>

                    <!-- Các trang số -->
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/home?authorId=${authorId}&page=${i}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>

                    <!-- Trang sau -->
                    <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/home?authorId=${authorId}&page=${currentPage + 1}">
                            Trang sau »
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
        <div class="text-center text-muted small mt-2">
            Hiển thị trang <strong>${currentPage}</strong> / <strong>${totalPages}</strong> (${pageSize} sản phẩm / trang)
        </div>
    </c:if>

</div>
</body>
</html>
