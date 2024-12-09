package control;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

            // Get the session
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

            int userId = userIdObj; // Get user ID from session

           

            // Calculate expiration time: 12 hours from the current time
            long createTimeMillis = System.currentTimeMillis();
            long expirationTimeMillis = createTimeMillis + (12 * 60 * 60 * 1000);  // 12 hours in milliseconds

            // Format the expiration time (could use SimpleDateFormat or convert to a readable format if needed)
            String expirationTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(expirationTimeMillis));

            // Set the attributes to forward to the JSP
            request.setAttribute("publicKey", publicKeyBase64);  // Public key for display
            request.setAttribute("privateKey", privateKeyBase64); // Private key (inform user to download it)
            request.setAttribute("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(createTimeMillis)));  // Creation time in readable format
            request.setAttribute("endTime", expirationTime);  // Expiration time, 12 hours after creation

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
            // Get the key information from the request
            String publicKey = request.getParameter("publicKey");
            String createTimeString = request.getParameter("createTime");
            String endTimeString = request.getParameter("endTime");

            // Parse the createTime and endTime strings into Date objects
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Adjust the format if necessary
            Date createTime = null;
            Date endTime = null;

            // Parse createTime
            try {
                if (createTimeString != null && !createTimeString.isEmpty()) {
                    createTime = sdf.parse(createTimeString); // Convert String to Date
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid createTime format");
                return;
            }

            // Parse endTime
            try {
                if (endTimeString != null && !endTimeString.isEmpty()) {
                    endTime = sdf.parse(endTimeString); // Convert String to Date
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endTime format");
                return;
            }

            // Check if createTime is null after parsing
            if (createTime == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Create Time is missing or invalid");
                return;
            }

            // Check if endTime is null after parsing
            if (endTime == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "End Time is missing or invalid");
                return;
            }

            // Debugging: Check if publicKey is null or empty
            if (publicKey == null || publicKey.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Public Key is missing or empty");
                return;
            }

            // Retrieve userId from the session
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }

            Integer userIdObj = (Integer) session.getAttribute("userId");
            int userId = userIdObj != null ? userIdObj : -1; // Default to -1 if userId is not found

            if (userId == -1) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }

            // Save the key information into the database
            DAOKey daoKey = new DAOKey();
            boolean isPublicKeySaved = daoKey.savePublicKey(userId, publicKey, createTime, endTime);  // Implement this method in your DAO

            if (!isPublicKeySaved) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving Public Key");
                return;
            }
            else {
                request.setAttribute("message", "Khóa đã được lưu thành công!");
			}

            // Redirect or forward after saving the data
            response.sendRedirect("keyControl");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving key information");
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
