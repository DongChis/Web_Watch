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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productIdStr = request.getParameter("id");
		if (productIdStr == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"Product ID is missing\"}");
			return;
		}

		int productId;
		try {
			productId = Integer.parseInt(productIdStr);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"Invalid product ID\"}");
			return;
		}

		HttpSession session = request.getSession();
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		if (cart == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"Cart is empty\"}");
			return;
		}

		boolean itemRemoved = cart.removeIf(item -> item.getProduct().getProductID() == productId);
		 System.out.println("Product ID: " + productIdStr);
		    System.out.println("Cart: " + cart);
		    System.out.println(itemRemoved);

		if (!itemRemoved) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"Product not found in cart\"}");
			return;
		}

		int totalQuantity = cart.stream().mapToInt(CartItem::getQuantity).sum();
		session.setAttribute("cart", cart);
		session.setAttribute("totalQuantity", totalQuantity);

		response.setContentType("application/json");
		response.getWriter().write("{\"totalQuantity\": " + totalQuantity + "}");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("add-to-cart");
	}
}
