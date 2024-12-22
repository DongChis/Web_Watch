package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.DAOKey;
import entity.Alg_KEY;

@WebServlet(urlPatterns = { "/keyControl" })
@MultipartConfig
public class KeyControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println("Received action: " + action);

		if (action == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is missing");
			return;
		}

		switch (action) {
		case "generateKey":
			generateKey(request, response);
			break;
		case "importKey":
			importKey(request, response);
			break;
		case "reportKey":
			reportKey(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
			break;
		}
	}

	public void generateKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get the key size from the request
			String keySize = request.getParameter("keySize");

			if (keySize == null || keySize.isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key size is required");
				return;
			}

			// Create a new Alg_KEY object for generating the RSA key pair
			Alg_KEY algKey = new Alg_KEY();
			algKey.generateKey("RSA", Integer.parseInt(keySize));

			// Get the Base64 encoded public and private keys
			String publicKeyBase64 = algKey.getPublicKeyBase64();
			String privateKeyBase64 = algKey.getPrivateKeyBase64();

			// Get the session
			HttpSession session = request.getSession(false); // Use false to not create a new session
			if (session == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
				return;
			}

			Integer userIdObj = (Integer) session.getAttribute("userId");
			if (userIdObj == null) {
				// userID attribute is missing from session
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
				return;
			}

			int userId = userIdObj; // Get user ID from session

			// Calculate expiration time: 12 hours from the current time
			long createTimeMillis = System.currentTimeMillis();
			long expirationTimeMillis = createTimeMillis + (12 * 60 * 60 * 1000); // 12 hours in milliseconds

			// Format the expiration time (could use SimpleDateFormat or convert to a
			// readable format if needed)
			String expirationTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new java.util.Date(expirationTimeMillis));

			// Set the attributes to forward to the JSP
			request.setAttribute("publicKey", publicKeyBase64); // Public key for display
			request.setAttribute("privateKey", privateKeyBase64); // Private key (inform user to download it)
			request.setAttribute("createTime",
					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(createTimeMillis))); // Creation
																															// time
																															// in
																															// readable
																															// format
			request.setAttribute("endTime", expirationTime); // Expiration time, 12 hours after creation

			// Forward the request to the Function.jsp page to display the keys
			request.getRequestDispatcher("Function.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			// Handle the case where the key size is invalid
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid key size");
		} catch (Exception e) {
			// Handle any other exceptions
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating key");
		}
	}

	private void importKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Lấy thông tin khóa công khai từ yêu cầu
			String publicKey = request.getParameter("publicKey");

			// Lấy tệp khóa công khai từ yêu cầu
			Part filePart = request.getPart("publicKeyFile"); // Tải file khóa
			String fileName = null;
			String filePath = null;

			// Nếu có tệp được tải lên, lưu tệp vào thư mục uploads
			if (filePart != null && filePart.getSize() > 0) {
				// Lấy tên tệp và lưu tệp vào thư mục uploads
				fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
				String uploadDir = getServletContext().getRealPath("/") + "uploads"; // Đường dẫn tuyệt đối trên server

				// Đảm bảo thư mục uploads tồn tại
				File uploads = new File(uploadDir);
				if (!uploads.exists()) {
					uploads.mkdir(); // Tạo thư mục nếu không tồn tại
				}

				// Tạo file mới trên server và ghi dữ liệu tệp vào
				File file = new File(uploads, fileName);
				filePart.write(file.getAbsolutePath()); // Lưu file vào thư mục

				filePath = file.getAbsolutePath(); // Lấy đường dẫn tệp đã lưu trên server
				System.out.println("Đã lưu tệp khóa tại: " + filePath);

				// Đọc nội dung tệp để xử lý (giả sử tệp là văn bản chứa khóa công khai)
				String fileContent = readFile(filePath);
				System.out.println("Nội dung khóa công khai: " + fileContent);

				// Nếu có khóa công khai trong tệp, lưu vào cơ sở dữ liệu
				if (fileContent != null && !fileContent.isEmpty()) {
					// Giả sử bạn có phương thức lưu khóa vào cơ sở dữ liệu
					DAOKey daoKey = new DAOKey();
					Date createTime = new Date();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(createTime);
					calendar.add(Calendar.HOUR, 12); // Thời gian hết hạn là 12 giờ sau
					Date endTime = calendar.getTime();

					
					HttpSession session = request.getSession();
					Integer userIdObj = (Integer) session.getAttribute("userId");
					int userId = userIdObj != null ? userIdObj : -1;
					if (userId == -1) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Người dùng chưa đăng nhập.");
						return;
					}

					boolean isPublicKeySaved = daoKey.savePublicKey(userId, fileContent, createTime, endTime);
					if (!isPublicKeySaved) {
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lưu khóa công khai.");
						return;
					}
				}
			}

			// Kiểm tra nếu không có khóa công khai hoặc tệp khóa, trả về lỗi
			if ((publicKey == null || publicKey.isEmpty()) && (filePart == null || filePart.getSize() == 0)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Khóa công khai hoặc tệp khóa công khai là bắt buộc.");
				return;
			}

			// Lấy userId từ session
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("userId") == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Người dùng chưa đăng nhập.");
				return;
			}

			Integer userIdObj = (Integer) session.getAttribute("userId");
			int userId = userIdObj != null ? userIdObj : -1;
			if (userId == -1) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Người dùng chưa đăng nhập.");
				return;
			}

			// Lưu khóa công khai vào cơ sở dữ liệu nếu có
			if (publicKey != null && !publicKey.isEmpty()) {
				DAOKey daoKey = new DAOKey();
				Date createTime = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(createTime);
				calendar.add(Calendar.HOUR, 12); // Thời gian hết hạn là 12 giờ sau
				Date endTime = calendar.getTime();

				boolean isPublicKeySaved = daoKey.savePublicKey(userId, publicKey, createTime, endTime);
				if (!isPublicKeySaved) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lưu khóa công khai.");
					return;
				}
			}

			// Chuyển hướng đến trang kiểm soát khóa sau khi lưu thành công
			request.setAttribute("message", "Khóa công khai đã được lưu thành công!");
			response.sendRedirect("keyControl");

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xử lý thông tin khóa.");
		}
	}

	// Phương thức để đọc nội dung tệp
	private String readFile(String filePath) throws IOException {
		StringBuilder content = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		while ((line = reader.readLine()) != null) {
			content.append(line).append("\n");
		}
		reader.close();
		return content.toString();
	}

	private void reportKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String privateKeyReport = request.getParameter("reportPrivateKey");
			int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString()); // Lấy userId từ
																									// session

			// Cập nhật thời gian báo mất khóa
			DAOKey daoKey = new DAOKey();
			daoKey.reportLostKey(userId); // Truyền userId để xác định khóa

			// Chuyển tiếp tới trang kết quả
			response.sendRedirect("Function.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reporting lost key.");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Retrieve the session and userId
			HttpSession session = request.getSession(false); // Use false to not create a new session
			if (session == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
				return;
			}

			Integer userId = (Integer) session.getAttribute("userId");
			if (userId == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID not found in session.");
				return;
			}

			// Retrieve key information from DAO
			DAOKey daoKey = new DAOKey();
			Map<String, String> keyInfo = daoKey.getKeyInfo(userId);

			// If key information is not available, set default messages
			String publicKey = keyInfo.get("publicKey");
			String createTime = keyInfo.get("createTime");
			String endTime = keyInfo.get("endTime");

			// Check if key info exists, otherwise provide a default message
			publicKey = publicKey != null ? publicKey : "Chưa có khóa công khai";
			createTime = createTime != null ? createTime : "Chưa có thông tin";
			endTime = endTime != null ? endTime : "Chưa có thông tin";

			// Set the information in the request attributes for use in the JSP page
			request.setAttribute("publicKey", publicKey);
			request.setAttribute("createTime", createTime);
			request.setAttribute("endTime", endTime);

			// Forward to the Function.jsp page for display
			request.getRequestDispatcher("Function.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving key information.");
		}
	}

}
