package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/*")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is an admin
        if (session == null || session.getAttribute("username") == null || 
            !"admin".equals(session.getAttribute("role"))) {
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
            case "/users":
                showUsers(request, response);
                break;
            case "/system":
                showSystemSettings(request, response);
                break;
            case "/logs":
                showLogs(request, response);
                break;
            case "/backup":
                showBackup(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch admin dashboard data
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    private void showUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch all users
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }

    private void showSystemSettings(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch system settings
        request.getRequestDispatcher("/WEB-INF/views/admin/system.jsp").forward(request, response);
    }

    private void showLogs(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to fetch system logs
        request.getRequestDispatcher("/WEB-INF/views/admin/logs.jsp").forward(request, response);
    }

    private void showBackup(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Add logic to show backup options
        request.getRequestDispatcher("/WEB-INF/views/admin/backup.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("username") == null || 
            !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        switch (pathInfo) {
            case "/create-user":
                createUser(request, response);
                break;
            case "/edit-user":
                editUser(request, response);
                break;
            case "/delete-user":
                deleteUser(request, response);
                break;
            case "/update-settings":
                updateSettings(request, response);
                break;
            case "/create-backup":
                createBackup(request, response);
                break;
            case "/restore-backup":
                restoreBackup(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement user creation logic
        response.sendRedirect(request.getContextPath() + "/admin/users?created=true");
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement user editing logic
        String userId = request.getParameter("userId");
        response.sendRedirect(request.getContextPath() + "/admin/users?edited=" + userId);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement user deletion logic
        String userId = request.getParameter("userId");
        response.sendRedirect(request.getContextPath() + "/admin/users?deleted=" + userId);
    }

    private void updateSettings(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement settings update logic
        response.sendRedirect(request.getContextPath() + "/admin/system?updated=true");
    }

    private void createBackup(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement backup creation logic
        response.sendRedirect(request.getContextPath() + "/admin/backup?created=true");
    }

    private void restoreBackup(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement backup restoration logic
        String backupId = request.getParameter("backupId");
        response.sendRedirect(request.getContextPath() + "/admin/backup?restored=" + backupId);
    }
}
