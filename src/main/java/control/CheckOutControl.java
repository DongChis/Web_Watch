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
import entity.CartItem;

/**
 * Servlet implementation class CheckOutControl
 */
@WebServlet(urlPatterns = { "/checkout" })
public class CheckOutControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckOutControl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

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
		
		// Call insertOrder method
		DAO.getInstance().insertOrder(cartItems, customerName, customerEmail, customerPhone, customerAddress,
				paymentMethod,sign,userId);
		

		// Redirect or forward to a confirmation page

		response.sendRedirect("OrderConfirm.jsp");


	}

}
