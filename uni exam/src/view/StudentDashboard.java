package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class StudentDashboard extends JFrame {
    // Colors
    private static final Color BACKGROUND = new Color(245, 245, 245);
    private static final Color PRIMARY = new Color(41, 128, 185);    // Blue
    private static final Color SECONDARY = new Color(52, 152, 219);  // Light Blue
    private static final Color ACCENT = new Color(46, 204, 113);     // Green
    
    // Fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    private String studentName;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private DefaultTableModel examTableModel;

    public StudentDashboard(String studentName) {
        this.studentName = studentName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Dashboard - " + studentName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Add components
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createTabbedPane(), BorderLayout.CENTER);

        setContentPane(mainPanel);
        
        // Initial refresh of available exams
        refreshAvailableExams();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + studentName);
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(Color.WHITE);

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn);
        logoutBtn.addActionListener(e -> handleLogout());

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        return header;
    }

    private JTabbedPane createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(BACKGROUND);

        // Add tabs
        tabbedPane.addTab("Available Exams", createAvailableExamsPanel());
        tabbedPane.addTab("My Results", createResultsPanel());
        tabbedPane.addTab("Profile", createProfilePanel());
        tabbedPane.addTab("Study Resources", createResourcesPanel());

        return tabbedPane;
    }

    private JPanel createAvailableExamsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Create table model with columns
        String[] columns = {"Exam Title", "Description", "Start Date", "End Date", "Duration (mins)", "Actions"};
        examTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only make the Actions column editable
            }
        };

        JTable table = new JTable(examTableModel);
        table.setFont(NORMAL_FONT);
        table.getTableHeader().setFont(HEADER_FONT);
        
        // Set up the action button column
        Action takeExam = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.parseInt(e.getActionCommand());
                String examTitle = (String) examTableModel.getValueAt(modelRow, 0);
                startExam(examTitle);
            }
        };

        new ButtonColumn(table, takeExam, 5);
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn);
        refreshBtn.addActionListener(e -> refreshAvailableExams());
        buttonPanel.add(refreshBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Results table
        String[] columns = {"Exam Title", "Date Taken", "Score", "Grade", "Feedback"};
        Object[][] data = {
            {"Java Programming", "2023-12-20", "85%", "A", "Excellent work!"},
            {"Web Development", "2023-12-15", "78%", "B", "Good effort"},
            {"Python Basics", "2023-12-10", "92%", "A+", "Outstanding!"}
        };

        JTable table = new JTable(data, columns);
        table.setFont(NORMAL_FONT);
        table.getTableHeader().setFont(HEADER_FONT);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Profile information
        addProfileField(panel, gbc, "Student ID:", "STU001");
        addProfileField(panel, gbc, "Name:", studentName);
        addProfileField(panel, gbc, "Email:", "student@university.edu");
        addProfileField(panel, gbc, "Department:", "Computer Science");
        addProfileField(panel, gbc, "Year:", "3rd Year");

        // Change password button
        JButton changePassBtn = new JButton("Change Password");
        styleButton(changePassBtn);
        gbc.gridy++;
        gbc.gridx = 1;
        panel.add(changePassBtn, gbc);

        return panel;
    }

    private void addProfileField(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
        gbc.gridy++;
    }

    private JPanel createResourcesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Resources list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("📚 Course Materials");
        listModel.addElement("📝 Practice Tests");
        listModel.addElement("📖 Study Guides");
        listModel.addElement("🎥 Video Tutorials");
        listModel.addElement("❓ FAQ Section");

        JList<String> resourceList = new JList<>(listModel);
        resourceList.setFont(NORMAL_FONT);
        resourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(resourceList), BorderLayout.CENTER);

        return panel;
    }

    private void styleButton(JButton button) {
        button.setFont(NORMAL_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY);
            }
        });
    }

    private void refreshAvailableExams() {
        examTableModel.setRowCount(0); // Clear existing rows
        
        // Get the saved exams from TeacherDashboard
        List<Map<String, Object>> savedExams = TeacherDashboard.getSavedExams();
        
        if (savedExams != null) {
            for (Map<String, Object> exam : savedExams) {
                Object[] row = new Object[6];
                row[0] = exam.get("title");
                row[1] = exam.get("description");
                row[2] = exam.get("startDate");
                row[3] = exam.get("endDate");
                row[4] = exam.get("duration");
                row[5] = "Take Exam";
                examTableModel.addRow(row);
            }
        }
    }

    private void startExam(String examTitle) {
        // Find the exam data
        List<Map<String, Object>> savedExams = TeacherDashboard.getSavedExams();
        Map<String, Object> examData = null;
        
        for (Map<String, Object> exam : savedExams) {
            if (examTitle.equals(exam.get("title"))) {
                examData = exam;
                break;
            }
        }

        if (examData == null) {
            JOptionPane.showMessageDialog(this,
                "Exam not found: " + examTitle,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if exam is available (within start and end dates)
        String startDateStr = (String) examData.get("startDate");
        String endDateStr = (String) examData.get("endDate");
        Date now = new Date();
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            
            if (now.before(startDate)) {
                JOptionPane.showMessageDialog(this,
                    "This exam is not yet available. It starts at " + startDateStr,
                    "Exam Not Available",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (now.after(endDate)) {
                JOptionPane.showMessageDialog(this,
                    "This exam has ended. The deadline was " + endDateStr,
                    "Exam Ended",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error checking exam dates",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show exam start confirmation
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you ready to start the exam?\n" +
            "Title: " + examTitle + "\n" +
            "Duration: " + examData.get("duration") + " minutes\n\n" +
            "Note: The timer will start immediately after you click Yes.",
            "Start Exam",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (choice == JOptionPane.YES_OPTION) {
            showExamWindow(examData);
        }
    }

    private void showExamWindow(Map<String, Object> examData) {
        // Create exam window
        JDialog examDialog = new JDialog(this, "Exam: " + examData.get("title"), true);
        examDialog.setSize(900, 700);
        examDialog.setLocationRelativeTo(this);
        examDialog.setLayout(new BorderLayout(10, 10));

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Header panel with exam info and timer
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(Color.WHITE);
        
        // Timer label
        JLabel timerLabel = new JLabel("Time Remaining: " + examData.get("duration") + ":00");
        timerLabel.setFont(HEADER_FONT);
        headerPanel.add(timerLabel, BorderLayout.EAST);
        
        // Start timer
        int durationMinutes = Integer.parseInt((String)examData.get("duration"));
        Timer timer = new Timer(1000, new ActionListener() {
            private int timeLeft = durationMinutes * 60;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
                
                if (timeLeft <= 0) {
                    ((Timer)e.getSource()).stop();
                    submitExam(examDialog, examData, getAnswers());
                }
            }
        });
        timer.start();

        // Questions panel
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(Color.WHITE);

        // Map to store answers
        Map<Integer, String> answers = new HashMap<>();
        
        // Add questions
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> questions = (List<Map<String, Object>>)examData.get("questions");
        for (int i = 0; i < questions.size(); i++) {
            Map<String, Object> question = questions.get(i);
            JPanel questionPanel = createQuestionPanel(i + 1, question, answers);
            questionsPanel.add(questionPanel);
            questionsPanel.add(Box.createVerticalStrut(20));
        }

        // Submit button
        JButton submitButton = new JButton("Submit Exam");
        styleButton(submitButton);
        submitButton.addActionListener(e -> {
            timer.stop();
            submitExam(examDialog, examData, answers);
        });

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(questionsPanel), BorderLayout.CENTER);
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        examDialog.add(mainPanel);
        examDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        examDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(examDialog,
                    "Are you sure you want to exit? Your progress will be lost.",
                    "Exit Exam",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                    
                if (choice == JOptionPane.YES_OPTION) {
                    timer.stop();
                    examDialog.dispose();
                }
            }
        });

        examDialog.setVisible(true);
    }

    private JPanel createQuestionPanel(int questionNumber, Map<String, Object> question, Map<Integer, String> answers) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        // Question text
        JLabel questionLabel = new JLabel(questionNumber + ". " + question.get("text"));
        questionLabel.setFont(NORMAL_FONT);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(questionLabel);
        panel.add(Box.createVerticalStrut(10));

        String type = (String)question.get("type");
        if ("Multiple Choice".equals(type)) {
            @SuppressWarnings("unchecked")
            List<String> options = (List<String>)question.get("options");
            ButtonGroup group = new ButtonGroup();
            
            for (String option : options) {
                JRadioButton radio = new JRadioButton(option);
                radio.setBackground(Color.WHITE);
                radio.setFont(NORMAL_FONT);
                radio.setAlignmentX(Component.LEFT_ALIGNMENT);
                group.add(radio);
                panel.add(radio);
                
                radio.addActionListener(e -> answers.put(questionNumber, option));
            }
        } else if ("True/False".equals(type)) {
            ButtonGroup group = new ButtonGroup();
            JRadioButton trueBtn = new JRadioButton("True");
            JRadioButton falseBtn = new JRadioButton("False");
            
            trueBtn.setBackground(Color.WHITE);
            falseBtn.setBackground(Color.WHITE);
            trueBtn.setFont(NORMAL_FONT);
            falseBtn.setFont(NORMAL_FONT);
            trueBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            falseBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            group.add(trueBtn);
            group.add(falseBtn);
            panel.add(trueBtn);
            panel.add(falseBtn);
            
            trueBtn.addActionListener(e -> answers.put(questionNumber, "True"));
            falseBtn.addActionListener(e -> answers.put(questionNumber, "False"));
        } else if ("Fill in the Blank".equals(type)) {
            JTextField answerField = new JTextField();
            answerField.setMaximumSize(new Dimension(300, 30));
            answerField.setFont(NORMAL_FONT);
            answerField.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(answerField);
            
            answerField.getDocument().addDocumentListener(new DocumentListener() {
                private void updateAnswer() {
                    answers.put(questionNumber, answerField.getText().trim());
                }
                
                @Override
                public void insertUpdate(DocumentEvent e) { updateAnswer(); }
                
                @Override
                public void removeUpdate(DocumentEvent e) { updateAnswer(); }
                
                @Override
                public void changedUpdate(DocumentEvent e) { updateAnswer(); }
            });
        }

        return panel;
    }

    private void submitExam(JDialog examDialog, Map<String, Object> examData, Map<Integer, String> answers) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> questions = (List<Map<String, Object>>)examData.get("questions");
        
        // Calculate score
        int totalPoints = 0;
        int earnedPoints = 0;
        StringBuilder feedback = new StringBuilder();
        
        for (int i = 0; i < questions.size(); i++) {
            Map<String, Object> question = questions.get(i);
            int questionNumber = i + 1;
            String correctAnswer = (String)question.get("correctAnswer");
            String userAnswer = answers.get(questionNumber);
            int points = Integer.parseInt((String)question.get("points"));
            
            totalPoints += points;
            
            feedback.append(String.format("Question %d:\n", questionNumber));
            feedback.append(String.format("Your answer: %s\n", userAnswer != null ? userAnswer : "No answer"));
            feedback.append(String.format("Correct answer: %s\n", correctAnswer));
            
            if (correctAnswer.equalsIgnoreCase(userAnswer)) {
                earnedPoints += points;
                feedback.append("Points earned: ").append(points).append("\n");
            } else {
                feedback.append("Points earned: 0\n");
            }
            feedback.append("\n");
        }
        
        double percentage = (double)earnedPoints / totalPoints * 100;
        String grade = calculateGrade(percentage);
        
        // Show results
        JTextArea feedbackArea = new JTextArea(feedback.toString());
        feedbackArea.setEditable(false);
        feedbackArea.setFont(NORMAL_FONT);
        
        JOptionPane.showMessageDialog(examDialog,
            String.format("Exam completed!\n\nScore: %.1f%%\nGrade: %s\n\nDetailed Feedback:\n", percentage, grade) +
            feedback.toString(),
            "Exam Results",
            JOptionPane.INFORMATION_MESSAGE);
            
        examDialog.dispose();
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 90) return "A";
        if (percentage >= 80) return "B";
        if (percentage >= 70) return "C";
        if (percentage >= 60) return "D";
        return "F";
    }

    private Map<Integer, String> getAnswers() {
        // This method should return the answers from the exam window
        // For now, it returns an empty map
        return new HashMap<>();
    }

    // Inner class for button column
    private class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
        private JTable table;
        private Action action;
        private JButton renderButton;
        private JButton editButton;
        private String text;

        public ButtonColumn(JTable table, Action action, int column) {
            this.table = table;
            this.action = action;

            renderButton = new JButton();
            editButton = new JButton();
            editButton.setFocusPainted(false);
            editButton.addActionListener(this);

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setCellRenderer(this);
            columnModel.getColumn(column).setCellEditor(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (hasFocus) {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            } else if (isSelected) {
                renderButton.setForeground(table.getSelectionForeground());
                renderButton.setBackground(table.getSelectionBackground());
            } else {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }

            renderButton.setText((value == null) ? "" : value.toString());
            return renderButton;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            text = (value == null) ? "" : value.toString();
            editButton.setText(text);
            return editButton;
        }

        @Override
        public Object getCellEditorValue() {
            return text;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            fireEditingStopped();
            action.actionPerformed(new ActionEvent(this,
                ActionEvent.ACTION_PERFORMED, String.valueOf(row)));
        }
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new SimpleLandingPage().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new StudentDashboard("Student").setVisible(true);
        });
    }
}
