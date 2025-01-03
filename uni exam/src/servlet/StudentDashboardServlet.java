package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/student/*")
public class StudentDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is a student
        if (session == null || session.getAttribute("username") == null || 
            !"student".equals(session.getAttribute("role"))) {
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
            case "/results":
                showResults(request, response);
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
        request.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(request, response);
    }

    private void showExams(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch available exams
        request.getRequestDispatcher("/WEB-INF/views/student/exams.jsp").forward(request, response);
    }

    private void showResults(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch exam results
        request.getRequestDispatcher("/WEB-INF/views/student/results.jsp").forward(request, response);
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch user profile
        request.getRequestDispatcher("/WEB-INF/views/student/profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("username") == null || 
            !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        switch (pathInfo) {
            case "/start-exam":
                startExam(request, response);
                break;
            case "/submit-exam":
                submitExam(request, response);
                break;
            case "/update-profile":
                updateProfile(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void startExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement exam start logic
        String examId = request.getParameter("examId");
        // Add logic to start exam
        response.sendRedirect(request.getContextPath() + "/student/exam?id=" + examId);
    }

    private void submitExam(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement exam submission logic
        String examId = request.getParameter("examId");
        // Add logic to submit exam
        response.sendRedirect(request.getContextPath() + "/student/results?examId=" + examId);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement profile update logic
        response.sendRedirect(request.getContextPath() + "/student/profile?updated=true");
    }
}
