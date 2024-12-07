package entity;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.swing.JOptionPane;

public class Alg_KEY {
	
	private  PublicKey publicKey;
	private  PrivateKey privateKey;
	private KeyPair keyPair;
	
	public  void generateKey(String algorithm, int keySize) {
		try {
			// Khởi tạo KeyPairGenerator và tạo cặp khóa
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
			keyPairGenerator.initialize(keySize);
			 keyPair = keyPairGenerator.generateKeyPair();
			
			 publicKey = keyPair.getPublic();
			 privateKey = keyPair.getPrivate();

			String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());		
		

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error generating key: " + e.getMessage());
		}
	}
	

	// Chuyển chuỗi Base64 thành PublicKey
	public  PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
		byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	public  PrivateKey getPrivateKeyFromBase64(String base64) throws Exception {
		byte[] decodedKey = Base64.getDecoder().decode(base64);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}
	
	public PublicKey getPublicKey() {
		return keyPair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}

}
