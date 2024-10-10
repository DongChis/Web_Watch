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
 * Servlet implementation class DeleteProduct
 */
@WebServlet(urlPatterns = { "/deleteProduct" })
public class DeleteProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteProduct() {
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

		HttpSession session = request.getSession(false);
		String pid = request.getParameter("pid");

		User user = (User) session.getAttribute("accSession");

		System.out.println("Product ID to delete: " + pid); // Ghi log PID
		System.out.println("User deleting product: " + user.getUsername());
		System.out.println("Product ID to delete: " + pid); // Ghi log PID

		DAO.getInstance().deleteProduct(pid, user.getUsername());

		request.getRequestDispatcher("productListAdmin").forward(request, response);

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
