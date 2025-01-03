package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {
    // Colors
    private static final Color BACKGROUND = new Color(245, 245, 245);
    private static final Color PRIMARY = new Color(142, 68, 173);    // Purple
    private static final Color SECONDARY = new Color(155, 89, 182);  // Light Purple
    private static final Color ACCENT = new Color(192, 57, 43);      // Red
    
    // Fonts
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    private String adminName;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    public AdminDashboard(String adminName) {
        this.adminName = adminName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Dashboard - " + adminName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
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
        JLabel welcomeLabel = new JLabel("Welcome, Administrator " + adminName);
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
        tabbedPane.addTab("User Management", createUserManagementPanel());
        tabbedPane.addTab("System Settings", createSystemSettingsPanel());
        tabbedPane.addTab("Logs", createLogsPanel());
        tabbedPane.addTab("Backup", createBackupPanel());

        return tabbedPane;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatCard("Total Users", "350"));
        statsPanel.add(createStatCard("Active Exams", "15"));
        statsPanel.add(createStatCard("System Load", "45%"));
        statsPanel.add(createStatCard("Storage Used", "65%"));

        // System status
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder("System Status"));

        String[] statusColumns = {"Component", "Status", "Last Updated"};
        Object[][] statusData = {
            {"Database", "Online", "2 mins ago"},
            {"File Server", "Online", "5 mins ago"},
            {"Backup System", "Online", "10 mins ago"},
            {"Email Service", "Online", "1 min ago"}
        };

        JTable statusTable = new JTable(statusData, statusColumns);
        statusTable.setFont(NORMAL_FONT);
        statusTable.getTableHeader().setFont(HEADER_FONT);

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(statsPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(statusPanel, gbc);

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

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addUserBtn = new JButton("Add User");
        JButton editUserBtn = new JButton("Edit User");
        JButton deleteUserBtn = new JButton("Delete User");

        styleButton(addUserBtn);
        styleButton(editUserBtn);
        styleButton(deleteUserBtn);

        toolbar.add(addUserBtn);
        toolbar.add(editUserBtn);
        toolbar.add(deleteUserBtn);

        // User table
        String[] columns = {"ID", "Name", "Role", "Email", "Status", "Last Login"};
        Object[][] data = {
            {"001", "John Smith", "Teacher", "john@email.com", "Active", "Today 10:00"},
            {"002", "Mary Johnson", "Student", "mary@email.com", "Active", "Yesterday"},
            {"003", "David Wilson", "Admin", "david@email.com", "Active", "Today 09:30"}
        };

        JTable table = new JTable(data, columns);
        table.setFont(NORMAL_FONT);
        table.getTableHeader().setFont(HEADER_FONT);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSystemSettingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Settings categories
        addSettingCategory(panel, gbc, "Security Settings", new String[]{
            "Two-Factor Authentication",
            "Password Policy",
            "Session Timeout",
            "IP Whitelist"
        });

        addSettingCategory(panel, gbc, "Email Settings", new String[]{
            "SMTP Configuration",
            "Email Templates",
            "Notification Settings"
        });

        addSettingCategory(panel, gbc, "System Settings", new String[]{
            "Database Configuration",
            "File Storage Settings",
            "Backup Schedule",
            "System Maintenance"
        });

        return panel;
    }

    private void addSettingCategory(JPanel panel, GridBagConstraints gbc, String category, String[] settings) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(HEADER_FONT);
        panel.add(categoryLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 30, 5, 10);

        for (String setting : settings) {
            gbc.gridx = 0;
            panel.add(new JLabel(setting), gbc);
            
            gbc.gridx = 1;
            JButton configBtn = new JButton("Configure");
            styleButton(configBtn);
            panel.add(configBtn, gbc);
            
            gbc.gridy++;
        }

        gbc.insets = new Insets(10, 10, 10, 10);
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Log filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Log Type:"));
        JComboBox<String> logTypeCombo = new JComboBox<>(new String[]{
            "All Logs", "System Logs", "User Logs", "Exam Logs", "Security Logs"
        });
        filterPanel.add(logTypeCombo);

        // Log table
        String[] columns = {"Timestamp", "Type", "User", "Action", "Details"};
        Object[][] data = {
            {"2023-12-30 10:00", "LOGIN", "john@email.com", "Success", "Login from 192.168.1.1"},
            {"2023-12-30 09:55", "SYSTEM", "backup", "Success", "Daily backup completed"},
            {"2023-12-30 09:45", "EXAM", "mary@email.com", "Submit", "Completed Programming Exam"}
        };

        JTable table = new JTable(data, columns);
        table.setFont(NORMAL_FONT);
        table.getTableHeader().setFont(HEADER_FONT);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBackupPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);

        // Backup controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backupNowBtn = new JButton("Backup Now");
        JButton restoreBtn = new JButton("Restore from Backup");
        
        styleButton(backupNowBtn);
        styleButton(restoreBtn);
        
        controlPanel.add(backupNowBtn);
        controlPanel.add(restoreBtn);

        // Backup history
        String[] columns = {"Date", "Time", "Size", "Type", "Status"};
        Object[][] data = {
            {"2023-12-30", "10:00 AM", "2.5 GB", "Full", "Success"},
            {"2023-12-29", "10:00 AM", "2.3 GB", "Full", "Success"},
            {"2023-12-28", "10:00 AM", "2.4 GB", "Full", "Success"}
        };

        JTable table = new JTable(data, columns);
        table.setFont(NORMAL_FONT);
        table.getTableHeader().setFont(HEADER_FONT);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new AdminDashboard("Admin").setVisible(true);
        });
    }
}
