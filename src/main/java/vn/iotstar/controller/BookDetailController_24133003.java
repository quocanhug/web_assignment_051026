package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.Book_24133003;
import vn.iotstar.model.Rating_24133003;
import vn.iotstar.model.User_24133003;
import vn.iotstar.service.IBookService_24133003;
import vn.iotstar.service.IRatingService_24133003;
import vn.iotstar.service.impl.BookServiceImpl_24133003;
import vn.iotstar.service.impl.RatingServiceImpl_24133003;

@WebServlet(name = "BookDetailController_24133003", urlPatterns = {"/book/detail"})
public class BookDetailController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IBookService_24133003 bookService = new BookServiceImpl_24133003();
    private IRatingService_24133003 ratingService = new RatingServiceImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            int bookId = Integer.parseInt(idParam.trim());
            Book_24133003 book = bookService.findById(bookId);
            if (book == null) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            List<Rating_24133003> reviews = ratingService.findByBookId(bookId);
            int reviewCount = ratingService.countReviewsByBookId(bookId);
            book.setReviewCount(reviewCount);

            req.setAttribute("book", book);
            req.setAttribute("reviews", reviews);

            HttpSession session = req.getSession(false);
            User_24133003 currentUser = (session != null) ? (User_24133003) session.getAttribute("user") : null;
            if (currentUser != null) {
                Rating_24133003 userRating = ratingService.findByUserAndBook(currentUser.getId(), bookId);
                req.setAttribute("userRating", userRating);
            }

            req.getRequestDispatcher("/WEB-INF/views/web/book-detail.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User_24133003 currentUser = (session != null) ? (User_24133003) session.getAttribute("user") : null;

        String bookIdParam = req.getParameter("bookid");
        if (bookIdParam == null || bookIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        int bookId;
        try { bookId = Integer.parseInt(bookIdParam.trim()); if (bookId <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException e) { resp.sendError(400); return; }

        if (currentUser == null) {
            session = req.getSession(true);
            session.setAttribute("error", "Vui lòng đăng nhập để gửi nhận xét và đánh giá!");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            String ratingParam = req.getParameter("rating");
            String reviewText = req.getParameter("review_text");

            int ratingVal = 5;
            if (ratingParam != null && !ratingParam.trim().isEmpty()) {
                ratingVal = Integer.parseInt(ratingParam.trim());
            }

            if (ratingVal < 1 || ratingVal > 5 || bookService.findById(bookId) == null) {
                session.setAttribute("error", "Sách không tồn tại hoặc điểm đánh giá không nằm trong khoảng 1–5.");
                resp.sendRedirect(req.getContextPath() + "/book/detail?id=" + bookId);
                return;
            }
            Rating_24133003 rating = new Rating_24133003();
            rating.setUserid(currentUser.getId());
            rating.setBookid(bookId);
            rating.setRating(ratingVal);
            rating.setReviewText(reviewText != null ? reviewText.trim() : "");

            if (ratingService.saveOrUpdate(rating))
                session.setAttribute("success", "Cảm ơn bạn đã gửi đánh giá cho cuốn sách này!");
            else session.setAttribute("error", "Chưa lưu được đánh giá. Vui lòng thử lại sau.");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi gửi đánh giá: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/book/detail?id=" + bookId);
    }
}
