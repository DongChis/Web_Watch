package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.User;

/**
 * Servlet implementation class loadInfoUser
 */
@WebServlet(urlPatterns = { "/loadInfoUser" })
public class loadInfoUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public loadInfoUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		HttpSession session = request.getSession(false); // Use false to not create a new session
		if (session == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
			return;
		}
		String userId = session.getAttribute("userId").toString();
		if (userId == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID not found in session.");
			return;
		}
		User user = DAO.getInstance().getUserByID(userId);
		
		request.setAttribute("username", user.getUsername());
		request.setAttribute("password", user.getPassword());
		request.setAttribute("email", user.getEmail());
		
		request.getRequestDispatcher("Readme.jsp").forward(request, response);
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
