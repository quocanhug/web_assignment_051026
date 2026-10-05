package vn.iotstar.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User_24133003;

@WebFilter(filterName = "AdminFilter_24133003", urlPatterns = {"/admin/*"})
public class AdminFilter_24133003 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User_24133003 user = (session != null) ? (User_24133003) session.getAttribute("user") : null;
        if (user == null) {
            session = req.getSession(true);
            session.setAttribute("error", "Vui lòng đăng nhập tài khoản Quản trị viên để truy cập!");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!user.isAdmin()) {
            session.setAttribute("error", "Bạn không có quyền quản trị viên!");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        chain.doFilter(request, response);
    }
}
