package analytics;

import database.DatabaseConnection;
import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AdvancedAnalytics {
    private Connection connection;

    public AdvancedAnalytics() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getStudentProgressAnalysis(int studentId) {
        Map<String, Object> analysis = new HashMap<>();
        try {
            // Get performance trend over time
            String trendQuery = "SELECT e.subject, r.percentage, r.submission_time " +
                              "FROM exam_results r " +
                              "JOIN exams e ON r.exam_id = e.id " +
                              "WHERE r.student_id = ? " +
                              "ORDER BY r.submission_time";
            PreparedStatement trendStmt = connection.prepareStatement(trendQuery);
            trendStmt.setInt(1, studentId);
            ResultSet trendRs = trendStmt.executeQuery();

            Map<String, List<Map<String, Object>>> subjectTrends = new HashMap<>();
            while (trendRs.next()) {
                String subject = trendRs.getString("subject");
                subjectTrends.computeIfAbsent(subject, k -> new ArrayList<>())
                    .add(Map.of(
                        "percentage", trendRs.getDouble("percentage"),
                        "date", trendRs.getTimestamp("submission_time")
                    ));
            }
            analysis.put("performanceTrends", subjectTrends);

            // Calculate improvement rates
            Map<String, Double> improvementRates = new HashMap<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : subjectTrends.entrySet()) {
                List<Map<String, Object>> scores = entry.getValue();
                if (scores.size() >= 2) {
                    double firstScore = (double) scores.get(0).get("percentage");
                    double lastScore = (double) scores.get(scores.size() - 1).get("percentage");
                    double improvement = ((lastScore - firstScore) / firstScore) * 100;
                    improvementRates.put(entry.getKey(), improvement);
                }
            }
            analysis.put("improvementRates", improvementRates);

            // Get strength and weakness analysis
            String strengthQuery = "SELECT q.subject, q.difficulty, " +
                                 "COUNT(CASE WHEN sea.selected_option = q.correct_option THEN 1 END) as correct, " +
                                 "COUNT(*) as total " +
                                 "FROM student_exam_answers sea " +
                                 "JOIN questions q ON sea.question_id = q.id " +
                                 "WHERE sea.student_id = ? " +
                                 "GROUP BY q.subject, q.difficulty";
            PreparedStatement strengthStmt = connection.prepareStatement(strengthQuery);
            strengthStmt.setInt(1, studentId);
            ResultSet strengthRs = strengthStmt.executeQuery();

            Map<String, Map<String, Double>> strengthAnalysis = new HashMap<>();
            while (strengthRs.next()) {
                String subject = strengthRs.getString("subject");
                String difficulty = strengthRs.getString("difficulty");
                int correct = strengthRs.getInt("correct");
                int total = strengthRs.getInt("total");
                double accuracy = (double) correct / total * 100;

                strengthAnalysis.computeIfAbsent(subject, k -> new HashMap<>())
                    .put(difficulty, accuracy);
            }
            analysis.put("strengthAnalysis", strengthAnalysis);

            // Get time management analysis
            String timeQuery = "SELECT e.duration, er.submission_time, e.start_time " +
                             "FROM exam_results er " +
                             "JOIN exams e ON er.exam_id = e.id " +
                             "WHERE er.student_id = ?";
            PreparedStatement timeStmt = connection.prepareStatement(timeQuery);
            timeStmt.setInt(1, studentId);
            ResultSet timeRs = timeStmt.executeQuery();

            List<Map<String, Object>> timeAnalysis = new ArrayList<>();
            while (timeRs.next()) {
                int duration = timeRs.getInt("duration");
                LocalDateTime startTime = timeRs.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime submissionTime = timeRs.getTimestamp("submission_time").toLocalDateTime();
                long timeSpent = ChronoUnit.MINUTES.between(startTime, submissionTime);

                timeAnalysis.add(Map.of(
                    "duration", duration,
                    "timeSpent", timeSpent,
                    "timeEfficiency", ((double) timeSpent / duration) * 100
                ));
            }
            analysis.put("timeManagement", timeAnalysis);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analysis;
    }

    public Map<String, Object> getPredictiveAnalysis(int studentId) {
        Map<String, Object> predictions = new HashMap<>();
        try {
            // Get past performance data
            String query = "SELECT e.subject, r.percentage " +
                          "FROM exam_results r " +
                          "JOIN exams e ON r.exam_id = e.id " +
                          "WHERE r.student_id = ? " +
                          "ORDER BY r.submission_time";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            Map<String, List<Double>> subjectScores = new HashMap<>();
            while (rs.next()) {
                String subject = rs.getString("subject");
                subjectScores.computeIfAbsent(subject, k -> new ArrayList<>())
                    .add(rs.getDouble("percentage"));
            }

            // Calculate predicted scores using simple linear regression
            Map<String, Double> predictedScores = new HashMap<>();
            for (Map.Entry<String, List<Double>> entry : subjectScores.entrySet()) {
                List<Double> scores = entry.getValue();
                if (scores.size() >= 2) {
                    double prediction = calculateLinearRegression(scores);
                    predictedScores.put(entry.getKey(), prediction);
                }
            }
            predictions.put("predictedScores", predictedScores);

            // Calculate success probability
            Map<String, Double> successProbability = new HashMap<>();
            for (Map.Entry<String, List<Double>> entry : subjectScores.entrySet()) {
                List<Double> scores = entry.getValue();
                if (!scores.isEmpty()) {
                    double avgScore = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    double probability = calculateSuccessProbability(avgScore);
                    successProbability.put(entry.getKey(), probability);
                }
            }
            predictions.put("successProbability", successProbability);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return predictions;
    }

    private double calculateLinearRegression(List<Double> scores) {
        int n = scores.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        
        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += scores.get(i);
            sumXY += i * scores.get(i);
            sumX2 += i * i;
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        
        return slope * n + intercept; // Prediction for next exam
    }

    private double calculateSuccessProbability(double avgScore) {
        // Simple logistic function to convert average score to probability
        return 1 / (1 + Math.exp(-0.1 * (avgScore - 50)));
    }
}
