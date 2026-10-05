package vn.iotstar.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.Author_24133003;
import vn.iotstar.model.Book_24133003;
import vn.iotstar.service.IAuthorService_24133003;
import vn.iotstar.service.IBookService_24133003;
import vn.iotstar.service.impl.AuthorServiceImpl_24133003;
import vn.iotstar.service.impl.BookServiceImpl_24133003;

@WebServlet(name = "BookAdminController_24133003", urlPatterns = {
    "/admin/books",
    "/admin/book/add",
    "/admin/book/edit",
    "/admin/book/delete"
})
public class BookAdminController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IBookService_24133003 bookService = new BookServiceImpl_24133003();
    private IAuthorService_24133003 authorService = new AuthorServiceImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/admin/books".equals(path)) {
            // Danh sách sách có phân trang
            int page = 1;
            int pageSize = 6;
            String pageParam = req.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam.trim());
                    if (page < 1) page = 1;
                } catch (Exception e) {
                    page = 1;
                }
            }

            List<Book_24133003> bookList = bookService.findAll(page, pageSize);
            int totalBooks = bookService.countAll();
            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
            if (totalPages == 0) totalPages = 1;

            req.setAttribute("bookList", bookList);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalBooks", totalBooks);

            req.getRequestDispatcher("/WEB-INF/views/admin/book-list.jsp").forward(req, resp);
        } else if ("/admin/book/add".equals(path)) {
            // Hiển thị form thêm sách
            List<Author_24133003> authors = authorService.findAll();
            req.setAttribute("authors", authors);
            req.setAttribute("action", "add");
            req.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(req, resp);
        } else if ("/admin/book/edit".equals(path)) {
            // Hiển thị form sửa sách
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/admin/books");
                return;
            }
            int id = Integer.parseInt(idStr.trim());
            Book_24133003 book = bookService.findById(id);
            if (book == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/books");
                return;
            }
            List<Author_24133003> authors = authorService.findAll();
            req.setAttribute("book", book);
            req.setAttribute("authors", authors);
            req.setAttribute("action", "edit");
            req.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(req, resp);
        } else if ("/admin/book/delete".equals(path)) {
            if (!"POST".equals(req.getMethod())) { resp.sendError(405); return; }
            // Xóa sách
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    boolean success = bookService.delete(id);
                    HttpSession session = req.getSession(true);
                    if (success) {
                        session.setAttribute("success", "Đã xóa sách thành công!");
                    } else {
                        session.setAttribute("error", "Xóa sách thất bại!");
                    }
                } catch (Exception e) {
                    req.getSession(true).setAttribute("error", "Lỗi: " + e.getMessage());
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin/books");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(true);

        if ("/admin/book/delete".equals(path)) { doGet(req, resp); return; }

        try {
            String title = req.getParameter("title");
            String isbnStr = req.getParameter("isbn");
            String publisher = req.getParameter("publisher");
            String priceStr = req.getParameter("price");
            String description = req.getParameter("description");
            String publishDateStr = req.getParameter("publish_date");
            String coverImage = req.getParameter("cover_image");
            String quantityStr = req.getParameter("quantity");

            Book_24133003 book = new Book_24133003();
            book.setTitle(title);
            book.setPublisher(publisher);
            book.setDescription(description);
            book.setCoverImage(coverImage != null && !coverImage.trim().isEmpty() ? coverImage.trim() : "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500");

            if (isbnStr != null && !isbnStr.trim().isEmpty()) {
                book.setIsbn(Integer.parseInt(isbnStr.trim()));
            }
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                book.setPrice(new BigDecimal(priceStr.trim()));
            }
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                book.setQuantity(Integer.parseInt(quantityStr.trim()));
            }
            if (publishDateStr != null && !publishDateStr.trim().isEmpty()) {
                book.setPublishDate(Date.valueOf(publishDateStr.trim()));
            }

            String authorNamesStr = req.getParameter("authorNames");
            if (title == null || title.isBlank() || title.length() > 200 || book.getPrice() == null
                    || book.getPrice().signum() < 0 || book.getQuantity() == null || book.getQuantity() < 0)
                throw new IllegalArgumentException("Tên sách, giá và tồn kho không hợp lệ.");
            String[] authorIdStrs = req.getParameterValues("authorIds");

            List<Integer> authorIds = new ArrayList<>();
            if (authorNamesStr != null && !authorNamesStr.trim().isEmpty()) {
                authorIds = authorService.getOrCreateAuthorIds(authorNamesStr);
            } else if (authorIdStrs != null) {
                for (String aId : authorIdStrs) {
                    authorIds.add(Integer.parseInt(aId.trim()));
                }
            }

            if ("/admin/book/add".equals(path)) {
                int newId = bookService.insert(book, authorIds);
                if (newId > 0) {
                    session.setAttribute("success", "Thêm sách mới thành công!");
                } else {
                    session.setAttribute("error", "Thêm sách thất bại!");
                }
            } else if ("/admin/book/edit".equals(path)) {
                String idStr = req.getParameter("bookid");
                int bookId = Integer.parseInt(idStr.trim());
                book.setBookid(bookId);
                boolean updated = bookService.update(book, authorIds);
                if (updated) {
                    session.setAttribute("success", "Cập nhật thông tin sách thành công!");
                } else {
                    session.setAttribute("error", "Cập nhật sách thất bại!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi xử lý dữ liệu: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/books");
    }
}
