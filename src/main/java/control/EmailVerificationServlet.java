package control;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import dao.DAOKey;
import entity.User;

/**
 * Servlet implementation class EmailVerificationServlet
 */
@WebServlet("/sendVerificationEmail")
public class EmailVerificationServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
			doGet(request, response);
		}
		
		

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
			return;
		}
		System.out.println(session);
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID not found in session.");
			return;
		}
	

		boolean isEmailVerified = false; // Hàm này kiểm tra xem email đã xác minh chưa
		System.out.println("post");
		if (!isEmailVerified) {
			
			User user = (User) DAO.getInstance().getUserByID(userId);

			String userEmail = user.getEmail();
			System.out.println("email: " + userEmail);
			try {
				DAO.getInstance().verifyEmail(userId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendVerificationEmail(userEmail, userId);
			
			response.sendRedirect("verifyEmail.jsp"); 
			return;
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

			String verificationLink = "http://localhost:8080/Web_Watch/verifyEmail?token="
					+ URLEncoder.encode(token, "UTF-8");

			String subject = "Xác minh email của bạn";
			String body = "Xin chào,\n\nVui lòng nhấp vào liên kết sau để xác minh email của bạn: \n" + verificationLink
					+ "\n\nLiên kết này sẽ hết hạn sau 24 giờ.\n\nTrân trọng.";

			// Kiểm tra các thông tin môi trường
			String emailUser = System.getenv("EMAIL_USER");
			String emailPass = System.getenv("EMAIL_PASS");
			
			System.out.println(emailUser + "|" + emailPass);

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
