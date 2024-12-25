package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import entity.Order;

/**
 * Servlet implementation class CancelOrder
 */
@WebServlet(urlPatterns = {"/cancelOrder"})
public class CancelOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    // Lấy ID đơn hàng từ yêu cầu
	    String orderIDStr = request.getParameter("orderID");
	    int orderID = Integer.parseInt(orderIDStr);
	    
	    try {
			Order order = DAO.getInstance().getOrderDetailByOrderID(orderID);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    

	    

	        // Chuyển hướng sang trang JSP để hiển thị kết quả
	        request.getRequestDispatcher("orderListAdmin").forward(request, response);

	    } 

	   
}
