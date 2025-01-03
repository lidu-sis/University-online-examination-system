package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/exam_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root"; // Change this to your MySQL password
    
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            if (connection.isValid(5)) {
                System.out.println("Database connection established successfully!");
            }
        } catch (ClassNotFoundException e) {
            showError("MySQL JDBC Driver not found.\nPlease add the MySQL JDBC driver to your project.");
            System.exit(1);
        } catch (SQLException e) {
            showError("Database connection failed!\nError: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException e) {
                showError("Failed to get database connection!\nError: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    private void showError(String message) {
        System.err.println(message);
        JOptionPane.showMessageDialog(null, message, "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
