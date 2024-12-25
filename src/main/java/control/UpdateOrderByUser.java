package control;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
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
import entity.Order;
import entity.User;

/**
 * Servlet implementation class UpdateOrderByUser
 */
@WebServlet(urlPatterns = { "/updateOrderByUser" })
public class UpdateOrderByUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateOrderByUser() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            String customerName = request.getParameter("customer-name");
            String customerEmail = request.getParameter("customer-email");
            String customerPhone = request.getParameter("customer-phone");
            String customerAddress = request.getParameter("customer-address");
            String paymentMethod = request.getParameter("payment-method");
            String sign = request.getParameter("sign");
         

            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
                return;
            }

            Integer userIdObj = (Integer) session.getAttribute("userId");
            if (userIdObj == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }

            int userId = userIdObj;

            DAOKey daoKey = DAOKey.getInstance();
            Map<String, String> keyInfo = daoKey.getKeyInfo(userId);
            if (keyInfo == null || keyInfo.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User public key is missing.");
                return;
            }

            String publicKeyString = keyInfo.get("publicKey");
            PublicKey publicKey = getPublicKeyFromString(publicKeyString);
            	System.out.println(publicKeyString);
            // Retrieve cart items from the existing order
            Order existingOrder = DAO.getInstance().getOrderDetailByOrderID(orderID);
            if (existingOrder == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
                return;
            }
            List<CartItem> cartItems = existingOrder.getCartItems();
            if (cartItems == null || cartItems.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart items are missing in the order.");
                return;
            }

            double totalPrice = 0;
            String src =
		    	    customerName +
		    	    "" + customerEmail +
		    	    "" + customerPhone +
		    	    "" + customerAddress +
		    	    "" + paymentMethod ;
		    	   
		   
		    	try {
		    		Order o = DAO.getInstance().getOrderDetailByOrderID(orderID);
		    		List<CartItem>	carts = o.getCartItems();
					for (CartItem item : carts) {
					   src += item.getProduct().getName() + "" + item.getQuantity() + "" + item.getTotalPrice() + " VND";
					    totalPrice += item.getTotalPrice();
					}
				} catch (Exception e) {
			
					e.printStackTrace();
				}

		    	src +=  totalPrice +" VND";
		    	System.out.println(src);

            boolean isVerified = verifySignatureWithPublicKey(publicKey, src, sign);
            System.out.println(isVerified);
            if (!isVerified) {
            	session.setAttribute("cartItems", cartItems);
                request.setAttribute("resultMessageUP", "Chữ ký không hợp lệ. Vui lòng kiểm tra lại.");
                request.setAttribute("messageColorUP", "red");
                request.setAttribute("order", DAO.getInstance().getOrderDetailByOrderID(orderID));
                request.getRequestDispatcher("EditOrderByUser.jsp").forward(request, response);
                
                return;
            }

            Order updatedOrder = new Order(orderID, cartItems, customerName, customerEmail, customerPhone,
                    customerAddress, paymentMethod, new Timestamp(System.currentTimeMillis()), sign, true);

            
            
            boolean isUpdated = DAO.getInstance().updateOrder(updatedOrder, orderID, userId);
            if (!isUpdated) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Order update failed.");
                return;
            }

            request.setAttribute("resultMessageUP", "Cập nhật đơn hàng thành công!");
            request.setAttribute("messageColorUP", "green");
            request.getRequestDispatcher("EditOrderByUser.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private PublicKey getPublicKeyFromString(String publicKeyString) throws Exception {
        try {
            publicKeyString = publicKeyString.replaceAll("\\s", "");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid public key or not Base64 encoded", e);
        }
    }

    private boolean verifySignatureWithPublicKey(PublicKey publicKey, String data, String clientSignatureStr) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            byte[] clientSignature = Base64.getDecoder().decode(clientSignatureStr);

            return signature.verify(clientSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
