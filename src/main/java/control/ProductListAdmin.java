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
        response.setContentType("text/html;charset=UTF-8");

        try {
            int pageSize = 6; // Số sản phẩm mỗi trang
            String pageParam = request.getParameter("page");
            int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;

            List<Product> listProduct = DAO.getInstance().getProductsByPage(page, pageSize);
            int totalProducts = DAO.getInstance().getTotalProducts(); // Tổng số sản phẩm
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

            request.setAttribute("productListAdmin", listProduct);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }

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
