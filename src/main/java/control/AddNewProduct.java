package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;

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
		request.getRequestDispatcher("productListAdmin").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Retrieve parameters from the request
	    String title = request.getParameter("title"); // Updated to title
	    String productName = request.getParameter("productName");
	    String productPriceStr = request.getParameter("productPrice");
	    String productDescription = request.getParameter("productDescription");
	    String imageUrl = request.getParameter("imageUrl"); // Assuming you have an image URL parameter
	    String gender = request.getParameter("gender"); // Assuming you have a gender parameter

	    // Input validation
	    if (title == null || title.isEmpty() ||
	        productName == null || productName.isEmpty() ||
	        productPriceStr == null || productPriceStr.isEmpty() ||
	        productDescription == null || productDescription.isEmpty() ||
	        imageUrl == null || imageUrl.isEmpty() || // Validate image URL
	        gender == null || gender.isEmpty()) { // Validate gender

	        request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin sản phẩm.");
	        request.getRequestDispatcher("Admin.jsp").forward(request, response);
	        return;
	    }

	    // Parse the product price to a double
	    double productPrice;
	    try {
	        productPrice = Double.parseDouble(productPriceStr);
	    } catch (NumberFormatException e) {
	        request.setAttribute("errorMessage", "Giá sản phẩm không hợp lệ.");
	        request.getRequestDispatcher("Admin.jsp").forward(request, response);
	        return;
	    }

	    try {
	        // Generate a new product ID (this can be handled by the database if using auto-increment)
	        int productId = generateProductId(); // You need to implement this method

	        // Call the method to add a new product
	        DAO.getInstance().addProduct(productId, title, productName, productDescription, productPrice, imageUrl, gender);

	        // Set a success message in the session
	        HttpSession session = request.getSession();
	        session.setAttribute("successMessage", "Sản phẩm mới đã được thêm thành công!");

	        // Redirect to Admin.jsp or another page as needed
	        response.sendRedirect("Admin.jsp");
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Set an error message if something goes wrong
	        request.setAttribute("errorMessage", "Lỗi trong quá trình thêm sản phẩm. Vui lòng thử lại sau.");
	        request.getRequestDispatcher("Admin.jsp").forward(request, response);
	    }
	}

	// Example method to generate a new Product ID (implement according to your needs)
	private int generateProductId() {
	    // Logic to generate a new ProductID (consider using a database query or sequence)
	    return 1; // Placeholder return value; adjust as necessary
	}


}
