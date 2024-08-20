package control;

import dao.DAO;
import entity.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterControl", urlPatterns = { "/signup" })
public class RegisterControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

       
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Gọi phương thức đăng ký tài khoản từ DAO
        
        User existingUser = DAO.getInstance().getUserByUsername(username);  
        if (existingUser == null) {
            try {
				DAO.getInstance().signUp(username, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            response.sendRedirect("Login.jsp"); 
        } else {
            request.setAttribute("message", "Tên đăng nhập đã tồn tại!");
            request.getRequestDispatcher("Login.jsp").forward(request, response); 
        }
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