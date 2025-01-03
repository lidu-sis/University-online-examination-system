package view;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractCellEditor;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TeacherDashboard extends JFrame {
    // Colors
    private static final Color BACKGROUND = new Color(245, 245, 245);
    private static final Color PRIMARY = new Color(70, 130, 180);
    private static final Color SECONDARY = new Color(176, 196, 222);
    
    // Fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    private String teacherName;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private static List<Map<String, Object>> savedExams = new ArrayList<>();
    private DefaultTableModel examTableModel;

    public TeacherDashboard(String teacherName) {
        this.teacherName = teacherName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Teacher Dashboard - " + teacherName);
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
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + teacherName);
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
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Create Exam", createExamPanel());
        tabbedPane.addTab("View Saved Exams", createSavedExamsPanel());
        tabbedPane.addTab("View Results", createResultsPanel());
        tabbedPane.addTab("Manage Students", createStudentsPanel());

        return tabbedPane;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Quick Stats
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatCard("Active Exams", "5"));
        statsPanel.add(createStatCard("Total Students", "150"));
        statsPanel.add(createStatCard("Pending Grades", "25"));

        // Upcoming Exams
        JPanel upcomingPanel = new JPanel();
        upcomingPanel.setLayout(new BoxLayout(upcomingPanel, BoxLayout.Y_AXIS));
        upcomingPanel.setBorder(BorderFactory.createTitledBorder("Upcoming Exams"));
        upcomingPanel.setBackground(Color.WHITE);

        String[] exams = {
            "Programming Basics - Tomorrow 10:00 AM",
            "Data Structures - Wed 2:00 PM",
            "Database Systems - Fri 9:00 AM"
        };

        for (String exam : exams) {
            JLabel examLabel = new JLabel(exam);
            examLabel.setFont(NORMAL_FONT);
            examLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
            upcomingPanel.add(examLabel);
        }

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(statsPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(upcomingPanel, gbc);

        return panel;
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(SECONDARY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(TITLE_FONT);
        valueLabel.setForeground(PRIMARY);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(NORMAL_FONT);

        gbc.gridy = 0;
        card.add(valueLabel, gbc);
        gbc.gridy = 1;
        card.add(titleLabel, gbc);

        return card;
    }

    private JPanel createExamPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title section
        JLabel titleHeaderLabel = new JLabel("Exam Details");
        titleHeaderLabel.setFont(HEADER_FONT);
        formPanel.add(titleHeaderLabel, gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);

        // Exam details
        JTextField examTitleField = new JTextField(40);
        JTextArea examDescField = new JTextArea(4, 40);
        examDescField.setLineWrap(true);
        examDescField.setWrapStyleWord(true);
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 300, 5));
        durationSpinner.setPreferredSize(new Dimension(80, 25));
        JSpinner startDateSpinner = createDateTimeSpinner();
        JSpinner endDateSpinner = createDateTimeSpinner();

        // Add exam details to form with tooltips
        formPanel.add(createFormLabel("Exam Title:"), gbc);
        formPanel.add(examTitleField, gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);

        formPanel.add(createFormLabel("Description:"), gbc);
        JScrollPane descScrollPane = new JScrollPane(examDescField);
        descScrollPane.setPreferredSize(new Dimension(600, 100));
        formPanel.add(descScrollPane, gbc);
        formPanel.add(Box.createVerticalStrut(15), gbc);

        // Time panel with explanations
        JPanel timePanel = new JPanel(new GridBagLayout());
        timePanel.setBackground(Color.WHITE);
        GridBagConstraints timeGbc = new GridBagConstraints();
        timeGbc.anchor = GridBagConstraints.WEST;
        timeGbc.insets = new Insets(5, 5, 5, 5);

        // Duration with number validation
        timeGbc.gridx = 0;
        timeGbc.gridy = 0;
        timePanel.add(new JLabel("Duration (minutes):"), timeGbc);
        timeGbc.gridx = 1;
        timePanel.add(durationSpinner, timeGbc);
        JLabel durationHintLabel = new JLabel("(Time allowed for each student to complete the exam)");
        durationHintLabel.setFont(new Font(NORMAL_FONT.getName(), Font.ITALIC, 12));
        durationHintLabel.setForeground(Color.GRAY);
        timeGbc.gridx = 2;
        timePanel.add(durationHintLabel, timeGbc);

        // Date picker for start date
        timeGbc.gridx = 0;
        timeGbc.gridy = 1;
        timePanel.add(new JLabel("Start Date:"), timeGbc);
        timeGbc.gridx = 1;
        timePanel.add(startDateSpinner, timeGbc);
        JLabel startHintLabel = new JLabel("(When students can begin taking the exam)");
        startHintLabel.setFont(new Font(NORMAL_FONT.getName(), Font.ITALIC, 12));
        startHintLabel.setForeground(Color.GRAY);
        timeGbc.gridx = 2;
        timePanel.add(startHintLabel, timeGbc);

        // Date picker for end date
        timeGbc.gridx = 0;
        timeGbc.gridy = 2;
        timePanel.add(new JLabel("End Date:"), timeGbc);
        timeGbc.gridx = 1;
        timePanel.add(endDateSpinner, timeGbc);
        JLabel endHintLabel = new JLabel("(Last time students can start the exam)");
        endHintLabel.setFont(new Font(NORMAL_FONT.getName(), Font.ITALIC, 12));
        endHintLabel.setForeground(Color.GRAY);
        timeGbc.gridx = 2;
        timePanel.add(endHintLabel, timeGbc);

        formPanel.add(timePanel, gbc);
        formPanel.add(Box.createVerticalStrut(20), gbc);

        // Questions section header
        JLabel questionsHeaderLabel = new JLabel("Exam Questions");
        questionsHeaderLabel.setFont(HEADER_FONT);
        formPanel.add(questionsHeaderLabel, gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);

        // Question type selection panel
        JPanel questionTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionTypePanel.setBackground(Color.WHITE);
        questionTypePanel.add(new JLabel("Question Type:"));
        String[] questionTypes = {"Multiple Choice", "True/False", "Fill in the Blank"};
        JComboBox<String> questionTypeCombo = new JComboBox<>(questionTypes);
        questionTypePanel.add(questionTypeCombo);
        formPanel.add(questionTypePanel, gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);

        // Questions panel
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(Color.WHITE);

        List<QuestionPanel> questionPanels = new ArrayList<>();
        
        // Add Question button
        JButton addQuestionBtn = new JButton("Add Question");
        styleButton(addQuestionBtn);
        addQuestionBtn.addActionListener(e -> {
            QuestionPanel qPanel = new QuestionPanel(questionPanels.size() + 1, 
                                                   (String)questionTypeCombo.getSelectedItem());
            questionPanels.add(qPanel);
            questionsPanel.add(qPanel);
            questionsPanel.add(Box.createVerticalStrut(10));
            questionsPanel.revalidate();
            questionsPanel.repaint();
        });

        // Make the questions scroll pane taller
        JScrollPane questionsScrollPane = new JScrollPane(questionsPanel);
        questionsScrollPane.setPreferredSize(new Dimension(600, 400));
        formPanel.add(questionsScrollPane, gbc);
        formPanel.add(Box.createVerticalStrut(10), gbc);
        formPanel.add(addQuestionBtn, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton saveButton = new JButton("Save Exam");
        JButton cancelButton = new JButton("Clear Form");
        
        styleButton(saveButton);
        styleButton(cancelButton);
        
        saveButton.addActionListener(e -> saveExam(
            examTitleField.getText(),
            examDescField.getText(),
            String.valueOf(durationSpinner.getValue()),
            formatDateTime((Date)startDateSpinner.getValue()),
            formatDateTime((Date)endDateSpinner.getValue()),
            questionPanels
        ));
        
        cancelButton.addActionListener(e -> clearExamForm(
            examTitleField,
            examDescField,
            durationSpinner,
            startDateSpinner,
            endDateSpinner,
            questionPanels,
            questionsPanel
        ));

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        formPanel.add(buttonsPanel, gbc);

        // Add form to main panel with scrolling
        JScrollPane mainScrollPane = new JScrollPane(formPanel);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(mainScrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        return label;
    }

    private void saveExam(String title, String description, String duration,
                         String startDate, String endDate, 
                         List<QuestionPanel> questionPanels) {
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
        examData.put("questionCount", questions.size());

        // Save exam data
        savedExams.add(examData);

        // Update the exam table if it exists
        if (examTableModel != null) {
            examTableModel.addRow(new Object[]{
                title,
                startDate,
                endDate,
                duration + " min",
                String.valueOf(questions.size()),
                "View/Edit"
            });
        }

        // Show success message
        JOptionPane.showMessageDialog(this,
            "Exam created successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);

        // Switch to the View Saved Exams tab
        tabbedPane.setSelectedIndex(2); // Index 2 is the View Saved Exams tab
    }

    private void clearExamForm(JTextField titleField, JTextArea descField,
                              JSpinner durationField, JSpinner startDateField,
                              JSpinner endDateField, List<QuestionPanel> questionPanels,
                              JPanel questionsPanel) {
        titleField.setText("");
        descField.setText("");
        durationField.setValue(60);
        startDateField.setValue(new Date());
        endDateField.setValue(new Date());
        
        questionPanels.clear();
        questionsPanel.removeAll();
        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Create table
        String[] columns = {"Student Name", "Exam", "Score", "Date"};
        Object[][] data = {
            {"John Doe", "Programming Basics", "85%", "2023-12-25"},
            {"Jane Smith", "Data Structures", "92%", "2023-12-26"},
            {"Bob Wilson", "Database Systems", "78%", "2023-12-27"}
        };

        JTable table = new JTable(data, columns);
        table.setFont(NORMAL_FONT);
        table.getTableHeader().setFont(HEADER_FONT);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Create table
        String[] columns = {"ID", "Name", "Email", "Status"};
        Object[][] data = {
            {"001", "John Doe", "john@email.com", "Active"},
            {"002", "Jane Smith", "jane@email.com", "Active"},
            {"003", "Bob Wilson", "bob@email.com", "Inactive"}
        };

        JTable table = new JTable(data, columns);
        table.setFont(NORMAL_FONT);
        table.getTableHeader().setFont(HEADER_FONT);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSavedExamsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create table model
        String[] columnNames = {"Exam Title", "Start Date", "End Date", "Duration", "Questions", "Actions"};
        examTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only allow editing the Actions column
            }
        };

        // Create table
        JTable examTable = new JTable(examTableModel);
        examTable.setFont(NORMAL_FONT);
        examTable.setRowHeight(30);
        examTable.getTableHeader().setFont(HEADER_FONT);

        // Set column widths
        examTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        examTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        examTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        examTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        examTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        examTable.getColumnModel().getColumn(5).setPreferredWidth(150);

        // Load existing exams
        for (Map<String, Object> exam : savedExams) {
            examTableModel.addRow(new Object[]{
                exam.get("title"),
                exam.get("startDate"),
                exam.get("endDate"),
                exam.get("duration") + " min",
                exam.get("questionCount"),
                "View/Edit"
            });
        }

        // Add action buttons to each row
        Action viewEdit = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int row = Integer.parseInt(e.getActionCommand());
                String examTitle = (String)examTableModel.getValueAt(row, 0);
                viewExam(examTitle);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(examTable, viewEdit, 5);

        // Add search panel at the top
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Search Exams: "));
        JTextField searchField = new JTextField(30);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filterExams(); }
            public void removeUpdate(DocumentEvent e) { filterExams(); }
            public void insertUpdate(DocumentEvent e) { filterExams(); }

            private void filterExams() {
                String searchText = searchField.getText().toLowerCase();
                examTableModel.setRowCount(0);
                for (Map<String, Object> exam : savedExams) {
                    String title = ((String)exam.get("title")).toLowerCase();
                    if (title.contains(searchText)) {
                        examTableModel.addRow(new Object[]{
                            exam.get("title"),
                            exam.get("startDate"),
                            exam.get("endDate"),
                            exam.get("duration") + " min",
                            exam.get("questionCount"),
                            "View/Edit"
                        });
                    }
                }
            }
        });
        searchPanel.add(searchField);

        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton);
        refreshButton.addActionListener(e -> refreshExamList());
        searchPanel.add(refreshButton);

        // Add components to panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(examTable), BorderLayout.CENTER);

        return panel;
    }

    private void refreshExamList() {
        if (examTableModel != null) {
            examTableModel.setRowCount(0);
            for (Map<String, Object> exam : savedExams) {
                examTableModel.addRow(new Object[]{
                    exam.get("title"),
                    exam.get("startDate"),
                    exam.get("endDate"),
                    exam.get("duration") + " min",
                    exam.get("questionCount"),
                    "View/Edit"
                });
            }
        }
    }

    private void viewExam(String examTitle) {
        // Find the exam data
        final Map<String, Object> examData = findExamByTitle(examTitle);

        if (examData == null) {
            JOptionPane.showMessageDialog(this,
                "Exam not found: " + examTitle,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dialog to show exam details
        JDialog dialog = new JDialog(this, "View/Edit Exam: " + examTitle, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add fields
        final JTextField titleField = new JTextField((String)examData.get("title"));
        final JTextArea descField = new JTextArea((String)examData.get("description"));
        descField.setRows(3);
        final JTextField durationField = new JTextField((String)examData.get("duration"));
        final JTextField startDateField = new JTextField((String)examData.get("startDate"));
        final JTextField endDateField = new JTextField((String)examData.get("endDate"));

        formPanel.add(new JLabel("Title:"), gbc);
        formPanel.add(titleField, gbc);
        formPanel.add(new JLabel("Description:"), gbc);
        formPanel.add(new JScrollPane(descField), gbc);
        formPanel.add(new JLabel("Duration (minutes):"), gbc);
        formPanel.add(durationField, gbc);
        formPanel.add(new JLabel("Start Date:"), gbc);
        formPanel.add(startDateField, gbc);
        formPanel.add(new JLabel("End Date:"), gbc);
        formPanel.add(endDateField, gbc);

        // Add questions panel
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> questions = (List<Map<String, Object>>)examData.get("questions");
        for (Map<String, Object> question : questions) {
            JPanel questionPanel = new JPanel(new GridBagLayout());
            questionPanel.setBorder(BorderFactory.createTitledBorder("Question"));
            
            JTextArea questionText = new JTextArea((String)question.get("text"));
            questionText.setRows(3);
            questionText.setLineWrap(true);
            questionText.setWrapStyleWord(true);
            
            questionPanel.add(new JLabel("Question Text:"), gbc);
            questionPanel.add(new JScrollPane(questionText), gbc);
            questionPanel.add(new JLabel("Type: " + question.get("type")), gbc);
            questionPanel.add(new JLabel("Correct Answer: " + question.get("correctAnswer")), gbc);
            questionPanel.add(new JLabel("Points: " + question.get("points")), gbc);
            
            if (question.containsKey("options")) {
                @SuppressWarnings("unchecked")
                List<String> options = (List<String>)question.get("options");
                for (int i = 0; i < options.size(); i++) {
                    questionPanel.add(new JLabel("Option " + (i + 1) + ": " + options.get(i)), gbc);
                }
            }
            
            questionsPanel.add(questionPanel);
            questionsPanel.add(Box.createVerticalStrut(10));
        }

        // Add save and cancel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Close");
        
        saveButton.addActionListener(e -> {
            // Update exam data
            examData.put("title", titleField.getText());
            examData.put("description", descField.getText());
            examData.put("duration", durationField.getText());
            examData.put("startDate", startDateField.getText());
            examData.put("endDate", endDateField.getText());
            
            // Refresh the table
            refreshExamList();
            dialog.dispose();
            
            JOptionPane.showMessageDialog(this,
                "Exam updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        dialog.add(new JScrollPane(formPanel), BorderLayout.NORTH);
        dialog.add(new JScrollPane(questionsPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show dialog
        dialog.setVisible(true);
    }

    private Map<String, Object> findExamByTitle(String title) {
        for (Map<String, Object> exam : savedExams) {
            if (title.equals(exam.get("title"))) {
                return exam;
            }
        }
        return null;
    }

    private JSpinner createDateTimeSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        model.setCalendarField(Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm");
        spinner.setEditor(editor);
        spinner.setValue(new Date());
        spinner.setPreferredSize(new Dimension(150, 25));
        return spinner;
    }

    private String formatDateTime(Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    private class QuestionPanel extends JPanel {
        private JTextArea questionText;
        private JComboBox<String> questionType;
        private JPanel optionsPanel;
        private List<JTextField> options;
        private JTextField correctAnswer;
        private JTextField points;

        public QuestionPanel(int questionNumber, String initialType) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createTitledBorder("Question " + questionNumber));

            // Question text with larger area
            questionText = new JTextArea(5, 50);  // Increased size
            questionText.setLineWrap(true);
            questionText.setWrapStyleWord(true);
            JScrollPane questionScrollPane = new JScrollPane(questionText);
            questionScrollPane.setPreferredSize(new Dimension(600, 120));  // Taller question area
            add(questionScrollPane);

            // Question type
            String[] types = {"Multiple Choice", "True/False", "Fill in the Blank"};
            questionType = new JComboBox<>(types);
            questionType.setSelectedItem(initialType);
            questionType.addActionListener(e -> updateOptionsPanel());
            add(questionType);

            // Options panel
            optionsPanel = new JPanel();
            optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
            optionsPanel.setBackground(Color.WHITE);
            options = new ArrayList<>();
            add(optionsPanel);

            // Correct answer
            correctAnswer = new JTextField(30);  // Made wider
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
            } else if ("Fill in the Blank".equals(type)) {
                // For Fill in the Blank, we'll add a hint field
                JTextField hintField = new JTextField(30);
                optionsPanel.add(createLabeledField("Hint (optional):", hintField));
                options.add(hintField);
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
            renderButton.setText("View/Edit");
            return renderButton;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            text = "View/Edit";
            editButton.setText(text);
            return editButton;
        }

        @Override
        public Object getCellEditorValue() {
            return text;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            int row = table.convertRowIndexToModel(table.getEditingRow());
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(row)));
        }
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

    public static List<Map<String, Object>> getSavedExams() {
        return savedExams;
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TeacherDashboard dashboard = new TeacherDashboard("Teacher");
            dashboard.setVisible(true);
        });
    }
}
