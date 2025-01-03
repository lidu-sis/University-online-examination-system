package test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.*;
import controller.*;
import util.*;
import java.util.*;
import java.time.LocalDateTime;

public class TestSuite {
    private AuthenticationController authController;
    private ExamController examController;
    private QuestionBankController questionController;
    private ResultsAnalyticsController analyticsController;

    @BeforeEach
    void setUp() {
        authController = AuthenticationController.getInstance();
        examController = ExamController.getInstance();
        questionController = QuestionBankController.getInstance();
        analyticsController = ResultsAnalyticsController.getInstance();
    }

    @Test
    void testUserAuthentication() {
        // Test user registration
        User newUser = new User(0, "testuser", "Test@123", "STUDENT", "Test User", "test@example.com");
        assertTrue(authController.registerUser(newUser));

        // Test login with correct credentials
        User loggedInUser = authController.login("testuser", "Test@123");
        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.getUsername());

        // Test login with incorrect credentials
        User invalidLogin = authController.login("testuser", "wrongpassword");
        assertNull(invalidLogin);
    }

    @Test
    void testPasswordSecurity() {
        String password = "Test@123";
        String salt = SecurityUtil.generateSalt();
        String hash = SecurityUtil.hashPassword(password, salt);

        // Test password validation
        assertTrue(SecurityUtil.validatePassword(password, salt, hash));
        assertFalse(SecurityUtil.validatePassword("wrongpassword", salt, hash));

        // Test password strength
        assertTrue(SecurityUtil.isPasswordStrong("Test@123"));
        assertFalse(SecurityUtil.isPasswordStrong("weak"));
    }

    @Test
    void testQuestionBank() {
        // Test adding a question
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4");
        Question question = new Question(0, "Test question?", options, 2, 5, "Mathematics", "Medium");
        assertTrue(questionController.addQuestion(question));

        // Test retrieving questions
        List<Question> mathQuestions = questionController.getQuestionsBySubject("Mathematics");
        assertFalse(mathQuestions.isEmpty());
        assertEquals("Test question?", mathQuestions.get(0).getQuestionText());

        // Test searching questions
        List<Question> searchResults = questionController.searchQuestions("test");
        assertFalse(searchResults.isEmpty());
    }

    @Test
    void testExamManagement() {
        // Create test questions
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4");
        Question q1 = new Question(1, "Q1?", options, 1, 5, "Mathematics", "Easy");
        Question q2 = new Question(2, "Q2?", options, 2, 5, "Mathematics", "Medium");
        List<Question> questions = Arrays.asList(q1, q2);

        // Test exam creation
        Exam exam = new Exam(0, "Test Exam", "Mathematics", new Date(), 60);
        exam.setQuestions(questions);
        assertTrue(examController.createExam(exam));

        // Test retrieving available exams
        List<Exam> availableExams = examController.getAvailableExams(1);
        assertFalse(availableExams.isEmpty());

        // Test exam submission
        List<Integer> answers = Arrays.asList(1, 2);
        assertTrue(examController.submitExam(1, exam.getId(), answers));
    }

    @Test
    void testResultsAnalytics() {
        // Test getting student results
        Map<String, Object> results = analyticsController.getStudentResults(1);
        assertNotNull(results);
        assertNotNull(results.get("examResults"));

        // Test exam analytics
        Map<String, Object> analytics = analyticsController.getExamAnalytics(1);
        assertNotNull(analytics);
        assertNotNull(analytics.get("questionAnalytics"));
        assertNotNull(analytics.get("performanceDistribution"));

        // Test top performers
        List<Map<String, Object>> topPerformers = analyticsController.getTopPerformers(1, 5);
        assertNotNull(topPerformers);
        assertTrue(topPerformers.size() <= 5);
    }

    @Test
    void testSessionManagement() {
        User user = new User(1, "testuser", "password", "STUDENT", "Test User", "test@example.com");
        
        // Test session creation
        String token = SessionManager.getInstance().createSession(user);
        assertNotNull(token);

        // Test getting user from valid session
        User sessionUser = SessionManager.getInstance().getUser(token);
        assertNotNull(sessionUser);
        assertEquals(user.getId(), sessionUser.getId());

        // Test session invalidation
        SessionManager.getInstance().invalidateSession(token);
        assertNull(SessionManager.getInstance().getUser(token));
    }

    @Test
    void testAuditLogging() {
        AuditLogger logger = AuditLogger.getInstance();

        // Test action logging
        logger.logAction(1, "TEST_ACTION", "Test action details");

        // Test login attempt logging
        logger.logLoginAttempt("testuser", true, "127.0.0.1");

        // Test exam activity logging
        logger.logExamActivity(1, 1, "Started exam");
    }
}
