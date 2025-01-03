package view;

import javax.swing.SwingUtilities;

public class TeacherDashboardRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TeacherDashboard dashboard = new TeacherDashboard("Teacher");
            dashboard.setVisible(true);
        });
    }
}
