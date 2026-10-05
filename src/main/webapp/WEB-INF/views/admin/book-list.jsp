<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<html>
<head>
    <title>Quản Lý Sách (CRUD Books)</title>
</head>
<body>
<div class="admin-card">

    <!-- Header phần quản lý -->
    <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <h4 class="fw-bold text-dark mb-1">
                <i class="bi bi-journal-bookmark-fill me-2 text-primary"></i>Quản Lý Sách (CRUD Books)
            </h4>
            <p class="text-muted small mb-0">Xem, tạo mới, chỉnh sửa và xóa sách có phân trang</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/book/add" class="btn btn-success shadow-sm fw-bold rounded-pill px-4">
            <i class="bi bi-plus-circle me-1"></i>Thêm Sách Mới
        </a>
    </div>

    <!-- Bảng danh sách sách -->
    <div class="table-responsive">
        <table class="table table-hover align-middle table-bordered shadow-sm">
            <thead class="table-dark text-center">
                <tr>
                    <th style="width: 60px;">ID</th>
                    <th style="width: 80px;">Bìa</th>
                    <th>Tiêu Đề Sách</th>
                    <th style="width: 100px;">ISBN</th>
                    <th>Tác Giả</th>
                    <th>Nhà Xuất Bản</th>
                    <th style="width: 110px;">Ngày XB</th>
                    <th style="width: 100px;">Giá</th>
                    <th style="width: 80px;">Kho</th>
                    <th style="width: 80px;">Review</th>
                    <th style="width: 140px;">Thao Tác</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty bookList}">
                        <c:forEach items="${bookList}" var="b">
                            <tr>
                                <td class="text-center fw-bold">${b.bookid}</td>
                                <td class="text-center">
                                    <img src="<c:out value="${b.coverImage}"/>" alt="<c:out value="${b.title}"/>" class="rounded shadow-sm"
                                         style="width: 50px; height: 65px; object-fit: cover;"
                                         onerror="this.src='https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500';">
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/book/detail?id=${b.bookid}" target="_blank"
                                       class="fw-bold text-primary text-decoration-none">
                                        <c:out value="${b.title}"/>
                                    </a>
                                </td>
                                <td class="text-center">${b.isbn != null ? b.isbn : '-'}</td>
                                <td>
                                    <span class="badge bg-info text-dark"><c:out value="${b.authorNames != null ? b.authorNames : 'Chưa gắn'}"/></span>
                                </td>
                                <td><c:out value="${b.publisher != null ? b.publisher : '-'}"/></td>
                                <td class="text-center small">${b.publishDate != null ? b.publishDate : '-'}</td>
                                <td class="text-end fw-bold text-danger">${b.price} đ</td>
                                <td class="text-center">
                                    <span class="badge ${b.quantity > 0 ? 'bg-success' : 'bg-danger'}">${b.quantity}</span>
                                </td>
                                <td class="text-center">
                                    <span class="badge bg-warning text-dark"><i class="bi bi-chat-text"></i> ${b.reviewCount}</span>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/admin/book/edit?id=${b.bookid}"
                                       class="btn btn-outline-primary btn-sm me-1" title="Sửa thông tin">
                                        <i class="bi bi-pencil-square"></i> Sửa
                                    </a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/book/delete" class="d-inline" onsubmit="return confirm('Xóa cuốn sách này?')"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="id" value="${b.bookid}"><button type="submit" class="btn btn-outline-danger btn-sm"><i class="bi bi-trash"></i> Xóa</button></form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="11" class="text-center py-4 text-muted">
                                Hiện chưa có dữ liệu sách trong hệ thống.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <!-- Phân trang danh sách Admin -->
    <c:if test="${totalPages > 1}">
        <div class="d-flex justify-content-between align-items-center mt-3">
            <span class="small text-muted">Tổng cộng: <strong>${totalBooks}</strong> cuốn sách</span>
            <nav aria-label="Admin Page navigation">
                <ul class="pagination pagination-sm mb-0">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${currentPage - 1}">« Trước</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${currentPage + 1}">Sau »</a>
                    </li>
                </ul>
            </nav>
        </div>
    </c:if>

</div>
</body>
</html>
