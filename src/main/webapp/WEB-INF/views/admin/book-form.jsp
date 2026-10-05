<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<html>
<head>
    <title>${action eq 'add' ? 'Thêm Sách Mới' : 'Cập Nhật Sách'}</title>
</head>
<body>
<div class="admin-card" style="max-width: 800px; margin: 0 auto;">

    <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <h4 class="fw-bold text-dark mb-0">
            <i class="bi ${action eq 'add' ? 'bi-plus-circle text-success' : 'bi-pencil-square text-primary'} me-2"></i>
            ${action eq 'add' ? 'Thêm Sách Mới' : 'Cập Nhật Thông Tin Sách'}
        </h4>
        <a href="${pageContext.request.contextPath}/admin/books" class="btn btn-outline-secondary btn-sm rounded-pill">
            <i class="bi bi-arrow-left me-1"></i>Quay lại danh sách
        </a>
    </div>

    <form action="${pageContext.request.contextPath}/admin/book/${action}" method="POST"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <c:if test="${action eq 'edit'}">
            <input type="hidden" name="bookid" value="${book.bookid}"/>
        </c:if>

        <div class="row g-3">
            <div class="col-md-8">
                <label for="title" class="form-label fw-semibold">Tiêu đề sách <span class="text-danger">*</span></label>
                <input type="text" name="title" id="title" class="form-control"
                       value="<c:out value="${book.title}"/>" placeholder="Nhập tiêu đề sách..." required>
            </div>

            <div class="col-md-4">
                <label for="isbn" class="form-label fw-semibold">Mã ISBN</label>
                <input type="number" name="isbn" id="isbn" class="form-control"
                       value="${book.isbn}" placeholder="Ví dụ: 1001">
            </div>

            <div class="col-md-6">
                <label for="publisher" class="form-label fw-semibold">Nhà xuất bản</label>
                <input type="text" name="publisher" id="publisher" class="form-control"
                       value="<c:out value="${book.publisher}"/>" placeholder="Ví dụ: NXB Trẻ, Bloomsbury...">
            </div>

            <div class="col-md-6">
                <label for="publish_date" class="form-label fw-semibold">Ngày xuất bản</label>
                <input type="date" name="publish_date" id="publish_date" class="form-control"
                       value="${book.publishDate}">
            </div>

            <div class="col-md-6">
                <label for="price" class="form-label fw-semibold">Giá bán (VNĐ)</label>
                <input type="number" step="0.01" name="price" id="price" class="form-control"
                       value="${book.price}" placeholder="100.00">
            </div>

            <div class="col-md-6">
                <label for="quantity" class="form-label fw-semibold">Số lượng trong kho</label>
                <input type="number" name="quantity" id="quantity" class="form-control"
                       value="${book.quantity}" placeholder="50">
            </div>

            <div class="col-12">
                <label for="authorNames" class="form-label fw-semibold">Tác giả *</label>
                <input type="text" name="authorNames" id="authorNames" class="form-control"
                       list="authorSuggestions"
                       value="<c:out value="${not empty book ? book.authorNames : ''}"/>"
                       placeholder="Nhập tên tác giả (ví dụ: Dale Carnegie)..." required>
                <datalist id="authorSuggestions">
                    <c:forEach items="${authors}" var="a">
                        <option value="<c:out value="${a.authorName}"/>">
                    </c:forEach>
                </datalist>
                <div class="form-text small">Admin có thể tự do nhập tên tác giả (có gợi ý tự động). Nếu có nhiều tác giả, hãy phân cách bằng dấu phẩy (,).</div>
            </div>

            <div class="col-12">
                <label for="cover_image" class="form-label fw-semibold">Đường dẫn ảnh bìa (Cover Image URL)</label>
                <input type="url" name="cover_image" id="cover_image" class="form-control"
                       value="<c:out value="${book.coverImage}"/>" placeholder="https://images.unsplash.com/...">
                <div class="form-text small">Có thể dán link ảnh từ Unsplash hoặc link ảnh trực tuyến.</div>
            </div>

            <div class="col-12">
                <label for="description" class="form-label fw-semibold">Mô tả tóm tắt sách</label>
                <textarea name="description" id="description" rows="3" class="form-control"
                          placeholder="Nhập giới thiệu nội dung cuốn sách..."><c:out value="${book.description}"/></textarea>
            </div>
        </div>

        <div class="mt-4 pt-3 border-top text-end">
            <a href="${pageContext.request.contextPath}/admin/books" class="btn btn-secondary me-2">Hủy bỏ</a>
            <button type="submit" class="btn ${action eq 'add' ? 'btn-success' : 'btn-primary'} fw-bold px-4">
                <i class="bi bi-save me-1"></i>${action eq 'add' ? 'Lưu Sách Mới' : 'Cập Nhật Thay Đổi'}
            </button>
        </div>
    </form>

</div>
</body>
</html>
