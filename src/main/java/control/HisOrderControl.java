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
import entity.Order;

/**
 * Servlet implementation class HisOrderControl
 */
@WebServlet(urlPatterns = { "/hisOrder" })
public class HisOrderControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HisOrderControl() {
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

		int userId = userIdObj; // Get user ID from session
		
		

		List<Order> listHisOrder = DAO.getInstance().getHisOrders(userId);

		request.setAttribute("hisOrder", listHisOrder);

		request.getRequestDispatcher("HisOrder.jsp").forward(request, response);
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
