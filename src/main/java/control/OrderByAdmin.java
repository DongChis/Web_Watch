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
            int pageSize = 6; // Số đơn hàng mỗi trang
            String pageParam = request.getParameter("page");
            int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;

            List<Order> listOrder = DAO.getInstance().getOrdersByPage(page, pageSize);
            int totalOrders = DAO.getInstance().getTotalOrders();
            Order o = new Order();
            
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

            request.setAttribute("orderListAdmin", listOrder);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);

            if (listOrder.isEmpty()) {
                request.setAttribute("message", "Hiện tại không có đơn hàng nào.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }

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
