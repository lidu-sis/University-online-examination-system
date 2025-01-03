package com.university.exam.dao;

import com.university.exam.model.Exam;
import com.university.exam.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {
    
    public List<Exam> getUpcomingExams(int studentId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT e.* FROM exams e " +
                    "JOIN courses c ON e.course_id = c.id " +
                    "JOIN enrollments en ON c.id = en.course_id " +
                    "WHERE en.student_id = ? AND e.status = 'published' " +
                    "AND e.start_time > NOW() ORDER BY e.start_time";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Exam exam = new Exam();
                    exam.setId(rs.getInt("id"));
                    exam.setTitle(rs.getString("title"));
                    exam.setDescription(rs.getString("description"));
                    exam.setStartTime(rs.getTimestamp("start_time"));
                    exam.setDurationMinutes(rs.getInt("duration_minutes"));
                    exam.setTotalMarks(rs.getInt("total_marks"));
                    exam.setStatus(rs.getString("status"));
                    exams.add(exam);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }
    
    public Exam createExam(Exam exam) {
        String sql = "INSERT INTO exams (course_id, title, description, start_time, " +
                    "duration_minutes, total_marks, status, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, exam.getCourseId());
            stmt.setString(2, exam.getTitle());
            stmt.setString(3, exam.getDescription());
            stmt.setTimestamp(4, new Timestamp(exam.getStartTime().getTime()));
            stmt.setInt(5, exam.getDurationMinutes());
            stmt.setInt(6, exam.getTotalMarks());
            stmt.setString(7, exam.getStatus());
            stmt.setInt(8, exam.getCreatedBy());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        exam.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exam;
    }
    
    public boolean updateExamStatus(int examId, String status) {
        String sql = "UPDATE exams SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, examId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
