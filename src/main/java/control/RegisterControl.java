package control;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
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

import dao.DAO;
import dao.DAOKey;
import entity.User;

@WebServlet("/signup")
public class RegisterControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("user");
        String password = request.getParameter("pass");
        String email = request.getParameter("email");

        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            !isValidEmail(email)) {
            request.setAttribute("mess", "Invalid input. Please fill all fields correctly.");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
            return;
        }

        try {
            DAO dao = DAO.getInstance();

            // Kiểm tra xem username hoặc email đã tồn tại chưa
            if (dao.getUserByUsername(username) != null) {
                request.setAttribute("mess", "Username already exists.");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            if (dao.getUserByEmail(email) != null) {
                request.setAttribute("mess", "Email already in use.");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Lưu người dùng và tạo token
            dao.signUp(username, password, email);
            User user = (User)dao.getUserByEmail(email);
            // Gửi email xác minh
            sendVerificationEmail(email, user.getUserID());

            // Thông báo gửi email xác minh
            request.setAttribute("mess", "A verification email has been sent. Please check your email to verify.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mess", "e.get");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void sendVerificationEmail(String email, int userId) throws Exception {
        String token = generateToken();
        DAOKey.getInstance().saveToken(userId, token);

        String verificationLink = "http://localhost:8080/Web_Watch/verifyEmail?token=" + URLEncoder.encode(token, "UTF-8");
        String subject = "Verify your email";
        String body = "Xin chào,\n\nVui lòng nhấp vào liên kết sau để xác minh email của bạn: \n" + verificationLink
                + "\n\nLiên kết này sẽ hết hạn sau 1 phút. Vui lòng xác minh sớm nhất có thể để tránh mất hiệu lực.\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ hỗ trợ.";

        String emailUser = System.getenv("EMAIL_USER");
        String emailPass = System.getenv("EMAIL_PASS");

        if (emailUser == null || emailPass == null) {
            throw new IllegalStateException("Email credentials not found.");
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
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
