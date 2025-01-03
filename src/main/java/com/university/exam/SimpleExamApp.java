package com.university.exam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.university.exam.model.*;
import com.university.exam.ui.ModernUI;
import com.university.exam.ui.ModernUI.*;
import com.university.exam.ui.AdvancedUIEffects;
import com.university.exam.ui.AnimatedPanel;
import com.university.exam.ui.ModernCard;
import com.university.exam.ui.RoundedBorder;
import static com.university.exam.ui.ModernUI.*;

public class SimpleExamApp {
    private static JFrame mainFrame;
    private static UserManager userManager;
    private static ExamManager examManager;
    private static Timer transitionTimer;

    public static void main(String[] args) {
        // Ensure we run on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            userManager = UserManager.getInstance();
            examManager = ExamManager.getInstance();
            
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        mainFrame = new JFrame("University Exam System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        
        // Set custom frame appearance
        mainFrame.setUndecorated(true);
        mainFrame.getRootPane().setBorder(new RoundedBorder(PRIMARY, 15, true));
        
        // Add window controls
        JPanel titleBar = createTitleBar();
        mainFrame.add(titleBar, BorderLayout.NORTH);
        
        showLoginPanel();
        mainFrame.setVisible(true);
    }

    private static JPanel createTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(PRIMARY);
        titleBar.setPreferredSize(new Dimension(0, 30));

        // Title
        JLabel title = new JLabel("University Exam System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 12));
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titleBar.add(title, BorderLayout.WEST);

        // Window controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controls.setOpaque(false);

        JButton minimizeButton = new JButton("—");
        JButton closeButton = new JButton("×");

        for (JButton button : new JButton[]{minimizeButton, closeButton}) {
            button.setPreferredSize(new Dimension(45, 30));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            controls.add(button);
        }

        minimizeButton.addActionListener(e -> mainFrame.setState(Frame.ICONIFIED));
        closeButton.addActionListener(e -> System.exit(0));

        titleBar.add(controls, BorderLayout.EAST);

        // Make window draggable
        MouseAdapter dragAdapter = new MouseAdapter() {
            private Point dragPoint;

            public void mousePressed(MouseEvent e) {
                dragPoint = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                Point location = mainFrame.getLocation();
                mainFrame.setLocation(
                    location.x + e.getX() - dragPoint.x,
                    location.y + e.getY() - dragPoint.y
                );
            }
        };
        titleBar.addMouseListener(dragAdapter);
        titleBar.addMouseMotionListener(dragAdapter);

        return titleBar;
    }

    private static void showLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(BACKGROUND);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Logo or title
        JLabel titleLabel = createStyledLabel("University Exam System", 24, true);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = createStyledLabel("Please log in to continue", 14, false);
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input fields with modern styling
        ModernTextField usernameField = new ModernTextField("Username");
        ModernPasswordField passwordField = new ModernPasswordField("Password");
        
        // Buttons with modern styling
        ModernButton loginButton = new ModernButton("Login");
        loginButton.setBackground(PRIMARY);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(200, 40));

        ModernButton registerStudentButton = new ModernButton("Register as Student");
        registerStudentButton.setBackground(SECONDARY);
        registerStudentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerStudentButton.setMaximumSize(new Dimension(200, 40));

        ModernButton registerTeacherButton = new ModernButton("Register as Teacher");
        registerTeacherButton.setBackground(SECONDARY);
        registerTeacherButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerTeacherButton.setMaximumSize(new Dimension(200, 40));

        // Error message label
        JLabel messageLabel = createStyledLabel(" ", 12, false);
        messageLabel.setForeground(ERROR);
        
        // Set maximum sizes
        Dimension fieldSize = new Dimension(300, 40);
        Dimension buttonSize = new Dimension(250, 45);
        
        usernameField.setMaximumSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        loginButton.setMaximumSize(buttonSize);
        registerStudentButton.setMaximumSize(buttonSize);
        registerTeacherButton.setMaximumSize(buttonSize);

