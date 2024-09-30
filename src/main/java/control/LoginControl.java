package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import entity.User;

@WebServlet(name = "LoginControl", urlPatterns = { "/login" })
public class LoginControl extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("user");
		String password = request.getParameter("pass");

		HttpSession session = request.getSession();
		System.out.println(userName);
		System.out.println(password);


		User acc = DAO.getInstance().login(userName, password);
	
		if (acc == null) {
			request.setAttribute("mess", "Wrong user or password");
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		} else {
				session.setAttribute("accSession", acc);
				session.setAttribute("role", acc.getRole());
				response.sendRedirect("admin");
				
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
