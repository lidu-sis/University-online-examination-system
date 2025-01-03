package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import database.DatabaseConnection;

public class AuditLogger {
    private static AuditLogger instance;
    private Connection connection;

    private AuditLogger() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    public void logAction(int userId, String action, String details) {
        try {
            String query = "INSERT INTO audit_logs (user_id, action, details, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            pstmt.setObject(4, LocalDateTime.now());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logLoginAttempt(String username, boolean success, String ipAddress) {
        try {
            String query = "INSERT INTO login_attempts (username, success, ip_address, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setBoolean(2, success);
            pstmt.setString(3, ipAddress);
            pstmt.setObject(4, LocalDateTime.now());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logExamActivity(int userId, int examId, String activity) {
        try {
            String query = "INSERT INTO exam_logs (user_id, exam_id, activity, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, examId);
            pstmt.setString(3, activity);
            pstmt.setObject(4, LocalDateTime.now());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