        // Center alignment
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerStudentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerTeacherButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add hover effects
        for (JButton button : new JButton[]{loginButton, registerStudentButton, registerTeacherButton}) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(button.getBackground().brighter());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(button.getBackground().darker());
                }
            });
        }

        // Button actions with animations
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (userManager.authenticateUser(username, password)) {
                SimpleUser user = userManager.getUser(username);
                animateTransition(() -> showDashboard(user));
            } else {
                messageLabel.setText("Invalid username or password!");
                messageLabel.setForeground(ERROR);
                shakeComponent(loginPanel);
            }
        });

        registerStudentButton.addActionListener(e -> 
            animateTransition(() -> showRegistrationPanel("student")));
        registerTeacherButton.addActionListener(e -> 
            animateTransition(() -> showRegistrationPanel("teacher")));

        // Add components with spacing
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(subtitleLabel);
        loginPanel.add(Box.createVerticalStrut(30));
        
        // Username field with floating label
        JPanel usernamePanel = createFloatingLabelField("Username", usernameField);
        loginPanel.add(usernamePanel);
        loginPanel.add(Box.createVerticalStrut(20));
        
        // Password field with floating label
        JPanel passwordPanel = createFloatingLabelField("Password", passwordField);
        loginPanel.add(passwordPanel);
        
        loginPanel.add(Box.createVerticalStrut(30));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(registerStudentButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(registerTeacherButton);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(messageLabel);

        // Center the login panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(loginPanel);

        AnimatedPanel mainPanel = new AnimatedPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        mainPanel.fadeIn();
    }

    private static JPanel createFloatingLabelField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }

    private static void animateTransition(Runnable afterTransition) {
        if (transitionTimer != null && transitionTimer.isRunning()) {
            transitionTimer.stop();
        }

        JPanel glassPane = (JPanel) mainFrame.getGlassPane();
        glassPane.setBackground(new Color(0, 0, 0, 0));
        glassPane.setVisible(true);

        transitionTimer = new Timer(20, null);
        float[] alpha = {0f};

        transitionTimer.addActionListener(e -> {
            alpha[0] += 0.1f;
            glassPane.setBackground(new Color(0, 0, 0, Math.min(0.5f, alpha[0])));

            if (alpha[0] >= 1f) {
                transitionTimer.stop();
                afterTransition.run();
                
                // Fade out
                Timer fadeOutTimer = new Timer(20, null);
                fadeOutTimer.addActionListener(e2 -> {
                    alpha[0] -= 0.1f;
                    glassPane.setBackground(new Color(0, 0, 0, Math.max(0f, alpha[0])));
                    
                    if (alpha[0] <= 0f) {
                        fadeOutTimer.stop();
                        glassPane.setVisible(false);
                    }
                });
                fadeOutTimer.start();
            }
        });
        
        transitionTimer.start();
    }

    private static void shakeComponent(JComponent component) {
        final Point originalLocation = component.getLocation();
        final Timer timer = new Timer(50, null);
        final int[] delta = {5, -5, 4, -4, 3, -3, 2, -2, 1, -1, 0};
        final int[] index = {0};

        timer.addActionListener(e -> {
            if (index[0] < delta.length) {
                component.setLocation(originalLocation.x + delta[index[0]], originalLocation.y);
                index[0]++;
            } else {
                timer.stop();
                component.setLocation(originalLocation);
            }
        });
        
        timer.start();
    }

    private static void showRegistrationPanel(String role) {
        AnimatedPanel registerPanel = new AnimatedPanel();
        registerPanel.setLayout(new BorderLayout());

        ModernCard registrationCard = new ModernCard(role.equals("student") ? "Student Registration" : "Teacher Registration", PRIMARY);
        registrationCard.setLayout(new BoxLayout(registrationCard, BoxLayout.Y_AXIS));
        registrationCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        String title = role.equals("student") ? "Student Registration" : "Teacher Registration";
        JLabel titleLabel = createStyledLabel(title, 24, true);
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = createStyledLabel("Create your account", 14, false);
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernTextField usernameField = new ModernTextField("Username");
        ModernPasswordField passwordField = new ModernPasswordField("Password");
        ModernTextField fullNameField = new ModernTextField("Full Name");
        ModernButton registerButton = new ModernButton("Register");
        ModernButton backButton = new ModernButton("Back to Login");
        JLabel messageLabel = createStyledLabel(" ", 12, false);
        messageLabel.setForeground(ERROR);
        
        // Set maximum sizes
        Dimension fieldSize = new Dimension(300, 40);
        Dimension buttonSize = new Dimension(250, 45);
        
        usernameField.setMaximumSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        fullNameField.setMaximumSize(fieldSize);
        registerButton.setMaximumSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                messageLabel.setText("All fields are required!");
                messageLabel.setForeground(ERROR);
                return;
            }

            if (userManager.registerUser(username, password, role, fullName)) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Registration successful! Please login.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                showLoginPanel();
            } else {
                messageLabel.setText("Username already exists!");
                messageLabel.setForeground(ERROR);
            }
        });

        backButton.addActionListener(e -> showLoginPanel());

        registrationCard.add(titleLabel);
        registrationCard.add(Box.createVerticalStrut(10));
        registrationCard.add(subtitleLabel);
        registrationCard.add(Box.createVerticalStrut(30));

        // Username field with floating label
        JPanel usernamePanel = createFloatingLabelField("Username", usernameField);
        registrationCard.add(usernamePanel);
        registrationCard.add(Box.createVerticalStrut(20));
        
        // Password field with floating label
        JPanel passwordPanel = createFloatingLabelField("Password", passwordField);
        registrationCard.add(passwordPanel);
        
        registrationCard.add(Box.createVerticalStrut(20));
        
        // Full Name field with floating label
        JPanel fullNamePanel = createFloatingLabelField("Full Name", fullNameField);
        registrationCard.add(fullNamePanel);

        registrationCard.add(Box.createVerticalStrut(30));
        registrationCard.add(registerButton);
        registrationCard.add(Box.createVerticalStrut(10));
        registrationCard.add(backButton);
        registrationCard.add(Box.createVerticalStrut(20));
        registrationCard.add(messageLabel);

        // Center the registration card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(registrationCard);

        registerPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(registerPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        registerPanel.fadeIn();
    }

    private static void showDashboard(SimpleUser user) {
        AnimatedPanel dashboardPanel = new AnimatedPanel();
        dashboardPanel.setLayout(new BorderLayout());

        ModernCard dashboardCard = new ModernCard("Dashboard", PRIMARY);
        dashboardCard.setLayout(new BoxLayout(dashboardCard, BoxLayout.Y_AXIS));
        dashboardCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullName() + "!");
        welcomeLabel.setForeground(TEXT_PRIMARY);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(100, 30));
        
        logoutButton.addActionListener(e -> showLoginPanel());

        dashboardCard.add(welcomeLabel);
        dashboardCard.add(Box.createVerticalStrut(20));

        if (user.getRole().equals("admin")) {
            JButton viewTeachersButton = new JButton("View Teachers");
            JButton viewStudentsButton = new JButton("View Students");
            JButton viewAllResultsButton = new JButton("View All Results");
            
            viewTeachersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewStudentsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewAllResultsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewTeachersButton.setMaximumSize(new Dimension(150, 30));
            viewStudentsButton.setMaximumSize(new Dimension(150, 30));
            viewAllResultsButton.setMaximumSize(new Dimension(150, 30));
            
            viewTeachersButton.addActionListener(e -> showTeachersList());
            viewStudentsButton.addActionListener(e -> showStudentsList());
            viewAllResultsButton.addActionListener(e -> showAllResults());
            
            dashboardCard.add(viewTeachersButton);
            dashboardCard.add(Box.createVerticalStrut(10));
            dashboardCard.add(viewStudentsButton);
            dashboardCard.add(Box.createVerticalStrut(10));
            dashboardCard.add(viewAllResultsButton);
        } else if (user.getRole().equals("teacher")) {
            JButton createExamButton = new JButton("Create Exam");
            JButton viewResultsButton = new JButton("View Results");
            JButton viewStudentsButton = new JButton("View Students");
            
            createExamButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewResultsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewStudentsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            createExamButton.setMaximumSize(new Dimension(150, 30));
            viewResultsButton.setMaximumSize(new Dimension(150, 30));
            viewStudentsButton.setMaximumSize(new Dimension(150, 30));
            
            createExamButton.addActionListener(e -> showCreateExamPanel());
            viewResultsButton.addActionListener(e -> showAllResults());
            viewStudentsButton.addActionListener(e -> showStudentsList());
            
            dashboardCard.add(createExamButton);
            dashboardCard.add(Box.createVerticalStrut(10));
            dashboardCard.add(viewResultsButton);
            dashboardCard.add(Box.createVerticalStrut(10));
            dashboardCard.add(viewStudentsButton);
        } else {
            JButton takeExamButton = new JButton("Take Exam");
            JButton viewGradesButton = new JButton("View Grades");
            
            takeExamButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewGradesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            takeExamButton.setMaximumSize(new Dimension(150, 30));
            viewGradesButton.setMaximumSize(new Dimension(150, 30));
            
            takeExamButton.addActionListener(e -> showAvailableExams(user.getUsername()));
            viewGradesButton.addActionListener(e -> showUserResults(user.getUsername()));
            
            dashboardCard.add(takeExamButton);
            dashboardCard.add(Box.createVerticalStrut(10));
            dashboardCard.add(viewGradesButton);
        }

        dashboardCard.add(Box.createVerticalStrut(30));
        dashboardCard.add(logoutButton);

        // Center the dashboard card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(dashboardCard);

        dashboardPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(dashboardPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        dashboardPanel.fadeIn();
    }

    private static void showStudentsList() {
        AnimatedPanel studentsPanel = new AnimatedPanel();
        studentsPanel.setLayout(new BorderLayout());

        ModernCard studentsCard = new ModernCard("Registered Students", PRIMARY);
        studentsCard.setLayout(new BoxLayout(studentsCard, BoxLayout.Y_AXIS));
        studentsCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Registered Students");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        studentsCard.add(titleLabel);
        studentsCard.add(Box.createVerticalStrut(20));

        List<SimpleUser> students = userManager.getAllStudents();
        if (students.isEmpty()) {
            JLabel noStudentsLabel = new JLabel("No students registered");
            noStudentsLabel.setForeground(TEXT_SECONDARY);
            noStudentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            studentsCard.add(noStudentsLabel);
        } else {
            for (SimpleUser student : students) {
                JLabel studentLabel = new JLabel(student.getFullName() + " (" + student.getUsername() + ")");
                studentLabel.setForeground(TEXT_SECONDARY);
                studentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                studentsCard.add(studentLabel);
                studentsCard.add(Box.createVerticalStrut(10));
            }
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showDashboard(userManager.getUser("admin")));

        studentsCard.add(Box.createVerticalStrut(20));
        studentsCard.add(backButton);

        // Center the students card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(studentsCard);

        studentsPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(studentsPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        studentsPanel.fadeIn();
    }

    private static void showCreateExamPanel() {
        AnimatedPanel createExamPanel = new AnimatedPanel();
        createExamPanel.setLayout(new BorderLayout());

        ModernCard createExamCard = new ModernCard("Create New Exam", PRIMARY);
        createExamCard.setLayout(new BoxLayout(createExamCard, BoxLayout.Y_AXIS));
        createExamCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Create New Exam");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField examTitleField = new JTextField("Exam Title");
        JTextField durationField = new JTextField("Duration (minutes)");
        examTitleField.setMaximumSize(new Dimension(300, 25));
        durationField.setMaximumSize(new Dimension(300, 25));

        JButton addQuestionButton = new JButton("Add Question");
        JButton finishButton = new JButton("Finish Exam");
        JButton backButton = new JButton("Back to Dashboard");

        addQuestionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        finishButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        ArrayList<SimpleQuestion> questions = new ArrayList<>();

        addQuestionButton.addActionListener(e -> {
            String questionText = JOptionPane.showInputDialog("Enter question:");
            if (questionText != null && !questionText.trim().isEmpty()) {
                ArrayList<String> options = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    String option = JOptionPane.showInputDialog("Enter option " + (i + 1) + ":");
                    if (option == null) return;
                    options.add(option);
                }
                
                String correctAnswer = JOptionPane.showInputDialog("Enter correct option number (1-4):");
                try {
                    int correct = Integer.parseInt(correctAnswer) - 1;
                    if (correct >= 0 && correct < 4) {
                        questions.add(new SimpleQuestion(questionText, options, correct));
                        JOptionPane.showMessageDialog(mainFrame, "Question added successfully!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid option number!");
                }
            }
        });

        finishButton.addActionListener(e -> {
            String title = examTitleField.getText();
            try {
                int duration = Integer.parseInt(durationField.getText());
                if (!title.isEmpty() && !questions.isEmpty()) {
                    SimpleExam exam = new SimpleExam(title, duration);
                    questions.forEach(exam::addQuestion);
                    examManager.addExam(exam);
                    JOptionPane.showMessageDialog(mainFrame, "Exam created successfully!");
                    showDashboard(userManager.getUser("teacher"));
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please fill all fields and add at least one question!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a valid duration!");
            }
        });

        backButton.addActionListener(e -> showDashboard(userManager.getUser("teacher")));

        createExamCard.add(titleLabel);
        createExamCard.add(Box.createVerticalStrut(20));
        createExamCard.add(new JLabel("Exam Title:"));
        createExamCard.add(examTitleField);
        createExamCard.add(Box.createVerticalStrut(10));
        createExamCard.add(new JLabel("Duration (minutes):"));
        createExamCard.add(durationField);
        createExamCard.add(Box.createVerticalStrut(20));
        createExamCard.add(addQuestionButton);
        createExamCard.add(Box.createVerticalStrut(10));
        createExamCard.add(finishButton);
        createExamCard.add(Box.createVerticalStrut(20));
        createExamCard.add(backButton);

        // Center the create exam card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(createExamCard);

        createExamPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(createExamPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        createExamPanel.fadeIn();
    }

    private static void showAvailableExams(String username) {
        AnimatedPanel examListPanel = new AnimatedPanel();
        examListPanel.setLayout(new BorderLayout());

        ModernCard examListCard = new ModernCard("Available Exams", PRIMARY);
        examListCard.setLayout(new BoxLayout(examListCard, BoxLayout.Y_AXIS));
        examListCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Available Exams");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        examListCard.add(titleLabel);
        examListCard.add(Box.createVerticalStrut(20));

        Map<String, Integer> exams = examManager.getAvailableExams();
        if (exams.isEmpty()) {
            JLabel noExamsLabel = new JLabel("No exams available");
            noExamsLabel.setForeground(TEXT_SECONDARY);
            noExamsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            examListCard.add(noExamsLabel);
        } else {
            for (Map.Entry<String, Integer> exam : exams.entrySet()) {
                JButton examButton = new JButton(exam.getKey());
                examButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                examButton.setMaximumSize(new Dimension(200, 30));
                examButton.addActionListener(e -> startExam(username, exam.getKey()));
                examListCard.add(examButton);
                examListCard.add(Box.createVerticalStrut(10));
            }
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showDashboard(userManager.getUser(username)));

        examListCard.add(Box.createVerticalStrut(20));
        examListCard.add(backButton);

        // Center the exam list card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(examListCard);

        examListPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(examListPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        examListPanel.fadeIn();
    }

    private static void startExam(String username, String examTitle) {
        SimpleExam exam = examManager.getExam(examTitle);
        if (exam == null) return;

        AnimatedPanel examPanel = new AnimatedPanel();
        examPanel.setLayout(new BorderLayout());

        ModernCard examCard = new ModernCard(examTitle, PRIMARY);
        examCard.setLayout(new BoxLayout(examCard, BoxLayout.Y_AXIS));
        examCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel(examTitle);
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        examCard.add(titleLabel);
        examCard.add(Box.createVerticalStrut(20));

        ArrayList<JRadioButton[]> allOptions = new ArrayList<>();
        int questionNumber = 1;

        for (SimpleQuestion question : exam.getQuestions()) {
            JLabel questionLabel = new JLabel(questionNumber + ". " + question.getQuestion());
            questionLabel.setForeground(TEXT_SECONDARY);
            questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            examCard.add(questionLabel);
            examCard.add(Box.createVerticalStrut(10));

            ButtonGroup group = new ButtonGroup();
            JRadioButton[] options = new JRadioButton[4];
            
            for (int i = 0; i < question.getOptions().size(); i++) {
                options[i] = new JRadioButton(question.getOptions().get(i));
                options[i].setAlignmentX(Component.CENTER_ALIGNMENT);
                group.add(options[i]);
                examCard.add(options[i]);
            }
            
            allOptions.add(options);
            examCard.add(Box.createVerticalStrut(20));
            questionNumber++;
        }

        JButton submitButton = new JButton("Submit Exam");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        submitButton.addActionListener(e -> {
            int score = 0;
            for (int i = 0; i < exam.getQuestions().size(); i++) {
                JRadioButton[] options = allOptions.get(i);
                SimpleQuestion question = exam.getQuestions().get(i);
                
                for (int j = 0; j < options.length; j++) {
                    if (options[j].isSelected() && j == question.getCorrectOption()) {
                        score++;
                        break;
                    }
                }
            }
            
            int finalScore = (score * 100) / exam.getQuestions().size();
            examManager.submitExamResult(username, examTitle, finalScore);
            JOptionPane.showMessageDialog(mainFrame, "Your score: " + finalScore + "%");
            showDashboard(userManager.getUser(username));
        });

        examCard.add(submitButton);

        // Center the exam card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(examCard);

        examPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(examPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        examPanel.fadeIn();
    }

    private static void showUserResults(String username) {
        AnimatedPanel resultsPanel = new AnimatedPanel();
        resultsPanel.setLayout(new BorderLayout());

        ModernCard resultsCard = new ModernCard("Your Exam Results", PRIMARY);
        resultsCard.setLayout(new BoxLayout(resultsCard, BoxLayout.Y_AXIS));
        resultsCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Your Exam Results");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultsCard.add(titleLabel);
        resultsCard.add(Box.createVerticalStrut(20));

        Map<String, Integer> results = examManager.getUserResults(username);
        if (results.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No exam results available");
            noResultsLabel.setForeground(TEXT_SECONDARY);
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsCard.add(noResultsLabel);
        } else {
            for (Map.Entry<String, Integer> result : results.entrySet()) {
                JLabel resultLabel = new JLabel(result.getKey() + ": " + result.getValue() + "%");
                resultLabel.setForeground(TEXT_SECONDARY);
                resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                resultsCard.add(resultLabel);
                resultsCard.add(Box.createVerticalStrut(10));
            }
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showDashboard(userManager.getUser(username)));

        resultsCard.add(Box.createVerticalStrut(20));
        resultsCard.add(backButton);

        // Center the results card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(resultsCard);

        resultsPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(resultsPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        resultsPanel.fadeIn();
    }

    private static void showAllResults() {
        AnimatedPanel allResultsPanel = new AnimatedPanel();
        allResultsPanel.setLayout(new BorderLayout());

        ModernCard allResultsCard = new ModernCard("All Student Results", PRIMARY);
        allResultsCard.setLayout(new BoxLayout(allResultsCard, BoxLayout.Y_AXIS));
        allResultsCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("All Student Results");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        allResultsCard.add(titleLabel);
        allResultsCard.add(Box.createVerticalStrut(20));

        Map<String, Map<String, Integer>> allResults = examManager.getAllResults();
        if (allResults.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No exam results available");
            noResultsLabel.setForeground(TEXT_SECONDARY);
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            allResultsCard.add(noResultsLabel);
        } else {
            for (Map.Entry<String, Map<String, Integer>> userResults : allResults.entrySet()) {
                JLabel userLabel = new JLabel("Student: " + userResults.getKey());
                userLabel.setFont(new Font("Arial", Font.BOLD, 14));
                userLabel.setForeground(TEXT_SECONDARY);
                userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                allResultsCard.add(userLabel);
                allResultsCard.add(Box.createVerticalStrut(10));

                for (Map.Entry<String, Integer> result : userResults.getValue().entrySet()) {
                    JLabel resultLabel = new JLabel("    " + result.getKey() + ": " + result.getValue() + "%");
                    resultLabel.setForeground(TEXT_SECONDARY);
                    resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    allResultsCard.add(resultLabel);
                    allResultsCard.add(Box.createVerticalStrut(5));
                }
                allResultsCard.add(Box.createVerticalStrut(15));
            }
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showDashboard(userManager.getUser("teacher")));

        allResultsCard.add(Box.createVerticalStrut(20));
        allResultsCard.add(backButton);

        // Center the all results card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(allResultsCard);

        allResultsPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(allResultsPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        allResultsPanel.fadeIn();
    }

    private static void showTeachersList() {
        AnimatedPanel teachersPanel = new AnimatedPanel();
        teachersPanel.setLayout(new BorderLayout());

        ModernCard teachersCard = new ModernCard("Registered Teachers", PRIMARY);
        teachersCard.setLayout(new BoxLayout(teachersCard, BoxLayout.Y_AXIS));
        teachersCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Registered Teachers");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        teachersCard.add(titleLabel);
        teachersCard.add(Box.createVerticalStrut(20));

        List<SimpleUser> teachers = userManager.getAllTeachers();
        if (teachers.isEmpty()) {
            JLabel noTeachersLabel = new JLabel("No teachers registered");
            noTeachersLabel.setForeground(TEXT_SECONDARY);
            noTeachersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            teachersCard.add(noTeachersLabel);
        } else {
            for (SimpleUser teacher : teachers) {
                JLabel teacherLabel = new JLabel(teacher.getFullName() + " (" + teacher.getUsername() + ")");
                teacherLabel.setForeground(TEXT_SECONDARY);
                teacherLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                teachersCard.add(teacherLabel);
                teachersCard.add(Box.createVerticalStrut(10));
            }
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showDashboard(userManager.getUser("admin")));

        teachersCard.add(Box.createVerticalStrut(20));
        teachersCard.add(backButton);

        // Center the teachers card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(teachersCard);

        teachersPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Animate the panel
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(teachersPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        teachersPanel.fadeIn();
    }
}
