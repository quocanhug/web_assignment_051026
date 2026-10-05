<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/> - Trang Quản Trị</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f1f3f5;
        }
        .admin-card {
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            background: #fff;
            padding: 24px;
        }
    </style>
    <sitemesh:write property="head"/>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header Admin -->
    <%@include file="/commons/admin/header.jsp" %>

    <!-- Thông báo chung từ Session -->
    <div class="container-fluid px-4 mt-3">
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.success}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.error}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>
    </div>

    <!-- Nội dung chính của Admin View -->
    <main class="flex-grow-1 py-4">
        <div class="container-fluid px-4">
            <sitemesh:write property="body"/>
        </div>
    </main>

    <!-- Footer Admin -->
    <%@include file="/commons/admin/footer.jsp" %>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
