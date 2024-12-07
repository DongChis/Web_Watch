package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		String customerName = request.getParameter("customer-name");
		String customerEmail = request.getParameter("customer-email");
		String customerPhone = request.getParameter("customer-phone");
		String customerAddress = request.getParameter("customer-address");
		String paymentMethod = request.getParameter("payment-method");

		String optionPay1 = "credit-card";
		String optionPay2 = "bank-transfer";
		String optionPay3 = "cash-on-delivery";

		if (paymentMethod != null && !paymentMethod.isEmpty()) {
			// Use the payment method for your logic
			
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
				paymentMethod);
		

		// Redirect or forward to a confirmation page
		response.sendRedirect("authenUserControl");

	}

}
