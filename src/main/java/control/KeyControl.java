package control;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    // Handle POST requests
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
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

    // Generate RSA key pair
    private void generateKey(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keySizeStr = request.getParameter("keySize");
            if (keySizeStr == null || keySizeStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key size is required");
                return;
            }

            int keySize;
            try {
                keySize = Integer.parseInt(keySizeStr);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid key size");
                return;
            }

            // Generate RSA keys
            Alg_KEY algKey = new Alg_KEY();
            algKey.generateKey("RSA", keySize);

            String publicKeyBase64 = algKey.getPublicKeyBase64();
            String privateKeyBase64 = algKey.getPrivateKeyBase64();

            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
                return;
            }

            Integer userIdObj = (Integer) session.getAttribute("userId");
            if (userIdObj == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }

            int userId = userIdObj;

            // Calculate expiration time (12 hours from now)
            long createTimeMillis = System.currentTimeMillis();
            long expirationTimeMillis = createTimeMillis + (12 * 60 * 60 * 1000); // 12 hours
            String expirationTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date(expirationTimeMillis));

            // Save keys in database
            DAOKey daoKey = new DAOKey();
            Date createTime = new Date(createTimeMillis);
            Date endTime = new Date(expirationTimeMillis);

            boolean isSaved = daoKey.savePublicKey(userId, publicKeyBase64, createTime, endTime);
            if (!isSaved) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving keys to database");
                return;
            }

            // Set the attributes to display on the JSP page
            request.setAttribute("publicKey", publicKeyBase64);
            request.setAttribute("privateKey", privateKeyBase64);
            request.setAttribute("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
            request.setAttribute("endTime", expirationTime);

            request.getRequestDispatcher("Function.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating key");
        }
    }

    private void importKey(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String publicKey = request.getParameter("publicKey");  // Retrieve public key from text input
            Part filePart = request.getPart("publicKeyFile");       // Retrieve file part from request

            // Check if public key or file is provided
            if ((publicKey == null || publicKey.isEmpty()) && (filePart == null || filePart.getSize() == 0)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Public Key or Public Key File is required");
                return;
            }

            String src = null;
            String publicKeyFileContent = null;

            // Handle file upload (if a file is provided)
            if (filePart != null && filePart.getSize() > 0) {
                // Define the directory to store uploaded files
                String uploadDir = getServletContext().getRealPath("/") + "uploads";
                File uploads = new File(uploadDir);
                if (!uploads.exists()) {
                    uploads.mkdir();  // Create the directory if it doesn't exist
                }

                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                File file = new File(uploads, fileName);
                filePart.write(file.getAbsolutePath());  // Write the file to the server's file system

                src = file.getAbsolutePath();  // Store the file path

                // Read the content of the public key file into a string
                publicKeyFileContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            }

            // If public key is provided in text input, use it directly
            if (publicKey != null && !publicKey.isEmpty()) {
                publicKeyFileContent = publicKey;
            }

            // Set up expiration time (12 hours from now)
            Date createTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createTime);
            calendar.add(Calendar.HOUR, 12);  // Set expiration time to 12 hours later
            Date endTime = calendar.getTime();

            // Check session and user login
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }

            Integer userIdObj = (Integer) session.getAttribute("userId");
            int userId = userIdObj != null ? userIdObj : -1;

            // Initialize DAO to interact with the database
            DAOKey daoKey = new DAOKey();
            boolean isPublicKeySaved = false;

            // Save the public key (either from file or input)
            if (publicKeyFileContent != null && !publicKeyFileContent.isEmpty()) {
                isPublicKeySaved = daoKey.savePublicKey(userId, publicKeyFileContent, createTime, endTime);
            }

            // If a public key file is uploaded, save the file path
            if (src != null) {
                boolean isPublicKeyFileSaved = daoKey.savePublicKeyFile(userId, src, createTime, endTime);
                if (!isPublicKeyFileSaved) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving Public Key File");
                    return;
                }
            }

            // Check if saving the public key was successful
            if (!isPublicKeySaved) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving Public Key");
                return;
            }

            // Redirect after saving the key to the database
            response.sendRedirect("keyControl");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving key information");
        }
    }



    // Report lost key (deactivate key)
    private void reportKey(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());


			if (!isEmailVerified) {
				// Nếu email chưa được xác minh, chuyển hướng đến trang yêu cầu xác minh email
				response.sendRedirect("home");
				return;
			}
			// Retrieve key information from DAO
			DAOKey daoKey = new DAOKey();
			Map<String, String> keyInfo = daoKey.getKeyInfo(userId);
			// If key information is not available, set default messages
			String publicKey = keyInfo.get("publicKey");
			String createTime = keyInfo.get("createTime");
			String endTime = keyInfo.get("endTime");

        }
            }
    
    // Handle GET requests (show key info)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
                return;
            }

            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID not found in session.");
                return;
            }

            DAOKey daoKey = new DAOKey();
            Map<String, String> keyInfo = daoKey.getKeyInfo(userId);

            String publicKey = keyInfo.get("publicKey");
            String createTime = keyInfo.get("createTime");
            String endTime = keyInfo.get("endTime");

            // Set default messages if no key information found
            publicKey = publicKey != null ? publicKey : "Chưa có khóa công khai";
            createTime = createTime != null ? createTime : "Chưa có thông tin";
            endTime = endTime != null ? endTime : "Chưa có thông tin";

            request.setAttribute("publicKey", publicKey);
            request.setAttribute("createTime", createTime);
            request.setAttribute("endTime", endTime);

            request.getRequestDispatcher("Function.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving key information.");
        }
    }
}
