package control;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.CartItem;
import entity.Order;
import entity.User;

/**
 * Servlet implementation class UpdateOrderByUser
 */
@WebServlet(urlPatterns = { "/updateOrderByUser" })
public class UpdateOrderByUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateOrderByUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");

		

		 int orderID = Integer.parseInt(request.getParameter("orderID"));
		 
		String customerName = request.getParameter("customer-name");
		String customerEmail = request.getParameter("customer-email");
		String customerPhone = request.getParameter("customer-phone");
		String customerAddress = request.getParameter("customer-address");
		String paymentMethod = request.getParameter("payment-method");
		String sign = request.getParameter("sign");

		String optionPay1 = "credit-card";
		String optionPay2 = "bank-transfer";
		String optionPay3 = "cash-on-delivery";
		
		

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

		int userId = userIdObj;

		if (paymentMethod != null && !paymentMethod.isEmpty()) {

			if (paymentMethod.equals(optionPay1)) {
				paymentMethod = "The Tin Dung";
			}
			if (paymentMethod.equals(optionPay2)) {
				paymentMethod = "Chuyen khoan ngan hang";
			}
			if (paymentMethod.equals(optionPay3)) {
				paymentMethod = "Tien mat khi nhan hang";
			}

			System.out.println("Selected Payment Method: " + paymentMethod);
		} else {
			System.out.println("No Payment Method selected." + paymentMethod);
			paymentMethod = "Unknown";
		}

		// Assume you have a method to get the cart items from the session
		List<CartItem> cartItems = (List<CartItem>) request.getSession().getAttribute("cart");

		User user = DAO.getInstance().getUserByID(userId);

		try {
			Order order = new Order(orderID, cartItems, customerName, customerEmail, customerPhone, 
					customerAddress, paymentMethod,new Timestamp(System.currentTimeMillis()), sign,true);
			
			boolean isUpdated = DAO.getInstance().updateOrder(order, orderID);

			if (!isUpdated) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Order update failed.");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"An error occurred while processing the order.");
			return;
		}

		// Redirect or forward to a confirmation page

		request.getRequestDispatcher("hisOrder").forward(request, response);;
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
