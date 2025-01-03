package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import model.Question;

@WebServlet("/exam/*")
public class ExamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        switch (pathInfo) {
            case "/create":
                showExamCreationForm(request, response);
                break;
            case "/list":
                listExams(request, response);
                break;
            case "/edit":
                editExam(request, response);
                break;
            case "/view":
                viewExam(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        switch (pathInfo) {
            case "/create":
                createExam(request, response);
                break;
            case "/update":
                updateExam(request, response);
                break;
            case "/delete":
                deleteExam(request, response);
                break;
            case "/submit":
                submitExam(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showExamCreationForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/instructor/create-exam.jsp").forward(request, response);
    }

    private void createExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String examTitle = request.getParameter("examTitle");
        String examDescription = request.getParameter("examDescription");
        String duration = request.getParameter("duration");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        
        // Get question details
        String[] questions = request.getParameterValues("question");
        String[] correctAnswers = request.getParameterValues("correctAnswer");
        String[] points = request.getParameterValues("points");
        
        // Get options for multiple choice questions
        Map<Integer, String[]> options = new HashMap<>();
        for (int i = 0; i < questions.length; i++) {
            String[] questionOptions = request.getParameterValues("options_" + i);
            if (questionOptions != null) {
                options.put(i, questionOptions);
            }
        }

        // Validate input
        if (examTitle == null || examTitle.trim().isEmpty() ||
            questions == null || questions.length == 0) {
            request.setAttribute("error", "Please fill in all required fields");
            showExamCreationForm(request, response);
            return;
        }

        try {
            // TODO: Save exam to database
            // For now, just redirect with success message
            response.sendRedirect(request.getContextPath() + 
                "/instructor/exams?created=true&examTitle=" + examTitle);
        } catch (Exception e) {
            request.setAttribute("error", "Error creating exam: " + e.getMessage());
            showExamCreationForm(request, response);
        }
    }

    private void listExams(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Fetch exams from database
        request.getRequestDispatcher("/WEB-INF/views/instructor/exam-list.jsp").forward(request, response);
    }

    private void editExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String examId = request.getParameter("id");
        if (examId == null || examId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // TODO: Fetch exam details from database
        request.getRequestDispatcher("/WEB-INF/views/instructor/edit-exam.jsp").forward(request, response);
    }

    private void updateExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String examId = request.getParameter("id");
        if (examId == null || examId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Similar to createExam but updates existing exam
        // TODO: Update exam in database
        response.sendRedirect(request.getContextPath() + 
            "/instructor/exams?updated=true&id=" + examId);
    }

    private void deleteExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String examId = request.getParameter("id");
        if (examId == null || examId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // TODO: Delete exam from database
        response.sendRedirect(request.getContextPath() + 
            "/instructor/exams?deleted=true&id=" + examId);
    }

    private void viewExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String examId = request.getParameter("id");
        if (examId == null || examId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // TODO: Fetch exam details from database
        request.getRequestDispatcher("/WEB-INF/views/exam-view.jsp").forward(request, response);
    }

    private void submitExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String examId = request.getParameter("id");
        if (examId == null || examId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Get student answers
        Map<String, String> answers = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith("answer_")) {
                String questionId = paramName.substring(7); // Remove "answer_" prefix
                answers.put(questionId, request.getParameter(paramName));
            }
        }

        // TODO: Grade exam and save results to database
        response.sendRedirect(request.getContextPath() + 
            "/student/exams?submitted=true&id=" + examId);
    }
}
