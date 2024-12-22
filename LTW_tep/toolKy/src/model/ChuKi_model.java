package model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import view.ChuKi_View;

public class ChuKi_model {
	private PublicKey publicKey;
	private PrivateKey privateKey;
    private KeyPair keyPair;
    private SecureRandom secureRandom;
    private Signature signature;
    ChuKi_View view;
    public ChuKi_model(ChuKi_View view) {
        this.view = view; // Initialize the view when the model is created
    }
    public ChuKi_model() {
    	
    }
    public void generateKey(String algorithm) throws Exception {
    	if (this.keyPair == null) {
            if (this.secureRandom == null) {
                this.secureRandom = new SecureRandom();
            }
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(2048, secureRandom); // 2048-bit key size
            this.keyPair = keyPairGenerator.generateKeyPair();

            // Initialize Signature object
            initSignature("SHA256", "RSA");
        }
    }

   
//ký văn bản
    public String signMessage(String mess) throws Exception {
    	initSignature("SHA256", "RSA");
 		byte[]data = mess.getBytes();
 		privateKey = keyPair.getPrivate();
 		signature.initSign(privateKey);
 		signature.update(data);
 		byte[] sign = signature.sign();
 		return Base64.getEncoder().encodeToString(sign);
 	}
    
    // Initialize Signature object with specified hash algorithm and provider
    public void initSignature(String hashAlgorithm, String algorithm) throws Exception {
        if (this.signature == null) {
            this.signature = Signature.getInstance(hashAlgorithm + "with" + algorithm);
        }
        this.secureRandom = new SecureRandom(); // Initialize SecureRandom
    }
    
    


    public String generateHash(String message, String algorithm) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }
    public String hashFile(String path, String algorithm) {
        try (InputStream fis = new FileInputStream(path)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] byteArray = new byte[1024];
            int bytesRead;

            // Read file data in chunks and update the digest
            while ((bytesRead = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesRead);
            }

            // Convert the hash bytes to a hexadecimal format
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            System.out.println("Error while hashing file: " + e.getMessage());
            return null;
        }
    }
    
    
    public boolean verifySignature (String message, String signatureString) throws InvalidKeyException, SignatureException {
    	publicKey = keyPair.getPublic();
 		signature.initVerify(publicKey);
 		byte[] data = message.getBytes();
 		byte[] signValue = Base64.getDecoder().decode(signatureString);
 		signature.update(data);
 		return signature.verify(signValue);
 		
 	}

   
//    ký file
    public String signFile(String src) throws Exception {
    	initSignature("SHA256", "RSA");
 		byte[] data = src.getBytes();
 		privateKey = keyPair.getPrivate();
 		signature.initSign(privateKey);
 		
 		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
 		byte[] buff = new byte[1024];
 		int read;
 		while((read = bis.read(buff)) != -1) {
 			signature.update(buff,0,read);
 		}
 		bis.close();
 		byte[]sign = signature.sign();
 		return Base64.getEncoder().encodeToString(sign);
 	}
//xác minh file
    public boolean verifyFile(String src, String sign) throws IOException, InvalidKeyException, SignatureException {
 		
    	publicKey = keyPair.getPublic();
    	signature.initVerify(publicKey);
	
 		byte[] data = src.getBytes();
 		byte[] signValue = Base64.getDecoder().decode(sign);
 		
 		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
 		byte[] buff = new byte[1024];
 		int read;
 		while((read = bis.read(buff)) != -1) {
 			signature.update(buff,0,read);
 		}
 		return signature.verify(signValue);
 	}

    // Phương thức trả về PublicKey
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    // Phương thức trả về PrivateKey
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

}
