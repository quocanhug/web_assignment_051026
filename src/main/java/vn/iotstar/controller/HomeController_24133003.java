package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.Author_24133003;
import vn.iotstar.model.Book_24133003;
import vn.iotstar.service.IAuthorService_24133003;
import vn.iotstar.service.IBookService_24133003;
import vn.iotstar.service.impl.AuthorServiceImpl_24133003;
import vn.iotstar.service.impl.BookServiceImpl_24133003;

@WebServlet(name = "HomeController_24133003", urlPatterns = {"/home", ""})
public class HomeController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IAuthorService_24133003 authorService = new AuthorServiceImpl_24133003();
    private IBookService_24133003 bookService = new BookServiceImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Author_24133003> authors = authorService.findAll();
        req.setAttribute("authors", authors);

        // Mặc định khi vào Trang Chủ (không truyền param authorId), chọn ngay tác giả đầu tiên để hiển thị đúng mẫu Câu 3:
        // "Tác giả :Author_name" và phân trang đúng 03sp/trang
        int authorId = -1;
        String authorIdParam = req.getParameter("authorId");
        if (authorIdParam != null && !authorIdParam.trim().isEmpty()) {
            try {
                authorId = Integer.parseInt(authorIdParam.trim());
            } catch (Exception e) {
                authorId = -1;
            }
        }
        if (authorId == -1) {
            authorId = (!authors.isEmpty()) ? authors.get(0).getAuthorId() : 0;
        }

        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam.trim());
                if (page < 1) page = 1;
            } catch (Exception e) {
                page = 1;
            }
        }

        List<Book_24133003> bookList;
        int totalBooks;
        int pageSize;
        Author_24133003 currentAuthor = null;

        if (authorId > 0) {
            // Khi chọn theo tác giả: Phân trang đúng 03sp / trang theo Câu 3 của đề bài
            pageSize = 3;
            bookList = bookService.findByAuthor(authorId, page, pageSize);
            totalBooks = bookService.countByAuthor(authorId);
            for (Author_24133003 a : authors) {
                if (a.getAuthorId() == authorId) {
                    currentAuthor = a;
                    break;
                }
            }
        } else {
            // Mặc định vào Trang Chủ: Hiển thị 6 sản phẩm nổi bật / mới nhất
            pageSize = 6;
            bookList = bookService.findAll(page, pageSize);
            totalBooks = bookService.countAll();
        }

        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        req.setAttribute("currentAuthor", currentAuthor);
        req.setAttribute("authorId", authorId);
        req.setAttribute("bookList", bookList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalBooks", totalBooks);
        req.setAttribute("pageSize", pageSize);

        req.getRequestDispatcher("/WEB-INF/views/web/home.jsp").forward(req, resp);
    }
}
