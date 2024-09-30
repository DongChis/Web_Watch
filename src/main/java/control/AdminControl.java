package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AdminControl
 */
@WebServlet(urlPatterns = { "/admin" })
public class AdminControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminControl() {
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
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		/*
		 * if(session != null) {
		 * request.getRequestDispatcher("Admin.jsp").forward(request, response);
		 * response.sendRedirect("Admin.jsp"); } else { response.sendRedirect("login");
		 * }
		 */
		
		if(session != null ) {
			request.getRequestDispatcher("Admin.jsp").forward(request, response);
		}
		

		/* request.getRequestDispatcher("Admin.jsp").forward(request, response); */

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	/*
	 * protected void doPost(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException {
	 * 
	 * response.setContentType("text/html;charset=UTF-8");
	 * request.setCharacterEncoding("UTF-8");
	 * 
	 * request.getRequestDispatcher("Admin.jsp").forward(request, response);
	 * 
	 * doPost(request, response); }
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
