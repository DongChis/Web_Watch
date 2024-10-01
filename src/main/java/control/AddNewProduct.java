package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddNewProduct
 */
@WebServlet(urlPatterns = {"/addNewProduct"})
public class AddNewProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNewProduct() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 request.setAttribute("addNewProduct", true);
		request.getRequestDispatcher("Admin.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String productName = request.getParameter("productName");
		String productPrice = request.getParameter("productPrice");
		String productDescription = request.getParameter("productDescription");
	
		if (productName == null || productName.isEmpty() ||
		    productPrice == null || productPrice.isEmpty()) {
		
			request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin sản phẩm.");
			request.getRequestDispatcher("Admin.jsp").forward(request, response);
			return;
		}

	
		HttpSession session = request.getSession();
		session.setAttribute("successMessage", "Sản phẩm mới đã được thêm thành công!");

		//response.sendRedirect("Admin.jsp");
	}

}
