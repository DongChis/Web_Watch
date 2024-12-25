package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.AuditLog;
import entity.Order;
import entity.User;


/**
 * Servlet implementation class OrderDetailControl
 */
@WebServlet(urlPatterns = {"/orderDetail"})
public class OrderDetailControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderDetailControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setAttribute("orderDetail", true);
		int orderID = Integer.parseInt(request.getParameter("orderID"));
		 
		HttpSession session = request.getSession(false); // Use false to not create a new session
			if (session == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
				return;
			}

			Integer userIdObj = (Integer) session.getAttribute("userId");
			if (userIdObj == null) {
				// userID attribute is missing from session
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
				return;
			}
		 
	
			User user = DAO.getInstance().getUserByID(userIdObj);
		
	
		 List<AuditLog> auditLogs = DAO.getInstance().getAuditLogsForOrder(orderID);
		try {
			Order od = DAO.getInstance().getOrderDetailByOrderID(orderID);
			request.setAttribute("order", od);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		request.setAttribute("auditLogs", auditLogs);
		
	//	request.setAttribute("orderDetail", od);
	//	request.setAttribute("totalPrice", totalPrice);
		
		 if(!user.getRole().equals("Admin")) {
	        	request.getRequestDispatcher("OrderDetail.jsp").forward(request, response);
	        }
	
		request.getRequestDispatcher("orderListAdmin").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}