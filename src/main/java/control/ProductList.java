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

@WebServlet(urlPatterns = { "/productList" })
public class ProductList extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Số sản phẩm mỗi trang
            int pageSize = 6;

            // Lấy tham số từ request
            String pageParam = request.getParameter("page");
            String priceRange = request.getParameter("priceRange");
            String gender = request.getParameter("gender");
            String searchQuery = request.getParameter("search");

            // Xử lý giá trị trang hiện tại
            int page = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;

            // Lấy danh sách sản phẩm theo bộ lọc
            DAO dao = DAO.getInstance();
            List<Product> listProduct = dao.getFilteredProducts(priceRange, gender, searchQuery, page, pageSize);

            // Tính tổng số sản phẩm và số trang
            int totalProducts = dao.getTotalFilteredProducts(priceRange, gender, searchQuery);
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

            // Truyền dữ liệu vào request
            request.setAttribute("listAllProduct", listProduct);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("priceRange", priceRange);
            request.setAttribute("gender", gender);
            request.setAttribute("searchQuery", searchQuery);

        } catch (NumberFormatException e) {
            // Xử lý lỗi khi tham số không hợp lệ
            request.setAttribute("error", "Giá trị trang không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            // Xử lý lỗi chung
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }

        // Chuyển tiếp đến JSP
        request.getRequestDispatcher("ProductList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
