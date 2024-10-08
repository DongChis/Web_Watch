package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.Product;
import entity.User;

@WebServlet(urlPatterns = "/loadP")
public class LoadControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadControl() {
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

		request.setAttribute("edit", true);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("accSession");

		if (user != null) {
			String id = request.getParameter("pid");
			Product p = DAO.getInstance().getProductByID(id);
			request.setAttribute("product", p);

			if (id != null) {
				request.getRequestDispatcher("edit").forward(request, response);
			}
		} else {
			response.sendRedirect("login");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
