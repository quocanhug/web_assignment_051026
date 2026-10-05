<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - BookStore</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Roboto', sans-serif;
        }
        .auth-card {
            border-radius: 16px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.25);
            max-width: 440px;
            width: 100%;
            background: #fff;
            padding: 35px;
        }
    </style>
</head>
<body>
<div class="auth-card">
    <div class="text-center mb-4">
        <a href="${pageContext.request.contextPath}/home" class="text-decoration-none">
            <h3 class="fw-bold text-primary mb-1"><i class="bi bi-book-half me-2 text-warning"></i>BookStore</h3>
        </a>
        <h5 class="fw-bold text-dark">Đăng Nhập Hệ Thống</h5>
        <p class="text-muted small">Vui lòng đăng nhập để tiếp tục</p>
    </div>

    <!-- Thông báo lỗi hoặc thành công -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show small" role="alert">
            <i class="bi bi-check-circle-fill me-1"></i><c:out value="${sessionScope.success}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show small" role="alert">
            <i class="bi bi-exclamation-circle-fill me-1"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.logout eq 'success'}">
        <div class="alert alert-info alert-dismissible fade show small" role="alert">
            <i class="bi bi-info-circle-fill me-1"></i>Bạn đã đăng xuất khỏi hệ thống thành công.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="POST"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <div class="mb-3">
            <label for="email" class="form-label fw-semibold">Email tài khoản</label>
            <div class="input-group">
                <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                <input type="email" name="email" id="email" class="form-control"
                       placeholder="Nhập email của bạn..." value="<c:out value="${email}"/>" required>
            </div>
        </div>

        <div class="mb-4">
            <label for="passwd" class="form-label fw-semibold">Mật khẩu</label>
            <div class="input-group">
                <span class="input-group-text"><i class="bi bi-lock"></i></span>
                <input type="password" name="passwd" id="passwd" class="form-control"
                       placeholder="••••••" required>
            </div>
        </div>

        <button type="submit" class="btn btn-primary w-100 fw-bold py-2 shadow-sm mb-3">
            <i class="bi bi-box-arrow-in-right me-1"></i>Đăng Nhập
        </button>
    </form>

    <div class="text-center pt-2 border-top small text-muted">
        Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register" class="text-primary fw-bold text-decoration-none">Đăng ký mới (OTP)</a>
        <br>
        <a href="${pageContext.request.contextPath}/home" class="text-secondary text-decoration-none mt-2 d-inline-block">
            <i class="bi bi-arrow-left"></i> Về Trang Chủ
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
