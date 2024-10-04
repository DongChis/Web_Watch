package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProductListAdmin
 */
@WebServlet(urlPatterns = {"/productListAdmin"})
public class ProductListAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductListAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[][] products = {
	            {"1", "Sản phẩm A", "100000", "Mô tả sản phẩm A"},
	            {"2", "Sản phẩm B", "200000", "Mô tả sản phẩm B"},
	            {"3", "Sản phẩm C", "150000", "Mô tả sản phẩm C"},
	        };
		request.setAttribute("productListAdmin", products);
		request.getRequestDispatcher("Admin.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
