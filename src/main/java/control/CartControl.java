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
import javax.websocket.Session;

import dao.DAO;
import entity.CartItem;
import entity.Product;
import entity.User;


@WebServlet(name = "CartControl", urlPatterns = "/add-to-cart")
public class CartControl extends HttpServlet {
	
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		//response.sendRedirect("Cart.jsp");
    	request.getRequestDispatcher("Cart.jsp").forward(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
//		Object accSession = session.getAttribute("accSession");
//        if (accSession == null) {
//            // Nếu accSession là null, chuyển hướng về trang đăng nhập
//        	
//        	System.out.println("session null");
//           response.sendRedirect("Login.jsp");
//        
//          //  return; // Dừng xử lý thêm vào giỏ hàng
//        }
//        
		String productIdStr = request.getParameter("id");
        String quantityStr = request.getParameter("quantity");

      

        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("accSession");
        if (user == null) {
        	  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        Product product = DAO.getInstance().getProductByID(String.valueOf(productId));
       
      

<<<<<<< HEAD
        
=======
>>>>>>> b7c2ec775dbd71ab0fb4fd2dad21a712e9ba0197
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