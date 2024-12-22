package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChuKi_View extends JFrame {

    private JTextArea textInput;
    private JTextArea textOutput;
    private JComboBox<String> comboAlgorithm;
    private JTextField hashTextField;
    private JTextField signatureTextField;
    private JTextField textPublicKey;
    private JTextField textPrivateKey;

    public ChuKi_View() {
        setTitle("Chữ Ký Số - RSA");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel for input and output text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2, 1));
        
        textInput = new JTextArea(5, 40);
        textInput.setBorder(BorderFactory.createTitledBorder("Input Text"));
        JScrollPane inputScroll = new JScrollPane(textInput);
        
        textOutput = new JTextArea(5, 40);
        textOutput.setBorder(BorderFactory.createTitledBorder("Output"));
        JScrollPane outputScroll = new JScrollPane(textOutput);
        
        textPanel.add(inputScroll);
        textPanel.add(outputScroll);
        
        // Panel for hash and signature
        JPanel hashAndSignPanel = new JPanel();
        hashAndSignPanel.setLayout(new GridLayout(2, 2));
        
        JLabel hashLabel = new JLabel("Hash:");
        hashTextField = new JTextField();
        JLabel signatureLabel = new JLabel("Signature:");
        signatureTextField = new JTextField();
        
        hashAndSignPanel.add(hashLabel);
        hashAndSignPanel.add(hashTextField);
        hashAndSignPanel.add(signatureLabel);
        hashAndSignPanel.add(signatureTextField);
        
        // Panel for algorithm and keys
        JPanel algorithmPanel = new JPanel();
        algorithmPanel.setLayout(new FlowLayout());
        
        JLabel algorithmLabel = new JLabel("Select Hash Algorithm:");
        comboAlgorithm = new JComboBox<>(new String[]{"SHA-256", "SHA-512", "MD5"});
        
        JLabel publicKeyLabel = new JLabel("Public Key:");
        textPublicKey = new JTextField(30);
        textPublicKey.setEditable(false);
        
        JLabel privateKeyLabel = new JLabel("Private Key:");
        textPrivateKey = new JTextField(30);
        textPrivateKey.setEditable(false);
        
        algorithmPanel.add(algorithmLabel);
        algorithmPanel.add(comboAlgorithm);
        algorithmPanel.add(publicKeyLabel);
        algorithmPanel.add(textPublicKey);
        algorithmPanel.add(privateKeyLabel);
        algorithmPanel.add(textPrivateKey);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        JButton btnGenerateKey = new JButton("Generate Key");
        JButton btnSign = new JButton("Sign");
        JButton btnVerify = new JButton("Verify");
        JButton btnChooseFile = new JButton("Choose File");
        JButton btnSaveKey = new JButton("Save Key");
        JButton btnLoadKey = new JButton("Load Key");
        JButton btnReset = new JButton("Reset");
        
        buttonPanel.add(btnGenerateKey);
        buttonPanel.add(btnSign);
        buttonPanel.add(btnVerify);
        buttonPanel.add(btnChooseFile);
        buttonPanel.add(btnSaveKey);
        buttonPanel.add(btnLoadKey);
        buttonPanel.add(btnReset);

        // Add panels to frame
        add(textPanel, BorderLayout.NORTH);
        add(hashAndSignPanel, BorderLayout.CENTER);
        add(algorithmPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    public JTextArea getTextInput() {
        return textInput;
    }

    public JTextArea getTextOutput() {
        return textOutput;
    }

    public JComboBox<String> getComboAlgorithm() {
        return comboAlgorithm;
    }

    public JTextField getHashTextField() {
        return hashTextField;
    }

    public JTextField getSignatureTextField() {
        return signatureTextField;
    }

    public JTextField getTextPublicKey() {
        return textPublicKey;
    }

    public JTextField getTextPrivateKey() {
        return textPrivateKey;
    }
    
    // Method to add action listeners to buttons
    public void addGenerateKeyListener(ActionListener listener) {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component button : panel.getComponents()) {
                    if (button instanceof JButton && ((JButton) button).getText().equals("Generate Key")) {
                        ((JButton) button).addActionListener(listener);
                    }
                }
            }
        }
    }

    public void addSignListener(ActionListener listener) {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component button : panel.getComponents()) {
                    if (button instanceof JButton && ((JButton) button).getText().equals("Sign")) {
                        ((JButton) button).addActionListener(listener);
                    }
                }
            }
        }
    }

    public void addVerifyListener(ActionListener listener) {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component button : panel.getComponents()) {
                    if (button instanceof JButton && ((JButton) button).getText().equals("Verify")) {
                        ((JButton) button).addActionListener(listener);
                    }
                }
            }
        }
    }

    // Add other listener methods as needed for buttons like Choose File, Load Key, Save Key, etc.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChuKi_View view = new ChuKi_View();
            view.setVisible(true);
        });
    }
}
