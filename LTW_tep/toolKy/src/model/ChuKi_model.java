package model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class ChuKi_model {
    private PrivateKey privateKey;
    private Signature signature;

    public ChuKi_model() {
        try {
            initSignature("SHA256", "RSA"); // Mặc định sử dụng SHA256 với RSA
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Khởi tạo đối tượng Signature
    public void initSignature(String hashAlgorithm, String algorithm) throws Exception {
        if (this.signature == null) {
            this.signature = Signature.getInstance(hashAlgorithm + "with" + algorithm);
        }
    }

    // Nhập private key từ chuỗi base64
    public void setPrivateKey(String privateKeyBase64) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        this.privateKey = (PrivateKey) java.security.KeyFactory.getInstance("RSA")
                .generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes));
    }

    // Ký văn bản
    public String signMessage(String message) throws Exception {
        byte[] data = message.getBytes();
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    // Ký file
    public String signFile(String filePath) throws Exception {
        signature.initSign(privateKey);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buff = new byte[1024];
        int read;
        while ((read = bis.read(buff)) != -1) {
            signature.update(buff, 0, read);
        }
        bis.close();
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }
}
