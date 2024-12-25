package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import dao.DAOKey;

/**
 * Servlet implementation class VerifyEmailServlet
 */
@WebServlet("/verifyEmail")
public class VerifyEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	  String token = request.getParameter("token");

          if (token == null || token.isEmpty()) {
              response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token is missing");
              return;
          }

          try {
              // Kiểm tra token trong cơ sở dữ liệu
              DAOKey daoKey = new DAOKey();
              boolean isTokenValid = daoKey.isTokenValid(token);
              
              if (isTokenValid) {
                  // Xác minh email của người dùng
                  int userId = daoKey.getUserIdByToken(token);
                  System.out.println( "Cập nhâp verify email :"+ DAO.getInstance().verifyEmail(userId));

                  // Gửi phản hồi thành công
                  response.setStatus(HttpServletResponse.SC_OK);
                  request.getRequestDispatcher("keyControl").forward(request, response);
              } else {
                  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                  response.getWriter().println("Token không hợp lệ hoặc đã hết hạn.");
              }
          } catch (Exception e) {
              // Log lỗi và gửi phản hồi lỗi cho người dùng
              e.printStackTrace();
              response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý yêu cầu.");
          }
    }
}