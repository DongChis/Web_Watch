package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import dao.DAOKey;

@WebServlet("/verifyEmail")
public class VerifyEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("mess", "Token not provided.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        try {
            DAOKey daoKey = DAOKey.getInstance();
            boolean isTokenValid = daoKey.isTokenValid(token);
            System.out.println(token);
            if (isTokenValid) {
                // Lấy userId từ token
                int userId = daoKey.getUserIdByToken(token);

                if (userId == -1) {
                    // Nếu không tìm thấy userId hợp lệ
                    request.setAttribute("mess", "Invalid token. Please try again.");
                    request.getRequestDispatcher("Register.jsp").forward(request, response);
                    return;
                }

                // Cập nhật emailVerified thành TRUE trong bảng Users
                boolean isVerified = DAO.getInstance().verifyEmail(userId);

                if (isVerified) {
     
                    // Thông báo thành công
                    request.setAttribute("mess", "Email verified successfully! Please log in.");
                    response.sendRedirect("Login.jsp"); // Dùng redirect để tránh việc reload lại trang
                } else {
                    request.setAttribute("mess", "Something went wrong while verifying your email.");
                    request.getRequestDispatcher("Register.jsp").forward(request, response);
                }

            } else {
                // Nếu token không hợp lệ hoặc hết hạn
                int userId = daoKey.getUserIdByToken(token);
                DAO.getInstance().deleteUserWithTokens(userId);
//                if (userId != -1) {
//                    DAO.getInstance().deleteFromPendingVerification(userId); // Xóa người dùng khỏi bảng PendingVerification
//                }

                // Hiển thị thông báo token không hợp lệ hoặc hết hạn
                request.setAttribute("mess", "Invalid or expired token. Your account has been removed. Please register again.");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while verifying email.");
        }
    }
}
