<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<footer class="bg-dark text-white pt-4 pb-3 mt-auto shadow-lg border-top border-secondary">
    <div class="container">
        <div class="row align-items-center g-3">
            <!-- Thông tin sinh viên & đề thi theo yêu cầu của Đề bài -->
            <div class="col-md-6 text-center text-md-start">
                <h6 class="text-warning fw-bold text-uppercase mb-2">
                    <i class="bi bi-info-circle-fill me-1"></i>Thông Tin Sinh Viên
                </h6>
                <div class="small text-light">
                    <p class="mb-1"><strong>Họ tên:</strong> <span class="badge bg-primary fs-6">Đinh Quốc Anh</span></p>
                    <p class="mb-1"><strong>MSSV:</strong> <span class="badge bg-info text-dark fs-6">24133003</span></p>
                    <p class="mb-0"><strong>Mã đề:</strong> <span class="badge bg-warning text-dark fs-6">Đề số 02</span></p>
                </div>
            </div>

            <!-- Liên kết nhanh -->
            <div class="col-md-6 text-center text-md-end">
                <h6 class="text-light fw-bold text-uppercase mb-2">Điều hướng nhanh</h6>
                <div class="small">
                    <a href="${pageContext.request.contextPath}/home" class="text-decoration-none text-light me-3">
                        <i class="bi bi-house me-1"></i>Trang Chủ
                    </a>
                    <a href="${pageContext.request.contextPath}/books" class="text-decoration-none text-light me-3">
                        <i class="bi bi-book me-1"></i>Sản phẩm
                    </a>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <form method="post" action="${pageContext.request.contextPath}/logout" class="d-inline"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><button type="submit" class="text-decoration-none text-danger">
                                <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
                            </button></form>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none text-warning">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="mt-2 text-secondary small">
                    © 2026 BookStore Web. Bản quyền thuộc về Bộ môn Công nghệ Phần mềm - Khoa CNTT.
                </div>
            </div>
        </div>
    </div>
</footer>
