<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<nav class="navbar navbar-expand-lg navbar-dark shadow-sm sticky-top" style="background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);">
    <div class="container">
        <!-- Logo / Brand -->
        <a class="navbar-brand fw-bold fs-4 d-flex align-items-center" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-book-half me-2 text-warning"></i>BookStore
        </a>

        <!-- Nút toggle mobile -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarWeb"
                aria-controls="navbarWeb" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Menu chính theo yêu cầu đề bài: Trang Chủ, Sản phẩm, Trang quản trị (nếu là admin) -->
        <div class="collapse navbar-collapse" id="navbarWeb">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link text-white fw-semibold px-3 active" href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-house-door-fill me-1"></i>Trang Chủ
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white fw-semibold px-3" href="${pageContext.request.contextPath}/books">
                        <i class="bi bi-grid-3x3-gap-fill me-1"></i>Sản phẩm
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white fw-semibold px-3 position-relative" href="${pageContext.request.contextPath}/cart">
                        <i class="bi bi-cart3 me-1 text-warning"></i>Giỏ hàng
                        <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                            <span class="badge bg-danger rounded-pill ms-1">${sessionScope.cart.totalQuantity}</span>
                        </c:if>
                    </a>
                </li>
                <!-- Chỉ hiển thị menu Trang quản trị khi người dùng là Admin -->
                <c:if test="${not empty sessionScope.user and sessionScope.user.isAdmin()}">
                    <li class="nav-item">
                        <a class="nav-link text-warning fw-bold px-3" href="${pageContext.request.contextPath}/admin/books">
                            <i class="bi bi-shield-lock-fill me-1"></i>Trang quản trị
                        </a>
                    </li>
                </c:if>
            </ul>

            <!-- Khu vực tài khoản: Đăng nhập / Đăng ký hoặc Hồ sơ / Đăng xuất -->
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item dropdown">
                            <a class="btn btn-outline-light dropdown-toggle rounded-pill px-3 py-1 d-flex align-items-center"
                               href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="bi bi-person-circle me-2 fs-5"></i>
                                <span><c:out value="${sessionScope.user.fullname != null ? sessionScope.user.fullname : sessionScope.user.email}"/></span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow">
                                <li class="dropdown-header text-muted">
                                    Vai trò: <strong>${sessionScope.user.isAdmin() ? 'Quản trị viên (Admin)' : 'Người dùng (User)'}</strong>
                                </li>
                                <c:if test="${sessionScope.user.isAdmin()}">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/books"><i class="bi bi-speedometer2 me-2 text-primary"></i>Quản lý Sách</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li>
                                    <form method="post" action="${pageContext.request.contextPath}/logout"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><button class="dropdown-item text-danger" type="submit">
                                        <i class="bi bi-box-arrow-right me-2"></i>Đăng xuất
                                    </button></form>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item me-2">
                            <a class="btn btn-outline-light rounded-pill px-3 py-1" href="${pageContext.request.contextPath}/login">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-warning text-dark fw-bold rounded-pill px-3 py-1 shadow-sm" href="${pageContext.request.contextPath}/register">
                                <i class="bi bi-person-plus-fill me-1"></i>Đăng ký
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
