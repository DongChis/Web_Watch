package control;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.AuditLog;
import entity.Order;

/**
 * Servlet implementation class HisOrderControl
 */
@WebServlet("/hisOrder")
public class HisOrderControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try {
            // Lấy danh sách đơn hàng
            List<Order> listHisOrder = DAO.getInstance().getHisOrders(userId);

            // Lấy trạng thái chỉnh sửa
            Map<Integer, Boolean> editStatusMap = DAO.getInstance().getOrderEditStatus();

            // Lấy danh sách log
            List<AuditLog> auditLogs = DAO.getInstance().getAuditLogsForUserOrders(userId);

            // Truyền dữ liệu tới JSP
            request.setAttribute("hisOrder", listHisOrder);
            request.setAttribute("editStatusMap", editStatusMap);
            request.setAttribute("auditLogs", auditLogs);

            // Chuyển tiếp đến JSP
            request.getRequestDispatcher("HisOrder.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving order history.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
