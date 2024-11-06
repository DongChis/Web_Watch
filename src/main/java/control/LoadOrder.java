package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import entity.Order;

/**
 * Servlet implementation class LoadOrder
 */
@WebServlet(urlPatterns = {"/loadOrder"})
public class LoadOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        
        Order order = null;
        try {
            // Retrieve order details by orderID
            order = DAO.getInstance().getOrderDetailByOrderID(orderID);
            request.setAttribute("order", order);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve order details.");
            return;
        }

        // Set order object in the request scope
        request.setAttribute("order-info", order);
        
        // Forward the request to the JSP page for editing
        request.getRequestDispatcher("editOrder").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
