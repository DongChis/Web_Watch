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
import entity.User;

@WebServlet(name = "CartControl", urlPatterns = "/add-to-cart")
public class CartControl extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("Cart.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productIdStr = request.getParameter("id");
		String quantityStr = request.getParameter("quantity");

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("accSession");
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int productId = Integer.parseInt(productIdStr);
		int quantity = Integer.parseInt(quantityStr);

		Product product = DAO.getInstance().getProductByID(String.valueOf(productId));
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

		int totalQuantity = cart.stream().mapToInt(CartItem::getQuantity).sum();
		session.setAttribute("cart", cart);
		session.setAttribute("totalQuantity", totalQuantity);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.getWriter().write("{\"totalQuantity\": " + totalQuantity + "}");
	}
}