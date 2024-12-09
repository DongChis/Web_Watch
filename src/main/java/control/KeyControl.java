package control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.DAOKey;
import entity.Alg_KEY;


@WebServlet(urlPatterns = {"/keyControl"})
public class KeyControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
       
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is missing");
            return;  // Dừng xử lý nếu không có action
        }
        
        switch(action) {
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

    public void generateKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            HttpSession session = request.getSession(false);  // Use false to not create a new session
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

            int userId = Integer.parseInt(userIdObj.toString()); // Get user ID from session

            // Save the public key to the database
            DAOKey daoKey = new DAOKey();
            daoKey.savePublicKey(userId, publicKeyBase64);

            // Calculate expiration time (optional, for now just set as null or calculate)
            String expirationTime = "null";  // You can calculate the expiration time as needed

            // Set the attributes to forward to the JSP
            request.setAttribute("publicKey", publicKeyBase64);  // Public key for display
            request.setAttribute("privateKey", privateKeyBase64); // Private key (inform user to download it)
            request.setAttribute("createdTime", System.currentTimeMillis());  // Timestamp when the key was created
            request.setAttribute("expirationTime", expirationTime);  // Set expiration if necessary

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




    private void importKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Part privateKeyPart = request.getPart("privateKeyFile");
            String privateKeyText = request.getParameter("privateKeyText");

            // Đọc khóa private từ file hoặc từ form
            String privateKey = "";
            if (privateKeyPart != null) {
                privateKey = new String(Files.readAllBytes(new File(privateKeyPart.getSubmittedFileName()).toPath()));
            } else if (privateKeyText != null) {
                privateKey = privateKeyText;
            }

            if (!privateKey.isEmpty()) {
                // Lưu khóa private vào cơ sở dữ liệu
                DAOKey daoKey = new DAOKey();
                int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString()); // Lấy userId từ session
                daoKey.savePrivateKey(userId, privateKey);  // Sửa lại để truyền thêm userId
            }

            // Chuyển tiếp tới trang kết quả
            response.sendRedirect("Function.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error importing key.");
        }
    }

    private void reportKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String privateKeyReport = request.getParameter("reportPrivateKey");
            int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString()); // Lấy userId từ session

            // Cập nhật thời gian báo mất khóa
            DAOKey daoKey = new DAOKey();
            daoKey.reportLostKey(userId);  // Truyền userId để xác định khóa

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
            HttpSession session = request.getSession(false);  // Use false to not create a new session
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
