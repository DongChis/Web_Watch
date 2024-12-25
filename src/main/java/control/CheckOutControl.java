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

/**
 * Servlet implementation class CheckOutControl
 */
@WebServlet(urlPatterns = { "/checkout" })
public class CheckOutControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckOutControl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

		protected void doPost(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		    response.setContentType("text/html;charset=UTF-8");

		    HttpSession session = request.getSession(false); // Không tạo session mới
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

		    // Lấy thông tin chữ ký từ request
		    String customerName = request.getParameter("customer-name");
		    String customerEmail = request.getParameter("customer-email");
		    String customerPhone = request.getParameter("customer-phone");
		    String customerAddress = request.getParameter("customer-address");
		    String paymentMethod = request.getParameter("payment-method");
		    String sign = request.getParameter("sign");
		    
		    // Lấy khóa công khai từ cơ sở dữ liệu
		    DAOKey daoKey = DAOKey.getInstance();
		    Map<String, String> keyInfo = daoKey.getKeyInfo(userId);
		    if (keyInfo == null || keyInfo.isEmpty()) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User public key is missing.");
		        return;
		    }

		    String publicKeyString = keyInfo.get("publicKey");
		    PublicKey publicKey;
		    try {
		        publicKey = getPublicKeyFromString(publicKeyString);
		        System.out.println(publicKey);
		    } catch (Exception e) {
		        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid public key.");
		        return;
		    }
		    
		    String tt = "1111credit-carddong ho 41250.0 VND250.0 VND";
		    double totalPrice = 0;
		    String src = "Thông Tin Đơn Hàng\n" +
		    	    "\n" +
		    	    "Thông Tin Người Nhận:\n" +
		    	    "Tên Người Nhận: " + customerName + "\n" +
		    	    "Email: " + customerEmail + "\n" +
		    	    "Số Điện Thoại: " + customerPhone + "\n" +
		    	    "Địa Chỉ: " + customerAddress + "\n" +
		    	    "\n" +
		    	    "Phương Thức Thanh Toán: " + paymentMethod + "\n" +
		    	    "\n" +
		    	    "Sản Phẩm:\n";
		   
		    	try {
		    		List<CartItem>	carts = (List<CartItem>)session.getAttribute("cart");
					for (CartItem item : carts) {
					    src += item.getProduct().getName() + " - Số lượng: " + item.getQuantity() + " - Giá: " + item.getTotalPrice()/item.getQuantity() + " VND\n";
					    totalPrice += item.getTotalPrice();
					}
				} catch (Exception e) {
			
					e.printStackTrace();
				}

		    	src += "\nTổng Tiền: " + totalPrice  + " VND";

		    // Xác thực chữ ký
		    	System.out.println(tt);
		    	
		    boolean isVerified = verifySignatureWithPublicKey(publicKey, tt, sign);
		    System.out.println("verifi don hang : "+isVerified);
		    if (!isVerified) {
		        request.setAttribute("resultMessageDH", "Chữ ký không hợp lệ. Vui lòng kiểm tra lại.");
		        request.getRequestDispatcher("CheckOut.jsp").forward(request, response);
		        return;
		    }else {
		    	session.removeAttribute("cart");
		    	
		    	response.sendRedirect("OrderConfirm.jsp");
		    }
		    String message = isVerified ? "Chữ ký hợp lệ!" : "Chữ ký không hợp lệ.";
		    String messageColor = isVerified ? "green" : "red";
		    request.setAttribute("resultMessageDH", message);
		    request.setAttribute("messageColorDH", messageColor);
		    
		    // Xử lý đặt hàng nếu chữ ký hợp lệ
		    List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
		    DAO.getInstance().insertOrder(cartItems, customerName, customerEmail, customerPhone, customerAddress, paymentMethod, sign, userId);

		    // Chuyển hướng đến trang xác nhận
		    
		}
		// Lấy PublicKey từ chuỗi Base64
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

		// Xác minh chữ ký với khóa công khai
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