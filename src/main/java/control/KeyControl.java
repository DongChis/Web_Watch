package control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.net.URLEncoder;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import entity.Alg_KEY;
import entity.User;

@WebServlet(urlPatterns = { "/keyControl" })
@MultipartConfig
public class KeyControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
		 String action = request.getParameter("action");
		 System.out.println("Received action: " + action);

		 if (action == null) {
		     response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is missing");
		     return;
		 }

	        switch (action) {
	            case "generateKey":
	                generateKey(request, response);
	                break;
	            case "importKey":
	                importKey(request, response);
	                break;
	            case "reportKey":
	                reportKey(request, response);
	                break;
	            default:
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
	                break;
	        }
	    }

	public void generateKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get the key size from the request
			String keySize = request.getParameter("keySize");

			if (keySize == null || keySize.isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key size is required");
				return;
			}

			// Create a new Alg_KEY object for generating the RSA key pair
			Alg_KEY algKey = new Alg_KEY();
			algKey.generateKey("RSA", Integer.parseInt(keySize));

			// Get the Base64 encoded public and private keys
			String publicKeyBase64 = algKey.getPublicKeyBase64();
			String privateKeyBase64 = algKey.getPrivateKeyBase64();

			// Get the session
			HttpSession session = request.getSession(false); // Use false to not create a new session
			if (session == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
				return;
			}

			Integer userIdObj = (Integer) session.getAttribute("userId");
			if (userIdObj == null) {
				// userID attribute is missing from session
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
				return;
			}

			int userId = userIdObj; // Get user ID from session

			// Calculate expiration time: 12 hours from the current time
			long createTimeMillis = System.currentTimeMillis();
			long expirationTimeMillis = createTimeMillis + (12 * 60 * 60 * 1000); // 12 hours in milliseconds

			// Format the expiration time (could use SimpleDateFormat or convert to a
			// readable format if needed)
			String expirationTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new java.util.Date(expirationTimeMillis));

			// Set the attributes to forward to the JSP
			request.setAttribute("publicKey", publicKeyBase64); // Public key for display
			request.setAttribute("privateKey", privateKeyBase64); // Private key (inform user to download it)
			request.setAttribute("createTime",
					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(createTimeMillis))); // Creation
																															// time
																															// in
																															// readable
																															// format
			request.setAttribute("endTime", expirationTime); // Expiration time, 12 hours after creation

			// Forward the request to the Function.jsp page to display the keys
			request.getRequestDispatcher("Function.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			// Handle the case where the key size is invalid
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid key size");
		} catch (Exception e) {
			// Handle any other exceptions
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating key");
		}
	}

	private void importKey(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		 try {
	            // Get the key information from the request
	            String publicKey = request.getParameter("publicKey");	           
	            
	            Part filePart = request.getPart("publicKeyFile");  // For file uploads
	            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
	            String uploadDir = getServletContext().getRealPath("/") + "uploads"; // Absolute directory path on the server
	            File uploads = new File(uploadDir);
	            if (!uploads.exists()) {
	                uploads.mkdir();
	            }
	            File file = new File(uploads, fileName);
	            
	            String src = file.getAbsolutePath();
	            System.out.println("path :  " + src);
	           

	            DAOKey daoKey = new DAOKey();
	            Date createTime = new Date();
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(createTime);
	            calendar.add(Calendar.HOUR, 12);
	            Date endTime = calendar.getTime();

	            if ((publicKey == null || publicKey.isEmpty()) && (filePart == null || filePart.getSize() == 0)) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Public Key or Public Key File is required");
	                return;
	            }

	            // Retrieve userId from the session
	            HttpSession session = request.getSession(false);
	            if (session == null || session.getAttribute("userId") == null) {
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
	                return;
	            }

	            Integer userIdObj = (Integer) session.getAttribute("userId");
	            int userId = userIdObj != null ? userIdObj : -1;
	            if (userId == -1) {
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
	                return;
	            }

	            // Save the public key to the database if it exists
	            boolean isPublicKeySaved = false;
	            if (publicKey != null && !publicKey.isEmpty()) {
	                isPublicKeySaved = daoKey.savePublicKey(userId, publicKey, createTime, endTime);
	            }
	            if (!isPublicKeySaved) {
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving Public Key");
	                return;
	            }
	            System.out.println("path: "+src);
	            
	            if (filePart != null ) {
	                boolean isPublicKeyFileSaved = daoKey.savePublicKeyFile(userId, src, createTime, endTime);
	                if (!isPublicKeyFileSaved) {
	                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving Public Key File");
	                    return;
	                }
	            }

	            request.setAttribute("message", "Khóa đã được lưu thành công!");
	            response.sendRedirect("keyControl");

	        } catch (Exception e) {
	            e.printStackTrace();
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving key information");
	        }
	    
    
	}


	private void reportKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String privateKeyReport = request.getParameter("reportPrivateKey");
			int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString()); // Lấy userId từ
																									// session

			// Cập nhật thời gian báo mất khóa
			DAOKey daoKey = new DAOKey();
			daoKey.reportLostKey(userId); // Truyền userId để xác định khóa

			// Chuyển tiếp tới trang kết quả
			response.sendRedirect("Function.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reporting lost key.");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Retrieve the session and userId
			HttpSession session = request.getSession(false); // Use false to not create a new session
			if (session == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
				return;
			}

			Integer userId = (Integer) session.getAttribute("userId");
			if (userId == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID not found in session.");
				return;
			}
			   // Kiểm tra xem người dùng đã xác minh email chưa
	        
	        boolean isEmailVerified = false; // Hàm này kiểm tra xem email đã xác minh chưa

	        if (!isEmailVerified) {
	            // Nếu email chưa được xác minh, gửi email xác minh
	            User user =(User) DAO.getInstance().getUserByID(userId+"");
	           
	            String userEmail = user.getEmail();
	            
	            sendVerificationEmail(userEmail, userId);
	            
	            response.sendRedirect("verifyEmail.jsp"); // Chuyển hướng đến trang yêu cầu xác minh email
	            return;
	        }
			

			// Retrieve key information from DAO
			DAOKey daoKey = new DAOKey();
			Map<String, String> keyInfo = daoKey.getKeyInfo(userId);

			// If key information is not available, set default messages
			String publicKey = keyInfo.get("publicKey");
			String createTime = keyInfo.get("createTime");
			String endTime = keyInfo.get("endTime");

			// Check if key info exists, otherwise provide a default message
			publicKey = publicKey != null ? publicKey : "Chưa có khóa công khai";
			createTime = createTime != null ? createTime : "Chưa có thông tin";
			endTime = endTime != null ? endTime : "Chưa có thông tin";

			// Set the information in the request attributes for use in the JSP page
			request.setAttribute("publicKey", publicKey);
			request.setAttribute("createTime", createTime);
			request.setAttribute("endTime", endTime);

			// Forward to the Function.jsp page for display
			request.getRequestDispatcher("Function.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving key information.");
		}
	}
	
	private void sendVerificationEmail(String email, int userId) {
	    try {
	        if (email == null || email.isEmpty()) {
	            System.out.println("Email không hợp lệ.");
	            return;
	        }

	        String token = generateToken();
	        if (token == null || token.isEmpty()) {
	            System.out.println("Không thể tạo token.");
	            return;
	        }

	        // Lưu token và thời gian hết hạn vào database
	        DAOKey daoKey = new DAOKey();
	        daoKey.saveToken(userId, token);

	        String verificationLink = "http://localhost:8080/Web_Watch/verifyEmail?token=" + URLEncoder.encode(token, "UTF-8");

	        String subject = "Xác minh email của bạn";
	        String body = "Xin chào,\n\nVui lòng nhấp vào liên kết sau để xác minh email của bạn: \n"
	                + verificationLink + "\n\nLiên kết này sẽ hết hạn sau 24 giờ.\n\nTrân trọng.";

	        // Kiểm tra các thông tin môi trường
	        String emailUser = System.getenv("EMAIL_USER");
	        String emailPass = System.getenv("EMAIL_PASS");

	        if (emailUser == null || emailPass == null) {
	            System.out.println("Thông tin email không hợp lệ.");
	            return;
	        }

	        Properties props = new Properties();
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");

	        Session session = Session.getInstance(props, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(emailUser, emailPass);
	            }
	        });

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(emailUser));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
	        message.setSubject(subject);
	        message.setText(body);

	        Transport.send(message);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	private String generateToken() {
	    return UUID.randomUUID().toString();
	}


}
