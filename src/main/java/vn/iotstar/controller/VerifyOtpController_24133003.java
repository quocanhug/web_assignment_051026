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
import vn.iotstar.util.EmailUtil_24133003;
import vn.iotstar.util.OtpUtil_24133003;

@WebServlet(name = "VerifyOtpController_24133003", urlPatterns = {"/verify-otp", "/resend-otp"})
public class VerifyOtpController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IUserService_24133003 userService = new UserServiceImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        User_24133003 pendingUser = (session != null) ? (User_24133003) session.getAttribute("pending_user") : null;

        if (pendingUser == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if ("/resend-otp".equals(path)) {
            if (!"POST".equals(req.getMethod())) { resp.sendError(405); return; }
            String newOtp = OtpUtil_24133003.generateOtp(6);
            session.setAttribute("otp", newOtp);
            session.setAttribute("otp_time", System.currentTimeMillis());

            String subject = "Gửi lại mã OTP đăng ký tài khoản - Web BookStore (MSSV: 24133003)";
            String body = "Mã OTP mới của bạn là: " + newOtp + "\nMã có hiệu lực trong 5 phút.";
            boolean sent = EmailUtil_24133003.sendEmail(pendingUser.getEmail(), subject, body);
            if (sent) {
                session.setAttribute("success", "Mã OTP mới đã được gửi vào email của bạn!");
            } else {
                session.setAttribute("warning", "Gửi email thất bại. Vui lòng thử lại hoặc liên hệ quản trị viên.");
            }
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
            return;
        }

        req.setAttribute("email", pendingUser.getEmail());
        req.getRequestDispatcher("/WEB-INF/views/web/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/resend-otp".equals(req.getServletPath())) { doGet(req, resp); return; }
        HttpSession session = req.getSession(false);
        User_24133003 pendingUser = (session != null) ? (User_24133003) session.getAttribute("pending_user") : null;
        String sessionOtp = (session != null) ? (String) session.getAttribute("otp") : null;
        Long otpTime = (session != null) ? (Long) session.getAttribute("otp_time") : null;

        if (pendingUser == null || sessionOtp == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String enteredOtp = req.getParameter("otp");
        if (enteredOtp == null || enteredOtp.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập mã OTP!");
            req.setAttribute("email", pendingUser.getEmail());
            req.getRequestDispatcher("/WEB-INF/views/web/verify-otp.jsp").forward(req, resp);
            return;
        }

        if (otpTime != null && (System.currentTimeMillis() - otpTime > 5 * 60 * 1000)) {
            req.setAttribute("error", "Mã OTP đã hết hiệu lực (quá 5 phút). Vui lòng bấm 'Gửi lại mã'!");
            req.setAttribute("email", pendingUser.getEmail());
            req.getRequestDispatcher("/WEB-INF/views/web/verify-otp.jsp").forward(req, resp);
            return;
        }

        if (sessionOtp.equals(enteredOtp.trim())) {
            boolean success = userService.register(pendingUser);
            if (success) {
                session.removeAttribute("otp");
                session.removeAttribute("otp_time");
                session.removeAttribute("pending_user");

                session.setAttribute("success", "Kích hoạt tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.");
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            } else {
                req.setAttribute("error", "Lỗi lưu tài khoản vào cơ sở dữ liệu. Vui lòng thử lại!");
                req.setAttribute("email", pendingUser.getEmail());
                req.getRequestDispatcher("/WEB-INF/views/web/verify-otp.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "Mã OTP không chính xác. Vui lòng kiểm tra lại!");
            req.setAttribute("email", pendingUser.getEmail());
            req.getRequestDispatcher("/WEB-INF/views/web/verify-otp.jsp").forward(req, resp);
        }
    }
}
