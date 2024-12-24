package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
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

import dao.DAO;
import dao.DAOKey;
import entity.User;

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

		Integer userIdString = (Integer)session.getAttribute("userId");
		if (userIdString == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is missing.");
			return;
		}

		try {
			int userId = userIdString;
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
		// thong tin nguoi dung
		User user = DAO.getInstance().getUserByID(userIdString);
		String src = user.getUsername() + " " + user.getPassword() + " " + user.getEmail();
		System.out.println(src);
		// Kiểm tra lựa chọn chữ ký
		String signatureOption = request.getParameter("signatureOption");
		System.out.println("Lựa chọn phương thức ký: " + signatureOption);

		if ("text".equals(signatureOption)) {
		
			String signatureText = request.getParameter("signatureText");
			if (signatureText.equals("")) {
				request.setAttribute("resultMessage", "vui long nhap chu ky");
			}
			if (signatureText != null && !signatureText.isEmpty()) {
				
				isVerified = verifySignatureWithPublicKey(publicKey, "DONG CHI", signatureText);
				System.out.println(signatureText);
				 System.out.println("Verifile chu ky(text):"+ isVerified);
			}
		}else if ("file".equals(signatureOption)) {
		    // Lấy file chữ ký từ request
		    Part filePart = request.getPart("signatureFile");

		    if (filePart != null && filePart.getSize() > 0) {
		        // Đọc nội dung file
		        InputStream fileContent = filePart.getInputStream();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));
		        StringBuilder fileContentBuilder = new StringBuilder();

		        String line;
		        while ((line = reader.readLine()) != null) {
		            fileContentBuilder.append(line);
		        }
		        String srcfile = "qd4iprFOC/fkW7mehWOgsHHzIX7fWXso1pV5PeWLRIlUgQrbDQ+ 5a+mjernQJF+KrXvYKklvycYEX46mP4ZeiiDuzfNz3g28lQt5s9fQcYiK3woZr4kC2VDkU7/CtvaWXiSfjt9Rg/blIkf0urBm2jaZGB370EEOd9UhajFBcEl8E2SWanzFTajGVIvpEyelVGzinbI2JvWVGlYnw2XQ0/bbxFtrWv2ZrwAnnoZEBnp2uosjN5PsOQnldZHuUbXryAON8S1eGE8oCleuamKfz2D4Nluwl9CcmAUvExWMpP2q0iXSz/ehITAgUUhce59s2XYzgGpm9CxANWiN+KtAwQ==";
		        String signatureFileText = fileContentBuilder.toString().trim(); // Dữ liệu trong file
		        System.out.println("File content: " + signatureFileText);

		        // Tiến hành xác minh chữ ký từ nội dung file
		        if (signatureFileText != null && !signatureFileText.isEmpty()) {
		        	
		            isVerified = verifySignatureWithPublicKey(publicKey, "DONG CHI", srcfile);
		            System.out.println("Verifile chu ky(file):"+ isVerified);
		        }
		    } else {
		        request.setAttribute("resultMessage", "Vui lòng tải lên file chữ ký");
		    }
		   
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