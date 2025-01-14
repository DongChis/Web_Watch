package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.CartItem;
import entity.User;

@WebServlet(name = "LoginControl", urlPatterns = {"/login"})
public class LoginControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập mã hóa và kiểu nội dung
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // Lấy thông tin username và password từ yêu cầu
        String userName = request.getParameter("user");
        String password = request.getParameter("pass");

        // Kiểm tra đầu vào hợp lệ
        if (userName == null || userName.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("mess", "Username and password must not be empty.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        try {
            // Kiểm tra thông tin đăng nhập qua DAO
            User acc = DAO.getInstance().login(userName, password);

            if (acc == null) {
                // Sai thông tin đăng nhập
                request.setAttribute("mess", "Wrong username or password.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }

            // Kiểm tra session hiện tại
            HttpSession currentSession = request.getSession(false);
            if (currentSession != null && currentSession.getAttribute("accSession") != null) {
                // Nếu đã đăng nhập trước đó, chuyển hướng tới trang admin
                response.sendRedirect("admin");
                return;
            }

            // Nếu đăng nhập thành công, tạo session mới
            HttpSession session = request.getSession(true);
            session.setAttribute("accSession", acc);
            session.setAttribute("userId", acc.getUserID());
            session.setAttribute("role", acc.getRole());
            

            // Tạo giỏ hàng riêng cho người dùng
            List<CartItem> cart = new ArrayList<>();
            session.setAttribute("cart", cart);

            // Phân quyền và chuyển hướng
            if ("Admin".equals(acc.getRole())) {
            	session.setAttribute("hasAccessedAdmin",true);
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("home"); // Chuyển hướng tới trang home cho người dùng không phải admin
            }

        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có vấn đề xảy ra
            request.setAttribute("mess", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            e.printStackTrace(); // Ghi log lỗi để theo dõi
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
