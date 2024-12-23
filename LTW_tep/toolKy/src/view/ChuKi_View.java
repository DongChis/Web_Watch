package view;

import java.awt.*;
import javax.swing.*;

import controller.ChuKi_Controller;
import model.ChuKi_model;

public class ChuKi_View extends JFrame {

    private JTextArea textInput;
    private JTextArea textOutput;
    private JTextField hashTextField;
    private JTextField signatureTextField;
    private JTextArea textPublicKey;
    private JTextArea textPrivateKey;
    private JComboBox<String> comboAlgorithm;

    private JButton btnGenerateKey;
    private JButton btnSign;
    private JButton btnVerify;
    private JButton btnChooseFile;
    private JButton btnSaveKey;
    private JButton btnLoadKey;
    private JButton btnReset;

    private ChuKi_Controller controller;

    public ChuKi_View() {
        setTitle("Digital Signature Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        initUI();

        ChuKi_model model = new ChuKi_model(this);
        controller = new ChuKi_Controller(this, model);
        addEventHandlers();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(20, 20)); // Add spacing between components
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the main panel

        // Fonts and Colors
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFont = new Font("Arial", Font.PLAIN, 14);

        // Text input and output area
        textInput = new JTextArea(10, 30);
        textOutput = new JTextArea(10, 30);
        textOutput.setEditable(false);
        textInput.setFont(textFont);
        textOutput.setFont(textFont);

        JScrollPane scrollInput = new JScrollPane(textInput);
        JScrollPane scrollOutput = new JScrollPane(textOutput);

        JPanel textPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // Space between input/output panels
        textPanel.add(createTitledPanel("Input Text", scrollInput, labelFont));
        textPanel.add(createTitledPanel("Output", scrollOutput, labelFont));

        // Hash and signature fields
        hashTextField = new JTextField();
        signatureTextField = new JTextField();
        hashTextField.setFont(textFont);
        signatureTextField.setFont(textFont);

        // Key areas
        textPublicKey = new JTextArea(5, 30);
        textPrivateKey = new JTextArea(5, 30);
        JScrollPane scrollPublicKey = new JScrollPane(textPublicKey);
        JScrollPane scrollPrivateKey = new JScrollPane(textPrivateKey);
        textPublicKey.setFont(textFont);
        textPrivateKey.setFont(textFont);

        // Buttons
        btnGenerateKey = createButton("Generate Key", labelFont);
        btnSign = createButton("Sign", labelFont);
        btnVerify = createButton("Verify", labelFont);
        btnChooseFile = createButton("Choose File", labelFont);
        btnSaveKey = createButton("Save Keys", labelFont);
        btnLoadKey = createButton("Load Keys", labelFont);
        btnReset = createButton("Reset", labelFont);
        comboAlgorithm = new JComboBox<>(new String[]{"SHA-256", "SHA-512"});
        comboAlgorithm.setFont(labelFont);

        // Top Panel: Key and Hash/Signature
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        topPanel.add(createTitledPanel("Public Key", scrollPublicKey, labelFont));
        topPanel.add(createTitledPanel("Private Key", scrollPrivateKey, labelFont));

        // Bottom Panel: Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 20, 20)); // Organize buttons
        buttonPanel.add(btnGenerateKey);
        buttonPanel.add(btnSign);
        buttonPanel.add(btnVerify);
        buttonPanel.add(btnChooseFile);
        buttonPanel.add(btnSaveKey);
        buttonPanel.add(btnLoadKey);
        buttonPanel.add(btnReset);
        buttonPanel.add(comboAlgorithm);

        // Main Layout
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private JPanel createTitledPanel(String title, JComponent component, Font titleFont) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setFont(titleFont);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title));
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(70, 130, 180)); // Steel blue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void addEventHandlers() {
        btnGenerateKey.addActionListener(controller::generateKey);
        btnSign.addActionListener(controller::sign);
        btnVerify.addActionListener(controller::verify);
        btnChooseFile.addActionListener(controller::chooseFile);
        btnSaveKey.addActionListener(controller::saveKey);
        btnLoadKey.addActionListener(controller::loadKey);
        btnReset.addActionListener(controller::reset);
    }

    // Getters
    public JTextArea getTextInput() {
        return textInput;
    }

    public JTextArea getTextOutput() {
        return textOutput;
    }

    public JTextField getHashTextField() {
        return hashTextField;
    }

    public JTextField getSignatureTextField() {
        return signatureTextField;
    }

    public JTextArea getTextPublicKey() {
        return textPublicKey;
    }

    public JTextArea getTextPrivateKey() {
        return textPrivateKey;
    }

    public JComboBox<String> getComboAlgorithm() {
        return comboAlgorithm;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChuKi_View view = new ChuKi_View();
            view.setVisible(true);
        });
    }
}
