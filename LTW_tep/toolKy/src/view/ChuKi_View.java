package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setTitle("Chu Ki So - Digital Signature Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initUI();

        ChuKi_model model = new ChuKi_model(this);
        controller = new ChuKi_Controller(this, model);
        addEventHandlers();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        // Text input and output area
        textInput = new JTextArea(5, 40);
        textOutput = new JTextArea(5, 40);
        textOutput.setEditable(false);

        JScrollPane scrollInput = new JScrollPane(textInput);
        JScrollPane scrollOutput = new JScrollPane(textOutput);

        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        textPanel.add(scrollInput);
        textPanel.add(scrollOutput);

        // Hash and signature fields
        hashTextField = new JTextField();
        signatureTextField = new JTextField();

        // Key areas
        textPublicKey = new JTextArea(3, 30);
        textPrivateKey = new JTextArea(3, 30);
        JScrollPane scrollPublicKey = new JScrollPane(textPublicKey);
        JScrollPane scrollPrivateKey = new JScrollPane(textPrivateKey);

        // Algorithm selection
        comboAlgorithm = new JComboBox<>(new String[]{"SHA-256", "SHA-512"});

        // Buttons
        btnGenerateKey = new JButton("Generate Key");
        btnSign = new JButton("Sign");
        btnVerify = new JButton("Verify");
        btnChooseFile = new JButton("Choose File");
        btnSaveKey = new JButton("Save Keys");
        btnLoadKey = new JButton("Load Keys");
        btnReset = new JButton("Reset");

        // Top Panel Layout
        JPanel keyPanel = new JPanel(new GridLayout(2, 2));
        keyPanel.add(new JLabel("Public Key:"));
        keyPanel.add(scrollPublicKey);
        keyPanel.add(new JLabel("Private Key:"));
        keyPanel.add(scrollPrivateKey);
        topPanel.add(keyPanel);

        JPanel hashSignPanel = new JPanel(new GridLayout(2, 2));
        hashSignPanel.add(new JLabel("Hash:"));
        hashSignPanel.add(hashTextField);
        hashSignPanel.add(new JLabel("Signature:"));
        hashSignPanel.add(signatureTextField);
        topPanel.add(hashSignPanel);

        // Bottom Panel Layout
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4));
        buttonPanel.add(btnGenerateKey);
        buttonPanel.add(btnSign);
        buttonPanel.add(btnVerify);
        buttonPanel.add(btnChooseFile);
        buttonPanel.add(btnSaveKey);
        buttonPanel.add(btnLoadKey);
        buttonPanel.add(btnReset);
        buttonPanel.add(comboAlgorithm);

        bottomPanel.add(textPanel);
        bottomPanel.add(buttonPanel);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);

        add(panel);
    }

    private void addEventHandlers() {
        btnGenerateKey.addActionListener(e -> controller.generateKey(e));
        btnSign.addActionListener(e -> controller.sign(e));
        btnVerify.addActionListener(e -> controller.verify(e));
        btnChooseFile.addActionListener(e -> controller.chooseFile(e));
        btnSaveKey.addActionListener(e -> controller.saveKey(e));
        btnLoadKey.addActionListener(e -> controller.loadKey(e));
        btnReset.addActionListener(e -> controller.reset(e));
    }

    // Getters for components
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
