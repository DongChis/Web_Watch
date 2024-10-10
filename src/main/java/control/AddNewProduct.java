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
 * Servlet implementation class AddNewProduct
 */
@WebServlet(urlPatterns = { "/addNewProduct" })
public class AddNewProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddNewProduct() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("addNewProduct", true);
		request.getRequestDispatcher("productListAdmin").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Retrieve parameters from the request
		String title = request.getParameter("title"); // Updated to title
		String productName = request.getParameter("productName");
		String productPriceStr = request.getParameter("productPrice");
		String productDescription = request.getParameter("productDescription");
		String imageUrl = request.getParameter("productImage"); // Assuming you have an image URL parameter

		HttpSession session = request.getSession();
		User acc = (User) session.getAttribute("accSession");
		String gender = acc.getRole();

		DAO.getInstance().insertProduct(title, productName, productDescription, productPriceStr, imageUrl, gender);
		request.getRequestDispatcher("productListAdmin").forward(request, response);
	}

}
