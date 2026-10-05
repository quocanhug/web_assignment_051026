<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<html>
<head>
    <title>Chi Tiết Sách - <c:out value="${book.title}"/></title>
    <style>
        .detail-cover {
            max-height: 420px;
            width: 100%;
            object-fit: cover;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.12);
        }
        .review-item {
            background-color: #f8f9fa;
            border-left: 4px solid #0d6efd;
            border-radius: 6px;
            padding: 12px 16px;
            margin-bottom: 12px;
        }
    </style>
</head>
<body>
<div class="container py-2">

    <!-- Nút quay lại -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary btn-sm rounded-pill">
            <i class="bi bi-arrow-left me-1"></i>Quay lại Trang Chủ
        </a>
    </div>

    <!-- KHỐI THÔNG TIN CHI TIẾT SÁCH (2 CỘT) -->
    <div class="card shadow-sm border-0 mb-4 bg-white">
        <div class="card-body p-4">
            <div class="row g-4 align-items-center">
                <!-- Cột trái: [cover_image] -->
                <div class="col-md-5 text-center">
                    <img src="<c:out value="${book.coverImage}"/>" class="detail-cover img-fluid" alt="<c:out value="${book.title}"/>"
                         onerror="this.src='https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500';">
                </div>

                <!-- Cột phải: Các trường theo đúng mẫu đề bài -->
                <div class="col-md-7">
                    <h3 class="fw-bold text-primary mb-3">Tiêu đề: <c:out value="${book.title}"/></h3>

                    <div class="list-group list-group-flush fs-6">
                        <div class="list-group-item px-0 py-2 border-0">
                            <strong>Mã isbn:</strong> <span class="text-secondary">${book.isbn != null ? book.isbn : 'N/A'}</span>
                        </div>
                        <div class="list-group-item px-0 py-2 border-0">
                            <strong>Tác giả:</strong> <span class="text-dark fw-semibold"><c:out value="${not empty book.authorNames ? book.authorNames : 'Đang cập nhật'}"/></span>
                        </div>
                        <div class="list-group-item px-0 py-2 border-0">
                            <strong>Publisher:</strong> <span class="text-secondary"><c:out value="${book.publisher != null ? book.publisher : 'NXB Tổng hợp'}"/></span>
                        </div>
                        <div class="list-group-item px-0 py-2 border-0">
                            <strong>Publisher_date:</strong> <span class="text-secondary">${book.publishDate != null ? book.publishDate : 'N/A'}</span>
                        </div>
                        <div class="list-group-item px-0 py-2 border-0">
                            <strong>Quantity:</strong>
                            <span class="badge ${book.quantity > 0 ? 'bg-success' : 'bg-danger'} fs-6">${book.quantity}</span>
                        </div>
                        <div class="list-group-item px-0 py-2 border-0">
                            <strong class="text-warning-emphasis">Reviews (${book.reviewCount})</strong>
                        </div>
                        <div class="list-group-item px-0 py-2 border-0">
                            <strong>Giá bán:</strong> <span class="fs-4 fw-bold text-danger">${book.price} VNĐ</span>
                        </div>

                        <!-- KHỐI THÊM VÀO GIỎ HÀNG -->
                        <div class="list-group-item px-0 py-3 border-0">
                            <c:choose>
                                <c:when test="${book.quantity > 0}">
                                    <form action="${pageContext.request.contextPath}/cart/add" method="POST" class="d-flex align-items-center flex-wrap gap-3"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="bookId" value="${book.bookid}"/>

                                        <div class="d-flex align-items-center">
                                            <span class="fw-semibold me-2 text-secondary">Số lượng:</span>
                                            <div class="input-group" style="width: 130px;">
                                                <button class="btn btn-outline-secondary" type="button" onclick="decrementDetailQty()">
                                                    <i class="bi bi-dash"></i>
                                                </button>
                                                <input type="number" name="quantity" id="detailQty" class="form-control text-center fw-bold"
                                                       value="1" min="1" max="${book.quantity}" required>
                                                <button class="btn btn-outline-secondary" type="button" onclick="incrementDetailQty(${book.quantity})">
                                                    <i class="bi bi-plus"></i>
                                                </button>
                                            </div>
                                        </div>

                                        <button type="submit" class="btn btn-warning fw-bold px-4 py-2 rounded-pill shadow-sm text-dark">
                                            <i class="bi bi-cart-plus-fill me-2 fs-5"></i>Thêm Vào Giỏ Hàng
                                        </button>
                                    </form>
                                    <small class="text-muted d-block mt-2">
                                        <i class="bi bi-info-circle me-1"></i>Số lượng tồn kho khả dụng: <strong>${book.quantity}</strong> cuốn
                                    </small>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-danger d-inline-flex align-items-center py-2 px-3 mb-0" role="alert">
                                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                        <div>Sản phẩm hiện đang tạm hết hàng trong kho. Vui lòng quay lại sau!</div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:if test="${not empty book.description}">
                            <div class="list-group-item px-0 py-2 border-0">
                                <strong>Mô tả tóm tắt:</strong>
                                <p class="text-muted mt-1 mb-0"><c:out value="${book.description}"/></p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- KHỐI REVIEWS (DANH SÁCH & FORM) -->
    <div class="row g-4">
        <!-- Cột danh sách Reviews theo mẫu: [users]: [review_text] -->
        <div class="col-lg-7">
            <div class="card shadow-sm border-0 h-100 bg-white">
                <div class="card-header bg-white border-bottom py-3">
                    <h5 class="fw-bold text-dark mb-0 d-flex align-items-center">
                        <i class="bi bi-chat-left-text-fill text-primary me-2"></i>Reviews
                    </h5>
                </div>
                <div class="card-body p-4">
                    <c:choose>
                        <c:when test="${not empty reviews}">
                            <c:forEach items="${reviews}" var="r">
                                <div class="review-item shadow-sm">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <span class="fw-bold text-primary">
                                            <i class="bi bi-person-circle me-1"></i>[<c:out value="${r.userFullName != null && !r.userFullName.isEmpty() ? r.userFullName : r.userEmail}"/>]:
                                        </span>
                                        <span class="badge bg-warning text-dark">
                                            ${r.rating} <i class="bi bi-star-fill text-danger"></i>
                                        </span>
                                    </div>
                                    <div class="text-dark ps-2">
                                        <c:out value="${r.reviewText}"/>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-4 text-muted">
                                <i class="bi bi-chat-square-dots fs-2"></i>
                                <p class="mt-2 mb-0">Chưa có nhận xét nào cho cuốn sách này. Hãy là người đầu tiên đánh giá!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Cột Form thêm reviews -->
        <div class="col-lg-5">
            <div class="card shadow-sm border-0 bg-white">
                <div class="card-header bg-primary text-white py-3">
                    <h5 class="fw-bold mb-0">Form thêm reviews</h5>
                </div>
                <div class="card-body p-4">
                    <form action="${pageContext.request.contextPath}/book/detail" method="POST"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        <input type="hidden" name="bookid" value="${book.bookid}"/>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Người đánh giá:</label>
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <input type="text" class="form-control bg-light"
                                           value="<c:out value="${sessionScope.user.fullname}"/> (<c:out value="${sessionScope.user.email}"/>)" readonly>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" class="form-control bg-light text-muted"
                                           value="Khách (Cần đăng nhập trước khi gửi)" readonly>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="mb-3">
                            <label for="rating" class="form-label fw-semibold">Đánh giá sao:</label>
                            <select name="rating" id="rating" class="form-select">
                                <option value="5" ${userRating != null && userRating.rating == 5 ? 'selected' : ''}>⭐⭐⭐⭐⭐ (5 sao - Tuyệt vời)</option>
                                <option value="4" ${userRating != null && userRating.rating == 4 ? 'selected' : ''}>⭐⭐⭐⭐ (4 sao - Tốt)</option>
                                <option value="3" ${userRating != null && userRating.rating == 3 ? 'selected' : ''}>⭐⭐⭐ (3 sao - Bình thường)</option>
                                <option value="2" ${userRating != null && userRating.rating == 2 ? 'selected' : ''}>⭐⭐ (2 sao - Tạm được)</option>
                                <option value="1" ${userRating != null && userRating.rating == 1 ? 'selected' : ''}>⭐ (1 sao - Kém)</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="review_text" class="form-label fw-semibold">Nội dung review:</label>
                            <textarea name="review_text" id="review_text" rows="4" class="form-control"
                                      placeholder="Nhập cảm nhận của bạn về cuốn sách..." required><c:out value="${userRating != null ? userRating.reviewText : ''}"/></textarea>
                        </div>

                        <c:if test="${empty sessionScope.user}">
                            <div class="alert alert-info py-2 small mb-3">
                                <i class="bi bi-info-circle me-1"></i>Bạn đang xem với tư cách Khách. Nhấn Submit sẽ chuyển sang trang Đăng nhập.
                            </div>
                        </c:if>

                        <button type="submit" class="btn btn-primary w-100 fw-bold py-2 shadow-sm">
                            <i class="bi bi-send-fill me-1"></i>Submit
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

<script>
    function decrementDetailQty() {
        var input = document.getElementById('detailQty');
        var val = parseInt(input.value, 10);
        if (isNaN(val) || val <= 1) {
            alert('Số lượng mua tối thiểu là 1 cuốn! Không được nhập số âm hoặc bằng 0.');
            input.value = 1;
            return;
        }
        input.value = val - 1;
    }

    function incrementDetailQty(maxStock) {
        var input = document.getElementById('detailQty');
        var val = parseInt(input.value, 10);
        if (isNaN(val) || val < 1) {
            input.value = 1;
            return;
        }
        if (val >= maxStock) {
            alert('Số lượng trong kho của cuốn sách này chỉ còn tối đa ' + maxStock + ' cuốn!');
            input.value = maxStock;
            return;
        }
        input.value = val + 1;
    }
</script>
</body>
</html>
