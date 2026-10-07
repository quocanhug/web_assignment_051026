package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User_24133003;
import vn.iotstar.service.IUserService_24133003;
import vn.iotstar.service.impl.UserServiceImpl_24133003;

@WebServlet(name = "LoginController_24133003", urlPatterns = {"/login"})
public class LoginController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IUserService_24133003 userService = new UserServiceImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/web/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String passwd = req.getParameter("passwd");

        if (email == null || email.trim().isEmpty() || passwd == null || passwd.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ Email và Mật khẩu!");
            req.getRequestDispatcher("/WEB-INF/views/web/login.jsp").forward(req, resp);
            return;
        }

        User_24133003 user = userService.login(email.trim(), passwd);
        if (user != null) {
            HttpSession session = req.getSession(true);
            req.changeSessionId();
            session.removeAttribute("error");
            session.setAttribute("user", user);
            session.setAttribute("success", "Đăng nhập thành công! Xin chào " + (user.getFullname() != null ? user.getFullname() : user.getEmail()));

            if (Boolean.TRUE.equals(session.getAttribute("returnToOrders"))) {
                session.removeAttribute("returnToOrders");
                resp.sendRedirect(req.getContextPath() + "/orders");
            } else if (Boolean.TRUE.equals(session.getAttribute("returnToCheckout"))) {
                session.removeAttribute("returnToCheckout");
                resp.sendRedirect(req.getContextPath() + "/cart/checkout");
            } else if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/books");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            req.setAttribute("error", "Email hoặc mật khẩu không chính xác!");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/web/login.jsp").forward(req, resp);
        }
    }
}
