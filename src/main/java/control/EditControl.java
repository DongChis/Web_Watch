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
 * Servlet implementation class EditControl
 */
@WebServlet(urlPatterns = { "/edit" })
public class EditControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditControl() {
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

		String pid = request.getParameter("pid");

		String title = request.getParameter("title"); // Updated to title
		String productName = request.getParameter("name");
		String price = request.getParameter("price");
		String productDescription = request.getParameter("description");
		String imageUrl = request.getParameter("imageURL");

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("accSession");

		if (user != null) {
			if (pid != null) {
				DAO.getInstance().editProduct(title, productName, productDescription, price, imageUrl, pid);
				request.getRequestDispatcher("productListAdmin").forward(request, response);
			}

		} else {
			response.sendRedirect("login");
		}

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
