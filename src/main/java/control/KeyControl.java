package control;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Alg_KEY;

@WebServlet(urlPatterns = {"/keyControl"})
public class KeyControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public KeyControl() {
        super();
    }

  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in (example check, adjust based on your session management)
        if (request.getSession().getAttribute("accSession") == null) {
            // Redirect to login page if not logged in
            response.sendRedirect("login.jsp");
            return;
        }

        // Forward to Function.jsp for authorized users
        request.getRequestDispatcher("Function.jsp").forward(request, response);
    }

    // Handle POST requests for processing the form submissions (key generation, import, report)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "generateKey":
                    handleGenerateKey(request, response);
                    break;
                case "importKey":
                    handleImportKey(request, response);
                    break;
                case "reportKey":
                    handleReportKey(request, response);
                    break;
                default:
                    response.sendRedirect("Function.jsp");
            }
        } else {
            response.sendRedirect("Function.jsp");
        }
    }
    private void handleGenerateKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String algorithm = "RSA";  // Algorithm can be dynamic if needed
        int keySize = Integer.parseInt(request.getParameter("keySize"));

        // Create an instance of Alg_KEY class to generate keys
        Alg_KEY algKey = new Alg_KEY();
        algKey.generateKey(algorithm, keySize);

        // Retrieve generated keys in Base64
        String publicKeyBase64 = Base64.getEncoder().encodeToString(algKey.getPublicKey().getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(algKey.getPrivateKey().getEncoded());

        // Get the current date and time for creation time
        Calendar calendar = Calendar.getInstance();
        Date createdTime = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdTimeStr = sdf.format(createdTime);

        // Set the expiration time to be 12 hours after creation time
        calendar.add(Calendar.HOUR, 12);
        Date expirationTime = calendar.getTime();
        String expirationTimeStr = sdf.format(expirationTime);

        // Save generated keys and times into request attributes for JSP display
        request.setAttribute("publicKey", publicKeyBase64);
        request.setAttribute("privateKey", privateKeyBase64);
        request.setAttribute("createdTime", createdTimeStr);
        request.setAttribute("expirationTime", expirationTimeStr);

        // Forward to the same page with the generated keys and times
        request.getRequestDispatcher("Function.jsp").forward(request, response);
    }
    // Handle key import logic (both file upload and text input)
    private void handleImportKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String privateKeyText = request.getParameter("privateKeyText");
        // Logic to handle file upload, for example, extracting keys from uploaded file
        if (privateKeyText != null && !privateKeyText.isEmpty()) {
            // Process private key text here
        }

        // Redirect or forward to Function.jsp based on success/failure
        request.setAttribute("message", "Key imported successfully");
        request.getRequestDispatcher("Function.jsp").forward(request, response);
    }

    // Handle reporting of exposed private key
    private void handleReportKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reportedKey = request.getParameter("reportPrivateKey");
        // Logic to report or handle the exposed key
        // For now, just display a message (adjust as needed)
        request.setAttribute("message", "Private key reported successfully");
        request.getRequestDispatcher("Function.jsp").forward(request, response);
    }
}
