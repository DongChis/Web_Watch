package control;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.CartItem;

@WebServlet(name = "RemoveFromCartControl", urlPatterns = { "/remove-from-cart" })
public class RemoveFromCartControl extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("add-to-cart");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productIdStr = request.getParameter("id");
		if (productIdStr == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		int productId;
		try {
			productId = Integer.parseInt(productIdStr);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		HttpSession session = request.getSession();
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		if (cart != null) {
			cart.removeIf(item -> item.getProduct().getProductID() == productId);
			int totalQuantity = 0;
			for (CartItem item : cart) {
				totalQuantity += item.getQuantity();

				
			}

			session.setAttribute("cart", cart);
			session.setAttribute("totalQuantity", totalQuantity);
			 response.setContentType("application/json");
		     response.getWriter().write("{\"totalQuantity\": " + totalQuantity + "}");
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect("add-to-cart");
		// Trả về thông tin tổng số lượng dưới dạng JSON
       
	}
}
