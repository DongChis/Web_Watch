package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;

/**
 * Servlet implementation class DeleteAllRecentProduct
 */
@WebServlet(urlPatterns = {"/clearDeletedProducts"})
public class DeleteAllRecentProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAllRecentProduct() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * count = 0
     * insert = 1 
     * delete = 2
     * 
     * 
     * click "xoa"
     *  count == insert
     *  true => count = delete
     *  
     *  if(count = delete) =>
     *  
     * 
     */
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  DAO.getInstance().clearRecentDeletedProducts(); // Gọi hàm xóa tất cả dữ liệu
	      response.sendRedirect("productListAdmin");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
