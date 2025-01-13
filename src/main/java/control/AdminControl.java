package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/admin"})
public class AdminControl extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    request.setCharacterEncoding("UTF-8");

    // Lấy session hiện tại (có thể là null nếu không có session nào)
    HttpSession session = request.getSession(false); // false để không tạo mới session nếu không có

    // Kiểm tra xem session có tồn tại và có role là 'admin'
    if (session != null && "Admin".equals(session.getAttribute("role"))) {
        // Nếu role là admin, chuyển hướng tới trang Admin.jsp
        request.getRequestDispatcher("Admin.jsp").forward(request, response);
    } else {
        // Nếu không có session hoặc role không phải là admin, chuyển hướng về trang login
        response.sendRedirect("Login.jsp");  // Hoặc đường dẫn trang login của bạn
    }
}


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
