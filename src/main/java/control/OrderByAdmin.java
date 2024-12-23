package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import entity.Order;
import entity.OrderItem;

/**
 * Servlet implementation class OrderByAdmin
 */
@WebServlet(urlPatterns = {"/orderListAdmin"})
public class OrderByAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderByAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Lấy danh sách đơn hàng từ DAO
            List<Order> listOrder = DAO.getInstance().getAllOrders();
            
            // Truyền danh sách sang JSP
            request.setAttribute("orderListAdmin", listOrder);
            
            // Nếu danh sách rỗng, truyền thêm thông báo
            if (listOrder == null || listOrder.isEmpty()) {
                request.setAttribute("message", "Hiện tại không có đơn hàng nào.");
            }
            
        } catch (Exception e) {
            // Ghi log lỗi để kiểm tra khi cần
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải danh sách đơn hàng: " + e.getMessage());
        }

        // Chuyển tiếp tới Admin.jsp
        request.getRequestDispatcher("Admin.jsp").forward(request, response);
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
