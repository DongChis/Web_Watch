package control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.DAOKey;

/**
 * Servlet implementation class UserVerification
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 10, // Giới hạn tệp tối đa là 10 MB
		maxRequestSize = 1024 * 1024 * 20 // Giới hạn yêu cầu tối đa là 20 MB
)
@WebServlet(name = "UserVerification", urlPatterns = { "/userVerification" })
public class UserVerification extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		boolean isVerified = false;

		// Kiểm tra session
		if (session == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
			return;
		}

		String userIdString = String.valueOf(session.getAttribute("userId"));
		if (userIdString == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is missing.");
			return;
		}

		try {
			int userId = Integer.parseInt(userIdString);
			DAOKey daoKey = DAOKey.getInstance();
			Map<String, String> keyInfo = daoKey.getKeyInfo(userId);

			if (keyInfo != null && !keyInfo.isEmpty()) {
				request.setAttribute("publicKey", keyInfo.get("publicKey"));
			} else {
				request.setAttribute("error", "No public key information found.");
				return;
			}
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid User ID.");
			return;
		}

		// Lấy khóa công khai
		String publicKeyString = (String) request.getAttribute("publicKey");
		if (publicKeyString == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Public Key is missing.");
			return;
		}

		PublicKey publicKey = null;
		try {
			publicKey = getPublicKeyFromString(publicKeyString);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while processing public key.");
			return;
		}

		// Kiểm tra lựa chọn chữ ký
		String signatureOption = request.getParameter("signatureOption");

		System.out.println("Lựa chọn phương thức ký: " + signatureOption); 

		if ("text".equals(signatureOption)) {
			System.out.println("text");
			String signatureText = request.getParameter("signatureText");
			if(signatureText .equals("")){
				request.setAttribute("resultMessage", "vui long nhap chu ky");
			}
			if (signatureText != null && !signatureText.isEmpty()) {
				isVerified = verifySignatureWithPublicKey(publicKey, "DONG CHI", signatureText);
			}
		} else if ("file".equals(signatureOption)) {
			System.out.println("file");
		}

		// Thêm thông báo kết quả vào request để hiển thị trong JSP
		String message = isVerified ? "Chữ ký hợp lệ!" : "Chữ ký không hợp lệ.";
		String messageColor = isVerified ? "green" : "red";
		request.setAttribute("resultMessage", message);
		request.setAttribute("messageColor", messageColor);

		if (isVerified) {
		     request.getRequestDispatcher("CheckOut.jsp").forward(request, response);
		} else {
		    // Handle failure (if needed)
		    request.getRequestDispatcher("userVerification.jsp").forward(request, response);
		}
		
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

	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("userVerification.jsp").forward(request, response);
	}
}
