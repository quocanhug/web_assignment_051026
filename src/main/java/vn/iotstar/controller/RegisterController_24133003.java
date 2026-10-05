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

@WebServlet(name = "RegisterController_24133003", urlPatterns = {"/register"})
public class RegisterController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IUserService_24133003 userService = new UserServiceImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/web/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phoneStr = req.getParameter("phone");
        String passwd = req.getParameter("passwd");
        String confirmPasswd = req.getParameter("confirm_passwd");

        if (email == null || email.trim().isEmpty() || passwd == null || passwd.trim().isEmpty()) {
            req.setAttribute("error", "Email và mật khẩu không được để trống!");
            req.getRequestDispatcher("/WEB-INF/views/web/register.jsp").forward(req, resp);
            return;
        }

        if (!passwd.equals(confirmPasswd)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher("/WEB-INF/views/web/register.jsp").forward(req, resp);
            return;
        }

        if (userService.checkExistEmail(email.trim())) {
            req.setAttribute("error", "Email này đã được sử dụng. Vui lòng chọn email khác!");
            req.getRequestDispatcher("/WEB-INF/views/web/register.jsp").forward(req, resp);
            return;
        }

        Integer phone = null;
        if (phoneStr != null && !phoneStr.trim().isEmpty()) {
            try {
                phone = Integer.parseInt(phoneStr.trim());
            } catch (Exception ignored) {
            }
        }

        User_24133003 pendingUser = new User_24133003();
        pendingUser.setEmail(email.trim());
        pendingUser.setFullname(fullname != null ? fullname.trim() : "");
        pendingUser.setPhone(phone);
        pendingUser.setPasswd(passwd);
        pendingUser.setIsAdmin(false);

        String otp = OtpUtil_24133003.generateOtp(6);
        HttpSession session = req.getSession(true);
        session.setAttribute("otp", otp);
        session.setAttribute("otp_time", System.currentTimeMillis());
        session.setAttribute("pending_user", pendingUser);

        String subject = "Xác nhận mã OTP đăng ký tài khoản - Web BookStore (MSSV: 24133003)";
        String body = "Xin chào " + pendingUser.getFullname() + ",\n\n"
                    + "Mã OTP kích hoạt tài khoản của bạn là: " + otp + "\n"
                    + "Mã có hiệu lực trong vòng 5 phút. Vui lòng không chia sẻ mã này cho bất kỳ ai.\n\n"
                    + "Trân trọng,\nĐội ngũ BookStore";

        boolean sent = EmailUtil_24133003.sendEmail(pendingUser.getEmail(), subject, body);
        if (!sent) {
            session.setAttribute("warning", "Không thể gửi email OTP. Vui lòng thử gửi lại hoặc liên hệ quản trị viên.");
        }

        resp.sendRedirect(req.getContextPath() + "/verify-otp");
    }
}
