package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.Book_24133003;
import vn.iotstar.service.IBookService_24133003;
import vn.iotstar.service.impl.BookServiceImpl_24133003;

@WebServlet(name = "BookListController_24133003", urlPatterns = {"/books"})
public class BookListController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IBookService_24133003 bookService = new BookServiceImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 12; // Hiển thị đầy đủ tất cả sách trong kho sản phẩm
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

        req.getRequestDispatcher("/WEB-INF/views/web/books.jsp").forward(req, resp);
    }
}
