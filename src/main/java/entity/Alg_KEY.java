package entity;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Alg_KEY {

    private KeyPair keyPair;

    // Tạo cặp khóa với thuật toán và kích thước key cụ thể
    public void generateKey(String algorithm, int keySize) throws Exception {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(keySize);
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new Exception("Error generating key: " + e.getMessage(), e);
        }
    }

    // Lấy PublicKey từ cặp khóa
    public PublicKey getPublicKey() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair has not been generated. Call generateKey() first.");
        }
        return keyPair.getPublic();
    }

    // Lấy PrivateKey từ cặp khóa
    public PrivateKey getPrivateKey() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair has not been generated. Call generateKey() first.");
        }
        return keyPair.getPrivate();
    }

    // Trả về PublicKey ở dạng Base64
    public String getPublicKeyBase64() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair has not been generated. Call generateKey() first.");
        }
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    // Trả về PrivateKey ở dạng Base64
    public String getPrivateKeyBase64() {
        if (keyPair == null) {
            throw new IllegalStateException("KeyPair has not been generated. Call generateKey() first.");
        }
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    // Lấy PublicKey từ chuỗi Base64
    public static PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new Exception("Error decoding PublicKey from Base64: " + e.getMessage(), e);
        }
    }

    // Lấy PrivateKey từ chuỗi Base64
    public static PrivateKey getPrivateKeyFromBase64(String base64PrivateKey) throws Exception {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new Exception("Error decoding PrivateKey from Base64: " + e.getMessage(), e);
        }
    }
}
