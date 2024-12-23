package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOKey;

/**
 * Servlet implementation class VerifyEmailServlet
 */
@WebServlet("/verifyEmail")
public class VerifyEmailServlet extends HttpServlet {
	 private static final long serialVersionUID = 1L;
	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        String token = request.getParameter("token");

	        if (token == null || token.isEmpty()) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token is missing");
	            return;
	        }
	        try {
	        // Kiểm tra token trong database
	        DAOKey daoKey = new DAOKey();
	        boolean isTokenValid = daoKey.isTokenValid(token);

	        if (isTokenValid) {
	            // Xác minh email của người dùng
	            int userId;
				
					userId = daoKey.getUserIdByToken(token);
				
	            daoKey.verifyEmail(userId); // Cập nhật trạng thái xác minh email trong DB

	            // Thông báo xác minh thành công
	            response.getWriter().println("Email đã được xác minh thành công.");
	        } else {
	            response.getWriter().println("Token không hợp lệ hoặc đã hết hạn.");
	        }
	        } catch (Exception e) {
				
				e.printStackTrace();
			}
	    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
