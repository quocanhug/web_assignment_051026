package vn.iotstar.filter;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter(urlPatterns = {"/*"})
public class CartSecurityFilter_24133003 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        req.setCharacterEncoding("UTF-8");
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        synchronized (session) {
            if (session.getAttribute("csrfToken") == null)
                session.setAttribute("csrfToken", UUID.randomUUID().toString());
        }
        if ("POST".equals(req.getMethod())) {
            String token = req.getParameter("csrfToken");
            if (!session.getAttribute("csrfToken").equals(token)) {
                resp.sendError(403, "Phiên xác nhận không hợp lệ. Vui lòng tải lại trang.");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
