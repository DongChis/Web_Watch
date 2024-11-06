package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.CartItem;
import entity.Order;

import dao.DAO;

/**
 * Servlet implementation class EditOrderControl
 */
@WebServlet(urlPatterns = {"/editOrder"})
public class EditOrderControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditOrderControl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("orderEdit", true);

        int orderID = Integer.parseInt(request.getParameter("orderID"));
        String customerName = request.getParameter("customerName");
       // List<CartItem> listCart = request.get
        String customerPhone = request.getParameter("customerPhone");
        String customerEmail = request.getParameter("customerEmail");
        String customerAddress = request.getParameter("customerAddress");
        String orderDate = request.getParameter("orderDate");
        String paymentMethod = request.getParameter("paymentMethod");
        
        Order order = null;

        try {
            order = DAO.getInstance().getOrderDetailByOrderID(orderID);
            boolean isUpdated = DAO.getInstance().updateOrder(order, orderID);

            if (!isUpdated) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Order update failed.");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the order.");
            return;
        }

        //response.sendRedirect("orderDetail?orderID=" + orderID);
        request.getRequestDispatcher("orderListAdmin").forward(request, response);
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
