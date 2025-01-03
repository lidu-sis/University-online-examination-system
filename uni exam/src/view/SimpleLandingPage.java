package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Map;

public class SimpleLandingPage extends JFrame {
    // Colors
    private static final Color BACKGROUND_COLOR = new Color(246, 241, 241);
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(176, 196, 222);
    private static final Color ACCENT_COLOR = new Color(255, 140, 0);
    
    // Fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 32);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 16);
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private RegistrationPanel registrationPanel;
    
    // Store login fields for each role
    private Map<String, JTextField> usernameFields;
    private Map<String, JPasswordField> passwordFields;

    public SimpleLandingPage() {
        setTitle("University Online Examination System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Initialize the maps
        usernameFields = new HashMap<>();
        passwordFields = new HashMap<>();
        
        initializeUI();
    }

    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        // Add panels
        contentPanel.add(createWelcomePanel(), "WELCOME");
        contentPanel.add(createLoginPanel("Student"), "STUDENT");
        contentPanel.add(createLoginPanel("Instructor"), "INSTRUCTOR");
        contentPanel.add(createLoginPanel("Administrator"), "ADMINISTRATOR");
        contentPanel.add(createRulesPanel(), "RULES");

        // Create and add registration panel
        registrationPanel = new RegistrationPanel(this);
        contentPanel.add(registrationPanel, "REGISTER");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Show welcome panel initially
        cardLayout.show(contentPanel, "WELCOME");
    }

    private JPanel createLoginPanel(String role) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(role + " Login", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        // Create and store the fields
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        usernameFields.put(role, usernameField);
        passwordFields.put(role, passwordField);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        styleButton(loginButton);
        styleButton(registerButton);
        styleButton(backButton);

        backButton.addActionListener(e -> showPanel("WELCOME"));
        loginButton.addActionListener(e -> handleLogin(role));
        registerButton.addActionListener(e -> showPanel("REGISTER"));

        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(30), gbc);
        gbc.gridy = 2;
        panel.add(createFieldPanel("Username:", usernameField), gbc);
        gbc.gridy = 3;
        panel.add(createFieldPanel("Password:", passwordField), gbc);
        gbc.gridy = 4;
        panel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy = 5;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void handleLogin(String role) {
        JTextField usernameField = usernameFields.get(role);
        JPasswordField passwordField = passwordFields.get(role);
        
        if (usernameField == null || passwordField == null) {
            JOptionPane.showMessageDialog(this,
                "System error: Login fields not found",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate credentials
        if (validateCredentials(username, password, role)) {
            // Successful login
            this.dispose(); // Close the login window
            
            // Open appropriate dashboard based on role
            SwingUtilities.invokeLater(() -> {
                try {
                    switch (role.toLowerCase()) {
                        case "student":
                            new StudentDashboard(username).setVisible(true);
                            break;
                        case "instructor":
                        case "teacher":
                            new TeacherDashboard(username).setVisible(true);
                            break;
                        case "administrator":
                        case "admin":
                            new AdminDashboard(username).setVisible(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null,
                                "Invalid role specified: " + role,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                        "Error opening dashboard: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // Clear password field on failed login
        }
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Title
        JLabel titleLabel = new JLabel("University Online Examination System");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.WEST);

        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        navPanel.setOpaque(false);

        JButton homeButton = createNavButton("Home");
        JButton rulesButton = createNavButton("Rules");

        homeButton.addActionListener(e -> showPanel("WELCOME"));
        rulesButton.addActionListener(e -> showPanel("RULES"));

        navPanel.add(homeButton);
        navPanel.add(rulesButton);
        panel.add(navPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to the University Online Examination System");
        welcomeLabel.setFont(HEADER_FONT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel selectLabel = new JLabel("Please select your role to continue:");
        selectLabel.setFont(NORMAL_FONT);
        selectLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Role buttons
        JButton studentButton = createRoleButton("Student Login");
        JButton instructorButton = createRoleButton("Instructor Login");
        JButton adminButton = createRoleButton("Administrator Login");

        // Add action listeners
        studentButton.addActionListener(e -> showPanel("STUDENT"));
        instructorButton.addActionListener(e -> showPanel("INSTRUCTOR"));
        adminButton.addActionListener(e -> showPanel("ADMINISTRATOR"));

        // Add components
        gbc.insets = new Insets(20, 0, 40, 0);
        panel.add(welcomeLabel, gbc);
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(selectLabel, gbc);
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(studentButton, gbc);
        panel.add(instructorButton, gbc);
        panel.add(adminButton, gbc);

        return panel;
    }

    private JPanel createRulesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Examination Rules and Guidelines");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] rules = {
            "1. Ensure a stable internet connection before starting the exam.",
            "2. Use a computer with a working webcam and microphone.",
            "3. Close all unnecessary applications and browsers.",
            "4. No external assistance or communication is allowed.",
            "5. Time limits must be strictly followed.",
            "6. Submit your answers before the deadline.",
            "7. Technical issues should be reported immediately.",
            "8. Maintain academic integrity throughout the exam."
        };

        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setBackground(BACKGROUND_COLOR);
        rulesPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        for (String rule : rules) {
            JLabel ruleLabel = new JLabel(rule);
            ruleLabel.setFont(NORMAL_FONT);
            ruleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
            rulesPanel.add(ruleLabel);
        }

        JButton backButton = new JButton("Back to Home");
        styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showPanel("WELCOME"));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(rulesPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(backButton);

        return panel;
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(NORMAL_FONT);
        label.setPreferredSize(new Dimension(100, 25));
        
        panel.add(label);
        panel.add(field);
        
        return panel;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(NORMAL_FONT);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(SECONDARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private JButton createRoleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(HEADER_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setPreferredSize(new Dimension(300, 60));
        styleButton(button);
        return button;
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
                button.setBackground(ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    // Add this method to handle showing login panel from registration
    public void showLoginPanel() {
        cardLayout.show(contentPanel, "STUDENT"); // Default to student login
    }

    private boolean validateCredentials(String username, String password, String role) {
        // Basic validation
        if (username == null || password == null || role == null ||
            username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        // For testing purposes only - REMOVE IN PRODUCTION
        // Simple validation: username should be at least 3 characters and password at least 4
        return username.length() >= 3 && password.length() >= 4;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            SimpleLandingPage frame = new SimpleLandingPage();
            frame.setVisible(true);
        });
    }
}
