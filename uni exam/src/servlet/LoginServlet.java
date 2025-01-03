package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to login page
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // TODO: Add proper authentication logic here
        boolean isAuthenticated = authenticate(username, password, role);

        if (isAuthenticated) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);

            // Redirect based on role
            switch (role.toLowerCase()) {
                case "student":
                    response.sendRedirect(request.getContextPath() + "/student/dashboard");
                    break;
                case "teacher":
                    response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
                    break;
                case "admin":
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/login?error=invalid_role");
            }
        } else {
            // Authentication failed
            response.sendRedirect(request.getContextPath() + "/login?error=invalid_credentials");
        }
    }

    private boolean authenticate(String username, String password, String role) {
        // TODO: Implement proper authentication logic with database
        // This is just a temporary implementation
        return !username.isEmpty() && !password.isEmpty() && !role.isEmpty();
    }
}
