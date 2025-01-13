package control;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import dao.DAOKey;
import entity.CartItem;

@WebServlet(urlPatterns = { "/checkout" })
public class CheckOutControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CheckOutControl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect GET requests to the cart page
        response.sendRedirect("CheckOut.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
            return;
        }

        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }
        int userId = userIdObj;

        String customerName = request.getParameter("customer-name");
        String customerEmail = request.getParameter("customer-email");
        String customerPhone = request.getParameter("customer-phone");
        String customerAddress = request.getParameter("customer-address");
        String paymentMethod = request.getParameter("payment-method");

     

        double totalPrice = 0;
        StringBuilder src = new StringBuilder()
                .append(customerName)
                .append(customerEmail)
                .append(customerPhone)
                .append(customerAddress)
                .append(paymentMethod);

        List<CartItem> cartItems;
        try {
            cartItems = (List<CartItem>) session.getAttribute("cart");
            if (cartItems == null || cartItems.isEmpty()) {
                response.sendRedirect("Cart.jsp");
                return;
            }

            for (CartItem item : cartItems) {
                src.append(item.getProduct().getName())
                   .append(item.getQuantity())
                   .append(item.getTotalPrice());
                totalPrice += item.getTotalPrice();
            }
            src.append(totalPrice);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing cart items.");
            return;
        }

 
       
            try {
                DAO dao = DAO.getInstance();
                boolean orderInserted = dao.insertOrder(cartItems, customerName, customerEmail, customerPhone, customerAddress, paymentMethod, userId);

                if (orderInserted) {
                    session.removeAttribute("cart"); // Clear cart after successful order placement
                    response.sendRedirect("OrderSuccess.jsp");
                } else {
                    request.setAttribute("errorMessage", "Failed to place the order. Please try again.");
                    request.getRequestDispatcher("CheckOut.jsp").forward(request, response);
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error inserting order: " + e.getMessage());
            }
        
    }


}
