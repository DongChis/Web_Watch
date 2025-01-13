package control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/admin/*", "/Admin.jsp"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter nếu cần
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Lấy session mà không tạo mới

        // Kiểm tra nếu session tồn tại và người dùng đã vào admin
        if (session != null) {
            Boolean hasAccessedAdmin = (Boolean) session.getAttribute("hasAccessedAdmin");
            Integer adminAccessCount = (Integer) session.getAttribute("adminAccessCount"); // Lấy số lần truy cập admin

            // Nếu chưa truy cập admin, khởi tạo số lần truy cập
            if (adminAccessCount == null) {
                adminAccessCount = 0;
                session.setAttribute("adminAccessCount", adminAccessCount);
            }

            // Kiểm tra xem người dùng đã vào admin bao nhiêu lần
            if (adminAccessCount >= 2) {
                // Nếu đã vào admin 2 lần, redirect đến trang chủ (chặn truy cập)
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
                return; // Dừng lại, không cho phép truy cập thêm
            }

            // Nếu chưa truy cập admin, cho phép vào và tăng số lần truy cập
            if ("Admin".equals(session.getAttribute("role"))) {
                session.setAttribute("hasAccessedAdmin", true); // Đánh dấu đã truy cập admin
                session.setAttribute("adminAccessCount", adminAccessCount + 1); // Tăng số lần truy cập lên
                chain.doFilter(request, response); // Cho phép truy cập
            } else {
                // Nếu không phải admin, chuyển hướng về login
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login.jsp");
            }
        } else {
            // Nếu session không tồn tại (chưa đăng nhập), chuyển hướng về login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login.jsp");
        }
    }
    @Override
    public void destroy() {
        // Dọn dẹp khi filter bị hủy nếu cần
    }
}
