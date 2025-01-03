package controller;

import database.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ResultsAnalyticsController {
    private static ResultsAnalyticsController instance;
    private Connection connection;

    private ResultsAnalyticsController() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultsAnalyticsController getInstance() {
        if (instance == null) {
            instance = new ResultsAnalyticsController();
        }
        return instance;
    }

    public Map<String, Object> getStudentResults(int studentId) {
        Map<String, Object> results = new HashMap<>();
        try {
            String query = "SELECT e.title, e.subject, r.submission_time, r.total_marks_obtained, " +
                          "r.percentage FROM exam_results r " +
                          "JOIN exams e ON r.exam_id = e.id " +
                          "WHERE r.student_id = ? ORDER BY r.submission_time DESC";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            List<Map<String, Object>> examResults = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> exam = new HashMap<>();
                exam.put("title", rs.getString("title"));
                exam.put("subject", rs.getString("subject"));
                exam.put("submissionTime", rs.getTimestamp("submission_time"));
                exam.put("marksObtained", rs.getInt("total_marks_obtained"));
                exam.put("percentage", rs.getDouble("percentage"));
                examResults.add(exam);
            }
            results.put("examResults", examResults);
            
            // Calculate overall statistics
            if (!examResults.isEmpty()) {
                double avgPercentage = examResults.stream()
                    .mapToDouble(exam -> (Double)exam.get("percentage"))
                    .average()
                    .orElse(0.0);
                results.put("averagePercentage", avgPercentage);
                
                double highestPercentage = examResults.stream()
                    .mapToDouble(exam -> (Double)exam.get("percentage"))
                    .max()
                    .orElse(0.0);
                results.put("highestPercentage", highestPercentage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Map<String, Object> getExamAnalytics(int examId) {
        Map<String, Object> analytics = new HashMap<>();
        try {
            // Get overall exam statistics
            String statsQuery = "SELECT COUNT(*) as total_students, " +
                              "AVG(percentage) as avg_percentage, " +
                              "MIN(percentage) as min_percentage, " +
                              "MAX(percentage) as max_percentage " +
                              "FROM exam_results WHERE exam_id = ?";
            PreparedStatement statsStmt = connection.prepareStatement(statsQuery);
            statsStmt.setInt(1, examId);
            ResultSet statsRs = statsStmt.executeQuery();
            
            if (statsRs.next()) {
                analytics.put("totalStudents", statsRs.getInt("total_students"));
                analytics.put("averagePercentage", statsRs.getDouble("avg_percentage"));
                analytics.put("lowestPercentage", statsRs.getDouble("min_percentage"));
                analytics.put("highestPercentage", statsRs.getDouble("max_percentage"));
            }

            // Get question-wise analysis
            String questionQuery = "SELECT q.id, q.question_text, " +
                                 "COUNT(CASE WHEN sea.selected_option = q.correct_option THEN 1 END) as correct_answers, " +
                                 "COUNT(*) as total_attempts " +
                                 "FROM questions q " +
                                 "JOIN exam_questions eq ON q.id = eq.question_id " +
                                 "JOIN student_exam_answers sea ON q.id = sea.question_id " +
                                 "WHERE eq.exam_id = ? " +
                                 "GROUP BY q.id";
            PreparedStatement questionStmt = connection.prepareStatement(questionQuery);
            questionStmt.setInt(1, examId);
            ResultSet questionRs = questionStmt.executeQuery();
            
            List<Map<String, Object>> questionAnalytics = new ArrayList<>();
            while (questionRs.next()) {
                Map<String, Object> questionStat = new HashMap<>();
                questionStat.put("questionId", questionRs.getInt("id"));
                questionStat.put("questionText", questionRs.getString("question_text"));
                int correctAnswers = questionRs.getInt("correct_answers");
                int totalAttempts = questionRs.getInt("total_attempts");
                questionStat.put("correctAnswers", correctAnswers);
                questionStat.put("totalAttempts", totalAttempts);
                questionStat.put("successRate", (double)correctAnswers / totalAttempts * 100);
                questionAnalytics.add(questionStat);
            }
            analytics.put("questionAnalytics", questionAnalytics);

            // Get performance distribution
            String distributionQuery = "SELECT " +
                                     "COUNT(CASE WHEN percentage >= 90 THEN 1 END) as excellent, " +
                                     "COUNT(CASE WHEN percentage >= 75 AND percentage < 90 THEN 1 END) as good, " +
                                     "COUNT(CASE WHEN percentage >= 60 AND percentage < 75 THEN 1 END) as average, " +
                                     "COUNT(CASE WHEN percentage < 60 THEN 1 END) as poor " +
                                     "FROM exam_results WHERE exam_id = ?";
            PreparedStatement distStmt = connection.prepareStatement(distributionQuery);
            distStmt.setInt(1, examId);
            ResultSet distRs = distStmt.executeQuery();
            
            if (distRs.next()) {
                Map<String, Integer> distribution = new HashMap<>();
                distribution.put("excellent", distRs.getInt("excellent"));
                distribution.put("good", distRs.getInt("good"));
                distribution.put("average", distRs.getInt("average"));
                distribution.put("poor", distRs.getInt("poor"));
                analytics.put("performanceDistribution", distribution);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analytics;
    }

    public List<Map<String, Object>> getTopPerformers(int examId, int limit) {
        List<Map<String, Object>> topPerformers = new ArrayList<>();
        try {
            String query = "SELECT u.full_name, u.email, r.total_marks_obtained, r.percentage " +
                          "FROM exam_results r " +
                          "JOIN users u ON r.student_id = u.id " +
                          "WHERE r.exam_id = ? " +
                          "ORDER BY r.percentage DESC LIMIT ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, examId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> performer = new HashMap<>();
                performer.put("fullName", rs.getString("full_name"));
                performer.put("email", rs.getString("email"));
                performer.put("marksObtained", rs.getInt("total_marks_obtained"));
                performer.put("percentage", rs.getDouble("percentage"));
                topPerformers.add(performer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topPerformers;
    }
}
