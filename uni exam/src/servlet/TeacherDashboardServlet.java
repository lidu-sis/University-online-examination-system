package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/teacher/*")
public class TeacherDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is a teacher
        if (session == null || session.getAttribute("username") == null || 
            !"teacher".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        switch (pathInfo) {
            case "/":
            case "/dashboard":
                showDashboard(request, response);
                break;
            case "/exams":
                showExams(request, response);
                break;
            case "/students":
                showStudents(request, response);
                break;
            case "/questions":
                showQuestions(request, response);
                break;
            case "/profile":
                showProfile(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch dashboard data
        request.getRequestDispatcher("/WEB-INF/views/teacher/dashboard.jsp").forward(request, response);
    }

    private void showExams(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch created exams
        request.getRequestDispatcher("/WEB-INF/views/teacher/exams.jsp").forward(request, response);
    }

    private void showStudents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch student list
        request.getRequestDispatcher("/WEB-INF/views/teacher/students.jsp").forward(request, response);
    }

    private void showQuestions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch question bank
        request.getRequestDispatcher("/WEB-INF/views/teacher/questions.jsp").forward(request, response);
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch teacher profile
        request.getRequestDispatcher("/WEB-INF/views/teacher/profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("username") == null || 
            !"teacher".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        switch (pathInfo) {
            case "/create-exam":
                createExam(request, response);
                break;
            case "/edit-exam":
                editExam(request, response);
                break;
            case "/add-question":
                addQuestion(request, response);
                break;
            case "/grade-exam":
                gradeExam(request, response);
                break;
            case "/update-profile":
                updateProfile(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement exam creation logic
        response.sendRedirect(request.getContextPath() + "/teacher/exams?created=true");
    }

    private void editExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement exam editing logic
        String examId = request.getParameter("examId");
        response.sendRedirect(request.getContextPath() + "/teacher/exams?edited=" + examId);
    }

    private void addQuestion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement question addition logic
        response.sendRedirect(request.getContextPath() + "/teacher/questions?added=true");
    }

    private void gradeExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement exam grading logic
        String examId = request.getParameter("examId");
        String studentId = request.getParameter("studentId");
        response.sendRedirect(request.getContextPath() + 
            "/teacher/exams/grades?examId=" + examId + "&studentId=" + studentId);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement profile update logic
        response.sendRedirect(request.getContextPath() + "/teacher/profile?updated=true");
    }
}
