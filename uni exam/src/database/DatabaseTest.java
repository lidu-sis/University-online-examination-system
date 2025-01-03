package database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        DatabaseConnection dbConnection = null;
        Connection conn = null;
        
        try {
            // Get database connection
            dbConnection = DatabaseConnection.getInstance();
            conn = dbConnection.getConnection();
            System.out.println("Connected successfully!");
            
            // Test if connection is valid
            if (conn.isValid(5)) {
                System.out.println("Connection is valid and working!");
                
                // Test a simple query
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
                    if (rs.next()) {
                        System.out.println("Number of users in database: " + rs.getInt(1));
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
    }
}
