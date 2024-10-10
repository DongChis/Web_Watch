package control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


<<<<<<< HEAD
@WebFilter(urlPatterns = {"/admin/*","/Admin.jsp/*"}) 
=======
@WebFilter(urlPatterns = {"/admin/*" ,"/Admin.jsp/*" }) 
>>>>>>> 1f33197fc2da5113bc4a440167b2e99aa83622c5
public class AdminFilter implements Filter  {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		 HttpServletRequest httpRequest = (HttpServletRequest) request;
	     HttpServletResponse httpResponse = (HttpServletResponse) response;
	     
	     HttpSession session = httpRequest.getSession(false);// lấy session mới và tham số false để tránh tạo sesion mới
	     
	     if (session != null && "Admin".equals(session.getAttribute("role"))) {
	    	 chain.doFilter(request, response);
	     }else {
	    	 httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
		}
	}
	
}
