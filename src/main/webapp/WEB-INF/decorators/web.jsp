<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/> - BookStore</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f8f9fa;
        }
        .book-card {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            border-radius: 10px;
            overflow: hidden;
        }
        .book-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.12) !important;
        }
        .book-cover {
            height: 260px;
            object-fit: cover;
            width: 100%;
        }
        .badge-rating {
            background-color: #ffc107;
            color: #212529;
            font-weight: bold;
        }
    </style>
    <sitemesh:write property="head"/>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header chung -->
    <%@include file="/commons/web/header.jsp" %>

    <!-- Thông báo chung từ Session -->
    <div class="container mt-3">
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show shadow-sm text-center" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.success}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show shadow-sm text-center" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.error}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>
    </div>

    <!-- Nội dung chính của từng view -->
    <main class="flex-grow-1 py-4">
        <sitemesh:write property="body"/>
    </main>

    <!-- Footer chung -->
    <%@include file="/commons/web/footer.jsp" %>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
