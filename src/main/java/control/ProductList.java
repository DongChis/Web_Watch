package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import entity.Product;

@WebServlet(urlPatterns = {"/productList"})
public class ProductList extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		try {
			int pageSize = 12; // Số sản phẩm mỗi trang
			String pageParam = request.getParameter("page");
			int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;

			List<Product> listProduct = DAO.getInstance().getProductsByPage(page, pageSize);
			int totalProducts = DAO.getInstance().getTotalProducts();
			int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

			request.setAttribute("listAllProduct", listProduct);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("currentPage", page);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
		}

		request.getRequestDispatcher("ProductList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
