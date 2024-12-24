package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.ChuKi_Controller;
import model.ChuKi_model;

public class ChuKi_View extends JPanel{
	private JTextArea textInput,textOutput;
	   
    private JButton  buttonSign, buttonChooseFile ,buttonGenerateKey, buttonLoadKey, buttonSaveKey
    ,buttonVerify,buttonReset,btnCopy,btnSetKey;
    private JComboBox<String> comboAlgorithm;
    ChuKi_Controller chuki_control;
    ChuKi_model chuki_model;
	private JTextField textPublicKey,textPrivateKey,signatureTextField,hashTextField;
	private JFileChooser fileChooser;
    private JTextField messageField;



    public ChuKi_View() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        chuki_model = new ChuKi_model(this);
        chuki_control = new ChuKi_Controller(this,chuki_model);
        
        // Create top label
        JLabel titleLabel = new JLabel("Chữ kí điện tử");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Create panel for input and output fields at the top
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(1, 1, 10, 10));  // Change to 1 row, 1 column
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for input and output text
        JPanel panelIO = new JPanel();
        panelIO.setLayout(new GridLayout(1, 2, 10, 10));  // 1 row, 2 columns
        panelIO.setPreferredSize(new Dimension(600, 220));  // Reduced height for better fit
      
        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JLabel inputLabel = new JLabel("Input Text:");
        textInput = new JTextArea();
      
        textInput.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(textInput, BorderLayout.CENTER);

        // Output Panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        JLabel outputLabel = new JLabel("Result");
        textOutput = new JTextArea();
       
        textOutput.setEditable(false); // Make output field read-only
        textOutput.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(textOutput, BorderLayout.CENTER);

        panelIO.add(inputPanel);
        panelIO.add(outputPanel);

        panelTop.add(panelIO);
        add(panelTop, BorderLayout.NORTH);  
        
        JPanel panelInput = new JPanel();
		panelInput.setLayout(new GridLayout(3, 2, 10, 10));
		panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelKey = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelKey.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
		panelInput.add(panelKey);
//		panelKey.add(new JLabel("Public Key"));
//		textPublicKey = new JTextField(15);
//		textPublicKey.setPreferredSize(new Dimension(200, 30));
//		panelKey.add(textPublicKey);
		
	
		panelKey.add(new JLabel("Private Key"));
		textPrivateKey = new JTextField(15);
		textPrivateKey.setPreferredSize(new Dimension(200, 30));
		panelKey.add(textPrivateKey);
		
		panelKey.add(new JLabel("Mã băm"));
		hashTextField = new JTextField(15);
		hashTextField.setPreferredSize(new Dimension(200, 30));
		panelKey.add(hashTextField);
		
		panelKey.add(new JLabel("Chứ kí"));
		signatureTextField = new JTextField(15);
		signatureTextField.setPreferredSize(new Dimension(200, 30));
		panelKey.add(signatureTextField);
        
        JPanel panelbtn = new JPanel();    
        
        panelbtn.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        panelbtn.setBackground(Color.LIGHT_GRAY);
        panelbtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        // Algorithm selection ComboBox
        panelbtn.add(new JLabel("Thuật toán băm:"));
        comboAlgorithm = new JComboBox<>(new String[] { "MD5", "SHA-1","SHA-256", "SHA-512" });
        panelbtn.add(comboAlgorithm);
        
        
        btnCopy = new JButton("Copy");
        panelKey.add(btnCopy);
        
        buttonReset = new JButton("Reset");
        panelbtn.add(buttonReset);
        
        buttonSign = new JButton("Kí");
        panelbtn.add(buttonSign);
        btnSetKey = new JButton("Set Private Key");
        panelbtn.add(btnSetKey);
//        buttonVerify = new JButton("Kiểm tra");
//        panelbtn.add(buttonVerify);
//        
//        buttonGenerateKey = new JButton("Tạo Key");
//        panelbtn.add(buttonGenerateKey);

        buttonLoadKey = new JButton("Load Key");
        panelbtn.add(buttonLoadKey);
        

//        buttonSaveKey = new JButton("Lưu Key");
//        panelbtn.add(buttonSaveKey);
       

        // File chooser button
        buttonChooseFile = new JButton("Chọn File");
        panelbtn.add(buttonChooseFile);
        
        panelInput.add(panelbtn);
        
        // Add the input panel to the center of the main panel
        add(panelInput, BorderLayout.SOUTH);

        // File chooser setup
        fileChooser = new JFileChooser();

        // Action listeners
        buttonReset.addActionListener(chuki_control::reset);
        buttonSign.addActionListener(chuki_control::sign);
//        buttonVerify.addActionListener(chuki_control::verify);
//        buttonGenerateKey.addActionListener(chuki_control::generateKey);
        buttonLoadKey.addActionListener(e -> {
            try {
                chuki_control.loadKey(e);
            } catch (Exception ex) {
                // Hiển thị thông báo lỗi lên giao diện
                getTextOutput().setText("Error loading key: " + ex.getMessage());
            }
        });   
//        buttonSaveKey.addActionListener(chuki_control::saveKey);
        buttonChooseFile.addActionListener(chuki_control::chooseFile);
        btnSetKey.addActionListener(e -> chuki_control.setPrivateKey());

        
        btnCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the text from the JTextField
                String textToCopy = signatureTextField.getText();
                
                // Copy the text to the system clipboard
                StringSelection selection = new StringSelection(textToCopy);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                
                JFrame frame = new JFrame("Copy Text Example");
                // Optionally, show a message indicating that the text was copied
                JOptionPane.showMessageDialog(frame, "Text copied to clipboard!");
            }
        });
       
    }

    public JTextArea getTextInput() {
        return textInput;
    }

    public JTextArea getTextOutput() {
        return textOutput;
    }

    public JTextField getTextPublicKey() {
        return textPublicKey;
    }

    public JTextField getTextPrivateKey() {
        return textPrivateKey;
    }

    public JComboBox<String> getComboAlgorithm() {
        return comboAlgorithm;
    }

    public JButton getButtonSign() {
        return buttonSign;
    }
    public JButton getButtonReset() {
        return buttonReset;
    }

    public JButton getButtonGenerateKey() {
        return buttonGenerateKey;
    }

    public JButton getButtonVerify() {
        return buttonVerify;
    }

    public JButton getButtonLoadKey() {
        return buttonLoadKey;
    }
    public JButton getBtnCopy() {
        return btnCopy;
    }

    public JButton getButtonSaveKey() {
        return buttonSaveKey;
    }

    public JButton getButtonChooseFile() {
        return buttonChooseFile;
    }
 // Getter and Setter for fileChooser
    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    // Getter and Setter for messageField
    public JTextField getMessageField() {
        return messageField;
    }

    public void setMessageField(JTextField messageField) {
        this.messageField = messageField;
    }

    // Getter and Setter for hashArea
    public JTextField getHashTextField() {
        return hashTextField;
    }
  

    // Getter and Setter for signatureArea
    public JTextField getSignatureTextField() {  	
        return signatureTextField;
    }
    

  

}
