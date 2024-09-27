package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.CartItem;
import entity.Product;


@WebServlet(name = "CartControl", urlPatterns = "/add-to-cart")
public class CartControl extends HttpServlet {
	
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
       response.sendRedirect("Cart.jsp");
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdStr = request.getParameter("id");
        String quantityStr = request.getParameter("quantity");

        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        Product product = DAO.getInstance().getProductByID(String.valueOf(productId));

        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean exist = false;
        for (CartItem item : cart) {
            if (item.getProduct().getProductID() == productId) {
                item.setQuantity(item.getQuantity() + quantity);
                exist = true;
                break;
            }
        }

        if (!exist) {
            cart.add(new CartItem(product, quantity));
        }

        session.setAttribute("cart", cart);
       response.setStatus(HttpServletResponse.SC_OK);
        
    }
}