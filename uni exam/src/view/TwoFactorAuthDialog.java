package view;

import security.TwoFactorAuth;
import util.SecurityUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import com.google.zxing.common.BitMatrix;

/**
 * Dialog for handling 2FA setup and verification
 */
public class TwoFactorAuthDialog extends JDialog {
    private JTextField codeField;
    private String secret;
    private boolean isVerified = false;

    /**
     * Constructor for 2FA setup
     * @param parent Parent frame
     * @param username Username for QR code generation
     */
    public TwoFactorAuthDialog(JFrame parent, String username) {
        super(parent, "Two-Factor Authentication Setup", true);
        this.secret = TwoFactorAuth.generateSecretKey();
        initializeSetupUI(username);
    }

    /**
     * Constructor for 2FA verification
     * @param parent Parent frame
     * @param secret User's 2FA secret
     */
    public TwoFactorAuthDialog(JFrame parent, String secret) {
        super(parent, "Two-Factor Authentication", true);
        this.secret = secret;
        initializeVerificationUI();
    }

    /**
     * Initializes the UI for 2FA setup
     * @param username Username for QR code generation
     */
    private void initializeSetupUI(String username) {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 500);
        setLocationRelativeTo(getParent());

        // Instructions panel
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Set up Two-Factor Authentication");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea instructions = new JTextArea(
            "1. Install Google Authenticator or Authy on your mobile device\n" +
            "2. Scan the QR code below with the app\n" +
            "3. Enter the 6-digit code shown in the app"
        );
        instructions.setEditable(false);
        instructions.setWrapStyleWord(true);
        instructions.setLineWrap(true);
        instructions.setBackground(null);
        instructions.setFont(new Font("Arial", Font.PLAIN, 12));
        
        instructionsPanel.add(titleLabel);
        instructionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        instructionsPanel.add(instructions);

        // QR Code panel
        JPanel qrPanel = new JPanel(new BorderLayout());
        try {
            BitMatrix qrCode = TwoFactorAuth.generateQRCode(username, "UniversityExamSystem", secret);
            JLabel qrLabel = new JLabel(new ImageIcon(toBufferedImage(qrCode)));
            qrPanel.add(qrLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            qrPanel.add(new JLabel("Error generating QR code"), BorderLayout.CENTER);
        }

        // Code entry panel
        JPanel codePanel = new JPanel(new FlowLayout());
        codeField = new JTextField(10);
        JButton verifyButton = new JButton("Verify");
        verifyButton.addActionListener(e -> verifyCode());
        
        codePanel.add(new JLabel("Enter code: "));
        codePanel.add(codeField);
        codePanel.add(verifyButton);

        // Add all panels
        add(instructionsPanel, BorderLayout.NORTH);
        add(qrPanel, BorderLayout.CENTER);
        add(codePanel, BorderLayout.SOUTH);
    }

    /**
     * Initializes the UI for 2FA verification
     */
    private void initializeVerificationUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(300, 150);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel titleLabel = new JLabel("Enter Authentication Code");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Code entry
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Code:"), gbc);

        codeField = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(codeField, gbc);

        // Verify button
        JButton verifyButton = new JButton("Verify");
        verifyButton.addActionListener(e -> verifyCode());
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(verifyButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Verifies the entered 2FA code
     */
    private void verifyCode() {
        String code = codeField.getText().trim();
        if (TwoFactorAuth.verifyTOTP(secret, code)) {
            isVerified = true;
            JOptionPane.showMessageDialog(this, 
                "Verification successful!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid code. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            codeField.setText("");
        }
    }

    /**
     * Converts BitMatrix to BufferedImage for QR code display
     * @param matrix BitMatrix from QR code generation
     * @return BufferedImage for display
     */
    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * @return true if the code was verified successfully
     */
    public boolean isVerified() {
        return isVerified;
    }

    /**
     * @return the generated secret key
     */
    public String getSecret() {
        return secret;
    }
}
