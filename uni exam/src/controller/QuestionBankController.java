package controller;

import model.Question;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionBankController {
    private static QuestionBankController instance;
    private Connection connection;

    private QuestionBankController() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static QuestionBankController getInstance() {
        if (instance == null) {
            instance = new QuestionBankController();
        }
        return instance;
    }

    public boolean addQuestion(Question question) {
        try {
            String query = "INSERT INTO questions (question_text, option1, option2, option3, option4, " +
                          "correct_option, marks, subject, difficulty, created_by) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            
            pstmt.setString(1, question.getQuestionText());
            List<String> options = question.getOptions();
            for (int i = 0; i < 4; i++) {
                pstmt.setString(i + 2, options.get(i));
            }
            pstmt.setInt(6, question.getCorrectOption());
            pstmt.setInt(7, question.getMarks());
            pstmt.setString(8, question.getSubject());
            pstmt.setString(9, question.getDifficulty());
            pstmt.setInt(10, 1); // Replace with actual teacher ID

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateQuestion(Question question) {
        try {
            String query = "UPDATE questions SET question_text = ?, option1 = ?, option2 = ?, " +
                          "option3 = ?, option4 = ?, correct_option = ?, marks = ?, " +
                          "subject = ?, difficulty = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            
            pstmt.setString(1, question.getQuestionText());
            List<String> options = question.getOptions();
            for (int i = 0; i < 4; i++) {
                pstmt.setString(i + 2, options.get(i));
            }
            pstmt.setInt(6, question.getCorrectOption());
            pstmt.setInt(7, question.getMarks());
            pstmt.setString(8, question.getSubject());
            pstmt.setString(9, question.getDifficulty());
            pstmt.setInt(10, question.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteQuestion(int questionId) {
        try {
            // First check if the question is used in any exam
            String checkQuery = "SELECT COUNT(*) FROM exam_questions WHERE question_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, questionId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Question is in use, cannot delete
            }

            // If not in use, delete the question
            String deleteQuery = "DELETE FROM questions WHERE id = ?";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, questionId);
            
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Question> getQuestionsBySubject(String subject) {
        List<Question> questions = new ArrayList<>();
        try {
            String query = "SELECT * FROM questions WHERE subject = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, subject);
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

    public List<Question> searchQuestions(String searchTerm) {
        List<Question> questions = new ArrayList<>();
        try {
            String query = "SELECT * FROM questions WHERE question_text LIKE ? OR subject LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
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
}
