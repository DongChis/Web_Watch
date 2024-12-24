package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.swing.JFileChooser;

import model.ChuKi_model;
import view.ChuKi_View;

public class ChuKi_Control {

	private ChuKi_View view;
	private ChuKi_model model;

	public ChuKi_Control(ChuKi_View view, ChuKi_model model) {
		this.view = view;
		this.model = model;
	}

	public void hashAndSignText() {
		try {
			// Step 1: Get input text
			String message = view.getTextInput().getText();
			if (message.isEmpty()) {
				view.getTextOutput().setText("Please enter text to hash and sign.");
				return;
			}

			// Step 2: Get selected hash algorithm
			String algorithm = (String) view.getComboAlgorithm().getSelectedItem();

			// Step 3: Generate hash
			String hash = model.generateHash(message, algorithm);

			view.getHashTextField().setText(hash); // Display hash in hash area

			// Step 4: Sign the hash with the RSA private key
			String signature = model.signMessage(message);
			view.getSignatureTextField().setText(signature); // Display the signature

			view.getTextOutput().setText("Kí thành công!");
		} catch (Exception e) {
			view.getTextOutput().setText("Error : " + e.getMessage());
		}
	}




	// Action methods
	public void chooseFile(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			StringBuilder content = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = reader.readLine()) != null) {
					content.append(line).append("\n");
				}

				view.getTextOutput().setText("File chosen: " + file.getAbsolutePath());

			} catch (IOException ex) {
				view.getTextOutput().setText("Error reading file: " + ex.getMessage());
			}
		}
	}

	// Updated methods in ChuKi_Control.java
	public void sign(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		try {
			// Lấy dữ liệu từ input và output
			String resultText = view.getTextOutput().getText();
			String inputText = view.getTextInput().getText();

			// Kiểm tra nếu không có input nào
			if (inputText.isEmpty() && (resultText.isEmpty() || !resultText.contains("File chosen: "))) {
				view.getTextOutput().setText("Chọn văn bản hoặc file");
				return;
			}

			// Ký chuỗi văn bản nếu có văn bản trong inputText
			if (!inputText.isEmpty()) {
				hashAndSignText(); // Hàm ký chuỗi văn bản

				return;
			}

			// Nếu không có văn bản, kiểm tra file đã chọn
			if (resultText.isEmpty() || !resultText.contains("File chosen: ")) {
				view.getTextOutput().setText("Chưa chọn file!");
				return;
			}

			// Lấy đường dẫn file từ resultText (giả sử resultText chứa thông tin như "File
			// chosen: <đường dẫn file>")
			String[] parts = resultText.split("File chosen:");
			String path = parts.length > 1 ? parts[1].trim() : ""; // Lấy đường dẫn sau "File chosen:"

			if (path.isEmpty()) {
				view.getTextOutput().setText("Chưa chọn file!");
				return; // Nếu không có đường dẫn file, trả về
			}
			// Step 2: Get selected hash algorithm
			String algorithm = (String) view.getComboAlgorithm().getSelectedItem();

			// Step 3: Generate hash
			String hash = model.generateHash(path, algorithm);

			String signature = model.signFile(path);
			;
			view.getSignatureTextField().setText(signature);
			System.out.println("chu ki cua file: " + signature);
			// Gọi hàm ký tệp

			view.getHashTextField().setText(hash);
			System.out.println("hashFile Text: " + hash);
			view.getTextOutput().setText("Kí thành công tệp:" + path);
			System.out.println("Tên file đã chọn: " + path);

		} catch (Exception ex) {
			// Xử lý lỗi
			view.getTextOutput().setText("Error: Filre null" + ex.getMessage());
		}
	}

	

	public void loadKey(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();

		
		// Load Private Key
		fileChooser.setDialogTitle("Load Private Key");
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String keyString = reader.readLine();
				view.getTextPrivateKey().setText(reader.readLine());
				view.getTextOutput().setText("Private Key loaded from: " + file.getAbsolutePath());
				 // Chuyển đổi chuỗi private key sang đối tượng PrivateKey
		        byte[] keyBytes = Base64.getDecoder().decode(keyString);
		        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		        // Set private key vào model
		        model.setPrivateKey(privateKey);
				
			} catch (IOException ex) {
				view.getTextOutput().setText("Error loading private key: " + ex.getMessage());
			}
		}
	}
	public void setPrivateKey() {
	    String keyString = view.getTextPrivateKey().getText();
	    if (keyString == null || keyString.trim().isEmpty()) {
	        view.getTextOutput().setText("No private key provided.");
	    } else {
	        try {
	            byte[] keyBytes = Base64.getDecoder().decode(keyString.trim());
	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
	            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

	            // Set private key vào model
	            model.setPrivateKey(privateKey);
	            view.getTextOutput().setText("Private Key set successfully.");
	        } catch (Exception ex) {
	            view.getTextOutput().setText("Invalid private key: " + ex.getMessage());
	        }
	    }
	}
	public void reset(ActionEvent e) {
		view.getTextOutput().setText("");
		view.getTextInput().setText("");
		view.getHashTextField().setText("");
		view.getSignatureTextField().setText("");
		//view.getTextPublicKey().setText("");
	}


}
