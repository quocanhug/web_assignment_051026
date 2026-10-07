package vn.iotstar.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.dao.impl.OrderDaoImpl_24133003;
import vn.iotstar.model.*;

@WebServlet(urlPatterns = {"/orders"})
public class OrderHistoryController_24133003 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDaoImpl_24133003 orders = new OrderDaoImpl_24133003();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24133003 user = (User_24133003) session.getAttribute("user");
        resp.setHeader("Cache-Control", "no-store");
        if (user == null) {
            session.removeAttribute("returnToCheckout");
            session.setAttribute("returnToOrders", true);
            session.setAttribute("error", "Vui lòng đăng nhập để xem lịch sử đặt hàng.");
            resp.sendRedirect(req.getContextPath() + "/login"); return;
        }
        OrderStatus_24133003 selected = null;
        int page = 1;
        try {
            String code = req.getParameter("status");
            if (code != null && !code.isBlank()) selected = OrderStatus_24133003.valueOf(code);
            String requestedPage = req.getParameter("page");
            if (requestedPage != null) page = Integer.parseInt(requestedPage);
            if (page < 1) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            resp.sendError(400, "Trạng thái hoặc trang không hợp lệ."); return;
        }
        try {
            var counts = orders.countForUser(user.getId());
            int allCount = counts.values().stream().mapToInt(Integer::intValue).sum();
            int total = selected == null ? allCount : counts.get(selected);
            int totalPages = Math.max(1, (int) Math.ceil(total / 10.0));
            page = Math.min(page, totalPages);
            req.setAttribute("orders", orders.findHistory(user.getId(), selected, page, 10));
            req.setAttribute("statuses", OrderStatus_24133003.values());
            req.setAttribute("statusCounts", counts);
            req.setAttribute("allCount", allCount);
            req.setAttribute("total", total);
            req.setAttribute("selectedStatus", selected == null ? "" : selected.name());
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.getRequestDispatcher("/WEB-INF/views/web/orders.jsp").forward(req, resp);
        } catch (Exception e) {
            log("Không thể tải lịch sử đơn hàng", e);
            resp.sendError(503, "Chưa thể tải lịch sử đặt hàng. Vui lòng thử lại sau.");
        }
    }
}
