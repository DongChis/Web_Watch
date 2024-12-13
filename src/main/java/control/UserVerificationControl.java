package control;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAOKey;

@WebServlet(name = "UserVerificationControl", urlPatterns = { "/userVerificationControl" })
public class UserVerificationControl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in again.");
            return;
        }
        
        String userIdString = String.valueOf(session.getAttribute("userId"));

        if (userIdString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is missing.");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdString);
            DAOKey daoKey = DAOKey.getInstance();
            Map<String, String> keyInfo = daoKey.getKeyInfo(userId);

            if (keyInfo != null && !keyInfo.isEmpty()) {
                request.setAttribute("publicKey", keyInfo.get("publicKey"));
                request.setAttribute("createTime", keyInfo.get("createTime"));
                request.setAttribute("endTime", keyInfo.get("endTime"));
            } else {
                request.setAttribute("error", "No public key information found.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid User ID.");
        }

        String signatureText = request.getParameter("signatureText");
        String signatureOption = request.getParameter("signatureOption");
        System.out.println("signatureOption "+ signatureOption);
        String publicKeyString = (String) request.getAttribute("publicKey");

        if (publicKeyString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Public Key is missing.");
            return;
        }

        boolean isVerified = false;
        String verificationMessage = "Signature verification failed.";
        PublicKey publicKey = null;

      
            try {
				publicKey = getPublicKeyFromString(publicKeyString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
           isVerified = verifySignatureWithPublicKey(publicKey, "DONG CHI", request);
            System.out.println(isVerified);
      

        if (isVerified) {
            verificationMessage = "Signature verification successful.";
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(verificationMessage);
    }

    private PublicKey getPublicKeyFromString(String publicKeyString) throws Exception {
        try {
            publicKeyString = publicKeyString.replaceAll("\\s", "");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid public key or not Base64 encoded", e);
        }
    }

    private boolean verifySignatureWithPublicKey(PublicKey publicKey, String data, HttpServletRequest request) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            String clientSignatureStr = request.getParameter("signatureText");
            System.out.println("clientSignatureStr " + clientSignatureStr);
            byte[] clientSignature = Base64.getDecoder().decode(clientSignatureStr);
            return signature.verify(clientSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
    }
}
