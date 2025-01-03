package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InstructorDashboard extends JFrame {
    // Colors for modern theme
    private static final Color PRIMARY_DARK = new Color(44, 62, 80);
    private static final Color PRIMARY_LIGHT = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(26, 188, 156);
    private static final Color TEXT_COLOR = new Color(236, 240, 241);
    private static final Color HOVER_COLOR = new Color(46, 204, 113);
    private static final Color PANEL_BACKGROUND = new Color(248, 249, 250);
    
    // Fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.SEMIBOLD, 18);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    private final String instructorName;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Timer refreshTimer;

    public InstructorDashboard(String instructorName) {
        this.instructorName = instructorName;
        initializeFrame();
        initializeUI();
        startRefreshTimer();
    }

    private void initializeFrame() {
        setTitle("Instructor Dashboard - " + instructorName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setBackground(PANEL_BACKGROUND);
    }

    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PANEL_BACKGROUND);

        // Create and add components
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createMainContent(), BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        header.setPreferredSize(new Dimension(getWidth(), 70));

        // Left side - Title and instructor name
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Instructor Dashboard");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel nameLabel = new JLabel("Welcome, " + instructorName);
        nameLabel.setFont(NORMAL_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        
        titlePanel.add(titleLabel);
        titlePanel.add(nameLabel);

        // Right side - DateTime and Notifications
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(NORMAL_FONT);
        timeLabel.setForeground(TEXT_COLOR);
        updateDateTime(timeLabel);

        JButton notificationBtn = createIconButton("🔔");
        JButton profileBtn = createIconButton("👤");
        
        infoPanel.add(timeLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(notificationBtn);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(profileBtn);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(infoPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(PRIMARY_LIGHT);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Add menu items
        String[] menuItems = {
            "📊 Dashboard",
            "📝 Create Exam",
            "📋 Manage Exams",
            "👥 Student List",
            "📈 Analytics",
            "⚙️ Settings",
            "❓ Help"
        };

        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(10));
        }

        sidebar.add(Box.createVerticalGlue());
        
        // Logout button at bottom
        JButton logoutBtn = createMenuButton("🚪 Logout");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> handleLogout());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JPanel createMainContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(PANEL_BACKGROUND);

        // Add different content panels
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createExamCreationPanel(), "CREATE_EXAM");
        contentPanel.add(createExamManagementPanel(), "MANAGE_EXAMS");
        contentPanel.add(createStudentListPanel(), "STUDENT_LIST");
        contentPanel.add(createAnalyticsPanel(), "ANALYTICS");

        return contentPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatsCard("Active Exams", "12", new Color(52, 152, 219)));
        statsPanel.add(createStatsCard("Students", "150", new Color(155, 89, 182)));
        statsPanel.add(createStatsCard("Average Score", "85%", new Color(46, 204, 113)));
        statsPanel.add(createStatsCard("Pending Reviews", "8", new Color(230, 126, 34)));

        // Recent activity panel
        JPanel activityPanel = createActivityPanel();
        
        // Upcoming exams panel
        JPanel upcomingPanel = createUpcomingExamsPanel();

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 0;
        panel.add(statsPanel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.6;
        gbc.weighty = 1;
        panel.add(activityPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.4;
        panel.add(upcomingPanel, gbc);

        return panel;
    }

    private JPanel createStatsCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(NORMAL_FONT);
        titleLabel.setForeground(Color.GRAY);

        card.add(valueLabel, gbc);
        card.add(titleLabel, gbc);

        return card;
    }

    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Recent Activity");
        titleLabel.setFont(HEADER_FONT);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Activity list
        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setBackground(Color.WHITE);

        String[] activities = {
            "New exam submission from John Doe",
            "Grade updated for Programming Basics",
            "Question bank updated for Data Structures",
            "New student enrolled in Database Systems",
            "Exam schedule updated for next week"
        };

        for (String activity : activities) {
            activityList.add(createActivityItem(activity));
            activityList.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(activityList);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUpcomingExamsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Upcoming Exams");
        titleLabel.setFont(HEADER_FONT);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Exam list
        JPanel examList = new JPanel();
        examList.setLayout(new BoxLayout(examList, BoxLayout.Y_AXIS));
        examList.setBackground(Color.WHITE);

        String[] exams = {
            "Programming Basics - Tomorrow 10:00 AM",
            "Data Structures - Wed 2:00 PM",
            "Database Systems - Fri 9:00 AM",
            "Web Development - Next Mon 11:00 AM"
        };

        for (String exam : exams) {
            examList.add(createExamItem(exam));
            examList.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(examList);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActivityItem(String text) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setBorder(new EmptyBorder(8, 0, 8, 0));
        item.add(label, BorderLayout.CENTER);

        return item;
    }

    private JPanel createExamItem(String text) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setBorder(new EmptyBorder(8, 0, 8, 0));
        item.add(label, BorderLayout.CENTER);

        return item;
    }

    private JPanel createExamCreationPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Create New Exam");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_DARK);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Exam details
        JTextField examTitleField = new JTextField(30);
        JTextArea examDescField = new JTextArea(3, 30);
        examDescField.setLineWrap(true);
        JTextField durationField = new JTextField(10);
        JTextField startDateField = new JTextField(15);
        JTextField endDateField = new JTextField(15);

        formPanel.add(createFormLabel("Exam Title:"), gbc);
        formPanel.add(examTitleField, gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);

        formPanel.add(createFormLabel("Description:"), gbc);
        formPanel.add(new JScrollPane(examDescField), gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);

        // Duration and dates panel
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.setBackground(Color.WHITE);
        timePanel.add(new JLabel("Duration (minutes):"));
        timePanel.add(durationField);
        timePanel.add(Box.createHorizontalStrut(20));
        timePanel.add(new JLabel("Start Date:"));
        timePanel.add(startDateField);
        timePanel.add(Box.createHorizontalStrut(20));
        timePanel.add(new JLabel("End Date:"));
        timePanel.add(endDateField);
        formPanel.add(timePanel, gbc);
        formPanel.add(Box.createVerticalStrut(20), gbc);

        // Questions panel
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(Color.WHITE);
        
        JLabel questionsLabel = new JLabel("Questions");
        questionsLabel.setFont(HEADER_FONT);
        questionsPanel.add(questionsLabel);
        questionsPanel.add(Box.createVerticalStrut(10));

        List<QuestionPanel> questionPanels = new ArrayList<>();
        JButton addQuestionBtn = new JButton("Add Question");
        styleButton(addQuestionBtn);
        addQuestionBtn.addActionListener(e -> {
            QuestionPanel qPanel = new QuestionPanel(questionPanels.size() + 1);
            questionPanels.add(qPanel);
            questionsPanel.add(qPanel);
            questionsPanel.add(Box.createVerticalStrut(10));
            questionsPanel.revalidate();
            questionsPanel.repaint();
        });

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        formPanel.add(scrollPane, gbc);
        formPanel.add(Box.createVerticalStrut(20), gbc);
        formPanel.add(addQuestionBtn, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton saveButton = new JButton("Save Exam");
        JButton cancelButton = new JButton("Cancel");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        saveButton.addActionListener(e -> saveExam(
            examTitleField.getText(),
            examDescField.getText(),
            durationField.getText(),
            startDateField.getText(),
            endDateField.getText(),
            questionPanels
        ));
        
        cancelButton.addActionListener(e -> clearExamForm(
            examTitleField,
            examDescField,
            durationField,
            startDateField,
            endDateField,
            questionPanels,
            questionsPanel
        ));

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        formPanel.add(buttonsPanel, gbc);

        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        return panel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        return label;
    }

    private void saveExam(String title, String description, String duration,
                         String startDate, String endDate, List<QuestionPanel> questionPanels) {
        // Validate input
        if (title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter an exam title",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (questionPanels.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please add at least one question",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create exam data
        Map<String, Object> examData = new HashMap<>();
        examData.put("title", title);
        examData.put("description", description);
        examData.put("duration", duration);
        examData.put("startDate", startDate);
        examData.put("endDate", endDate);
        
        List<Map<String, Object>> questions = new ArrayList<>();
        for (QuestionPanel qPanel : questionPanels) {
            Map<String, Object> question = qPanel.getQuestionData();
            if (question != null) {
                questions.add(question);
            }
        }
        examData.put("questions", questions);

        // TODO: Send exam data to server
        // For now, just show success message
        JOptionPane.showMessageDialog(this,
            "Exam created successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearExamForm(JTextField titleField, JTextArea descField,
                              JTextField durationField, JTextField startDateField,
                              JTextField endDateField, List<QuestionPanel> questionPanels,
                              JPanel questionsPanel) {
        titleField.setText("");
        descField.setText("");
        durationField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        
        questionPanels.clear();
        questionsPanel.removeAll();
        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    // Inner class for question panels
    private class QuestionPanel extends JPanel {
        private JTextArea questionText;
        private JComboBox<String> questionType;
        private JPanel optionsPanel;
        private List<JTextField> options;
        private JTextField correctAnswer;
        private JTextField points;

        public QuestionPanel(int questionNumber) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createTitledBorder("Question " + questionNumber));

            // Question text
            questionText = new JTextArea(3, 40);
            questionText.setLineWrap(true);
            add(new JScrollPane(questionText));

            // Question type
            String[] types = {"Multiple Choice", "True/False", "Short Answer"};
            questionType = new JComboBox<>(types);
            questionType.addActionListener(e -> updateOptionsPanel());
            add(questionType);

            // Options panel
            optionsPanel = new JPanel();
            optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
            optionsPanel.setBackground(Color.WHITE);
            options = new ArrayList<>();
            add(optionsPanel);

            // Correct answer
            correctAnswer = new JTextField(20);
            add(createLabeledField("Correct Answer:", correctAnswer));

            // Points
            points = new JTextField(5);
            add(createLabeledField("Points:", points));

            updateOptionsPanel();
        }

        private JPanel createLabeledField(String labelText, JComponent field) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setBackground(Color.WHITE);
            panel.add(new JLabel(labelText));
            panel.add(field);
            return panel;
        }

        private void updateOptionsPanel() {
            optionsPanel.removeAll();
            options.clear();

            String type = (String) questionType.getSelectedItem();
            if ("Multiple Choice".equals(type)) {
                for (int i = 0; i < 4; i++) {
                    JTextField option = new JTextField(30);
                    options.add(option);
                    optionsPanel.add(createLabeledField("Option " + (i + 1) + ":", option));
                }
            } else if ("True/False".equals(type)) {
                correctAnswer.setText("");
                String[] tfOptions = {"True", "False"};
                JComboBox<String> tfCombo = new JComboBox<>(tfOptions);
                optionsPanel.add(createLabeledField("Options:", tfCombo));
            }

            optionsPanel.revalidate();
            optionsPanel.repaint();
        }

        public Map<String, Object> getQuestionData() {
            String text = questionText.getText().trim();
            String type = (String) questionType.getSelectedItem();
            String answer = correctAnswer.getText().trim();
            String pointsValue = points.getText().trim();

            if (text.isEmpty() || answer.isEmpty() || pointsValue.isEmpty()) {
                return null;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("text", text);
            data.put("type", type);
            data.put("correctAnswer", answer);
            data.put("points", pointsValue);

            if ("Multiple Choice".equals(type)) {
                List<String> optionsList = new ArrayList<>();
                for (JTextField option : options) {
                    optionsList.add(option.getText().trim());
                }
                data.put("options", optionsList);
            }

            return data;
        }
    }

    private JPanel createExamManagementPanel() {
        // Placeholder for exam management panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.add(new JLabel("Exam Management Panel - To be implemented", SwingConstants.CENTER));
        return panel;
    }

    private JPanel createStudentListPanel() {
        // Placeholder for student list panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.add(new JLabel("Student List Panel - To be implemented", SwingConstants.CENTER));
        return panel;
    }

    private JPanel createAnalyticsPanel() {
        // Placeholder for analytics panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.add(new JLabel("Analytics Panel - To be implemented", SwingConstants.CENTER));
        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(NORMAL_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(PRIMARY_LIGHT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_LIGHT);
            }
        });

        // Add action listener to switch panels
        button.addActionListener(e -> {
            String cmd = text.substring(text.indexOf(' ') + 1).toUpperCase().replace(' ', '_');
            if (contentPanel != null && cardLayout != null) {
                cardLayout.show(contentPanel, cmd);
            }
        });

        return button;
    }

    private JButton createIconButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        button.setForeground(TEXT_COLOR);
        button.setBackground(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void updateDateTime(JLabel label) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
        label.setText(LocalDateTime.now().format(formatter));
    }

    private void startRefreshTimer() {
        refreshTimer = new Timer(1000, e -> {
            Component[] components = mainPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    Component[] headerComps = ((JPanel) comp).getComponents();
                    for (Component headerComp : headerComps) {
                        if (headerComp instanceof JPanel) {
                            Component[] labels = ((JPanel) headerComp).getComponents();
                            for (Component label : labels) {
                                if (label instanceof JLabel) {
                                    JLabel timeLabel = (JLabel) label;
                                    if (timeLabel.getText().contains(":")) {
                                        updateDateTime(timeLabel);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        refreshTimer.start();
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (refreshTimer != null) {
                refreshTimer.stop();
            }
            dispose();
            new SimpleLandingPage().setVisible(true);
        }
    }

    private void styleButton(JButton button) {
        button.setFont(NORMAL_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(PRIMARY_LIGHT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_LIGHT);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new InstructorDashboard("Dr. Smith").setVisible(true);
        });
    }
}
