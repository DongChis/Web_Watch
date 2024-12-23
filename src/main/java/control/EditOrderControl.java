package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.CartItem;
import entity.Order;
import entity.User;
import dao.DAO;

/**
 * Servlet implementation class EditOrderControl
 */
@WebServlet(urlPatterns = { "/editOrder" })
public class EditOrderControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditOrderControl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("orderEdit", true);

        int orderID = Integer.parseInt(request.getParameter("orderID"));
        String customerName = request.getParameter("customerName");
       // List<CartItem> listCart = request.get
        String customerPhone = request.getParameter("customerPhone");
        String customerEmail = request.getParameter("customerEmail");
        String customerAddress = request.getParameter("customerAddress");
        String orderDate = request.getParameter("orderDate");
        String paymentMethod = request.getParameter("paymentMethod");
        
        Order order = null;
        
     // Get the session
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

        try {
            order = DAO.getInstance().getOrderDetailByOrderID(orderID);
            boolean isUpdated = DAO.getInstance().updateOrder(order, orderID);

            if (!isUpdated) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Order update failed.");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the order.");
            return;
        }
        
        if(!user.getRole().equals("Admin")) {
        	request.getRequestDispatcher("EditOrder.jsp").forward(request, response);
        }

        //response.sendRedirect("orderDetail?orderID=" + orderID);
        request.getRequestDispatcher("orderListAdmin").forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
