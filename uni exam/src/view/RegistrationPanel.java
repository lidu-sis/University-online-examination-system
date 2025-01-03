package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class RegistrationPanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(246, 241, 241);
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(176, 196, 222);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 16);

    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private SimpleLandingPage parent;

    public RegistrationPanel(SimpleLandingPage parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 40, 20, 40));
        initializeUI();
    }

    private void initializeUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Title
        JLabel titleLabel = new JLabel("Registration", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        // Form fields
        fullNameField = new JTextField(20);
        emailField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        String[] roles = {"Student", "Instructor", "Administrator"};
        roleComboBox = new JComboBox<>(roles);

        // Buttons
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");
        styleButton(registerButton);
        styleButton(backButton);

        // Add components
        add(titleLabel, gbc);
        add(Box.createVerticalStrut(20), gbc);
        add(createFieldPanel("Full Name:", fullNameField), gbc);
        add(createFieldPanel("Email:", emailField), gbc);
        add(createFieldPanel("Username:", usernameField), gbc);
        add(createFieldPanel("Password:", passwordField), gbc);
        add(createFieldPanel("Confirm Password:", confirmPasswordField), gbc);
        add(createFieldPanel("Role:", roleComboBox), gbc);
        add(Box.createVerticalStrut(20), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        add(buttonPanel, gbc);

        // Add action listeners
        registerButton.addActionListener(e -> handleRegistration());
        backButton.addActionListener(e -> parent.showLoginPanel());
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(NORMAL_FONT);
        label.setPreferredSize(new Dimension(150, 25));
        
        panel.add(label);
        panel.add(field);
        
        return panel;
    }

    private void styleButton(JButton button) {
        button.setFont(NORMAL_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    private void handleRegistration() {
        // Get form values
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        // Validate input
        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: Add actual registration logic here
        JOptionPane.showMessageDialog(this,
            "Registration successful! Please login with your credentials.",
            "Registration Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Clear fields
        clearFields();
        
        // Return to login
        parent.showLoginPanel();
    }

    private void clearFields() {
        fullNameField.setText("");
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }
}