package controller;

import model.Exam;
import model.Question;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ExamController {
    private static ExamController instance;
    private Connection connection;

    private ExamController() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ExamController getInstance() {
        if (instance == null) {
            instance = new ExamController();
        }
        return instance;
    }

    public boolean createExam(Exam exam) {
        try {
            String query = "INSERT INTO exams (title, subject, start_time, duration, total_marks, created_by) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, exam.getTitle());
            pstmt.setString(2, exam.getSubject());
            pstmt.setTimestamp(3, new Timestamp(exam.getStartTime().getTime()));
            pstmt.setInt(4, exam.getDuration());
            pstmt.setInt(5, exam.getTotalMarks());
            pstmt.setInt(6, 1); // Replace with actual teacher ID

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int examId = rs.getInt(1);
                    return addQuestionsToExam(examId, exam.getQuestions());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean addQuestionsToExam(int examId, List<Question> questions) {
        try {
            String query = "INSERT INTO exam_questions (exam_id, question_id, question_number) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            
            for (int i = 0; i < questions.size(); i++) {
                pstmt.setInt(1, examId);
                pstmt.setInt(2, questions.get(i).getId());
                pstmt.setInt(3, i + 1);
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            return results.length == questions.size();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Exam> getAvailableExams(int studentId) {
        List<Exam> exams = new ArrayList<>();
        try {
            String query = "SELECT * FROM exams WHERE start_time > NOW() AND is_active = true";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Exam exam = new Exam(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("subject"),
                    new Date(rs.getTimestamp("start_time").getTime()),
                    rs.getInt("duration")
                );
                exams.add(exam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public List<Question> getExamQuestions(int examId) {
        List<Question> questions = new ArrayList<>();
        try {
            String query = "SELECT q.* FROM questions q " +
                          "JOIN exam_questions eq ON q.id = eq.question_id " +
                          "WHERE eq.exam_id = ? ORDER BY eq.question_number";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, examId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                List<String> options = new ArrayList<>();
                options.add(rs.getString("option1"));
                options.add(rs.getString("option2"));
                options.add(rs.getString("option3"));
                options.add(rs.getString("option4"));

                Question question = new Question(
                    rs.getInt("id"),
                    rs.getString("question_text"),
                    options,
                    rs.getInt("correct_option"),
                    rs.getInt("marks"),
                    rs.getString("subject"),
                    rs.getString("difficulty")
                );
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public boolean submitExam(int studentId, int examId, List<Integer> answers) {
        try {
            // Start transaction
            connection.setAutoCommit(false);
            
            // Insert answers
            String answerQuery = "INSERT INTO student_exam_answers (student_id, exam_id, question_id, selected_option, marks_obtained) " +
                                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement answerStmt = connection.prepareStatement(answerQuery);
            
            List<Question> questions = getExamQuestions(examId);
            int totalMarksObtained = 0;
            
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                int selectedOption = answers.get(i);
                int marksObtained = (selectedOption == question.getCorrectOption()) ? question.getMarks() : 0;
                totalMarksObtained += marksObtained;
                
                answerStmt.setInt(1, studentId);
                answerStmt.setInt(2, examId);
                answerStmt.setInt(3, question.getId());
                answerStmt.setInt(4, selectedOption);
                answerStmt.setInt(5, marksObtained);
                answerStmt.addBatch();
            }
            answerStmt.executeBatch();
            
            // Insert result
            String resultQuery = "INSERT INTO exam_results (student_id, exam_id, total_marks_obtained, percentage) " +
                               "VALUES (?, ?, ?, ?)";
            PreparedStatement resultStmt = connection.prepareStatement(resultQuery);
            resultStmt.setInt(1, studentId);
            resultStmt.setInt(2, examId);
            resultStmt.setInt(3, totalMarksObtained);
            
            int totalMarks = questions.stream().mapToInt(Question::getMarks).sum();
            double percentage = (totalMarksObtained * 100.0) / totalMarks;
            resultStmt.setDouble(4, percentage);
            
            resultStmt.executeUpdate();
            
            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
