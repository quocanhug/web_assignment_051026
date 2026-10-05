<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác Thực OTP - Kích Hoạt Tài Khoản</title>
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
            max-width: 450px;
            width: 100%;
            background: #fff;
            padding: 35px;
        }
        .otp-input {
            letter-spacing: 12px;
            font-size: 28px;
            text-align: center;
            font-weight: bold;
            color: #0d6efd;
        }
    </style>
</head>
<body>
<div class="auth-card text-center">
    <div class="mb-4">
        <div class="display-5 text-primary mb-2"><i class="bi bi-shield-lock"></i></div>
        <h4 class="fw-bold text-dark">Nhập Mã Xác Thực OTP</h4>
        <p class="text-muted small">
            Mã OTP gồm 6 chữ số đã được gửi đến email:<br>
            <strong class="text-primary"><c:out value="${email}"/></strong>
        </p>
    </div>

    <!-- Thông báo nếu có cảnh báo / lỗi -->
    <c:if test="${not empty sessionScope.warning}">
        <div class="alert alert-warning small text-start">
            <i class="bi bi-exclamation-triangle me-1"></i><c:out value="${sessionScope.warning}"/>
        </div>
        <c:remove var="warning" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success small text-start">
            <i class="bi bi-check-circle me-1"></i><c:out value="${sessionScope.success}"/>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger small text-start">
            <i class="bi bi-x-circle me-1"></i>${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-otp" method="POST"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <div class="mb-4">
            <label for="otp" class="form-label fw-semibold text-secondary">Mã OTP (6 chữ số):</label>
            <input type="text" name="otp" id="otp" maxlength="6" class="form-control otp-input py-2"
                   placeholder="123456" autofocus required>
            <div class="form-text small mt-2">Mã có hiệu lực trong 5 phút</div>
        </div>

        <button type="submit" class="btn btn-primary w-100 fw-bold py-2 shadow-sm mb-3">
            <i class="bi bi-check2-circle me-1"></i>Kích Hoạt Tài Khoản
        </button>
    </form>

    <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top small">
        <form method="post" action="${pageContext.request.contextPath}/resend-otp" class="d-inline"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><button type="submit" class="text-decoration-none text-primary fw-semibold">
            <i class="bi bi-arrow-clockwise me-1"></i>Gửi lại mã OTP
        </button></form>
        <a href="${pageContext.request.contextPath}/register" class="text-decoration-none text-secondary">
            Đổi email khác
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
