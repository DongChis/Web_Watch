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
	
	/**
	 * Servlet implementation class LoginControl
	 */
	@WebServlet(name = "LoginControl" , urlPatterns = {"/login"})
	public class LoginControl extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public LoginControl() {
	        super();
	        // TODO Auto-generated constructor stub
	    }
	
		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html;charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			String userName = request.getParameter("user");
			String password = request.getParameter("pass");
			System.out.println(userName);
			System.out.println(password);
			
			User acc = DAO.getInstance().login(userName,password);
			System.out.println(acc);
			if(acc == null) {
				request.setAttribute("mess","Wrong user or password");
				request.getRequestDispatcher("Login.jsp").forward(request, response);
			}else {
				HttpSession session = request.getSession();
				session.setAttribute("accSession", acc);			
				response.sendRedirect("home");
			}
		}
	
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			doGet(request, response);
		}
	
	}
