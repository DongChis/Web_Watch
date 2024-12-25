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
	 * @throws ServletException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    // Retrieve orderID from request
	    String orderIdParam = request.getParameter("orderID");

	    if (orderIdParam == null || orderIdParam.isEmpty()) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("Order ID is missing or invalid");
	        return;
	    }

	    int orderID = -1;
	    try {
	        orderID = Integer.parseInt(orderIdParam); // Parse the orderID
	    } catch (NumberFormatException e) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("Invalid Order ID format");
	        return;
	    }

	    // Call the DAO to cancel the order
	    boolean isCanceled = DAO.getInstance().cancelOrder(orderID);

	    // If canceled, update the order status
	    if (isCanceled) {
	        // Update the order status to 'Đã hủy' in the database or session
	        request.setAttribute("orderStatus", "cancel");
	    } else {
	        // Handle failure case (maybe order couldn't be canceled)
	        request.setAttribute("orderStatus", "Hủy thất bại");
	    }

	    // Forward to the JSP page where the updated order status will be shown
	    request.getRequestDispatcher("orderListAdmin").forward(request, response);
	}
	   
}
