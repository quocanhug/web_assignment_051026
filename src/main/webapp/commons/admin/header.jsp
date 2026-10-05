<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm sticky-top border-bottom border-warning">
    <div class="container-fluid px-4">
        <a class="navbar-brand fw-bold fs-4 d-flex align-items-center text-warning" href="${pageContext.request.contextPath}/admin/books">
            <i class="bi bi-shield-lock-fill me-2"></i>Admin Dashboard
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAdmin"
                aria-controls="navbarAdmin" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarAdmin">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/books">
                        <i class="bi bi-book me-1 text-info"></i>Quản lý Books (CRUD)
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/home" target="_blank">
                        <i class="bi bi-box-arrow-up-right me-1"></i>Xem trang người dùng
                    </a>
                </li>
            </ul>

            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                <li class="nav-item me-3 text-light">
                    <i class="bi bi-person-badge-fill me-1 text-warning"></i>
                    <span><c:out value="${sessionScope.user.fullname != null ? sessionScope.user.fullname : sessionScope.user.email}"/> (Admin)</span>
                </li>
                <li class="nav-item">
                    <form method="post" action="${pageContext.request.contextPath}/logout"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><button class="btn btn-danger btn-sm rounded-pill px-3" type="submit">
                        <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
                    </button></form>
                </li>
            </ul>
        </div>
    </div>
</nav>
