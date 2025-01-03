package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get registration form data
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");

        // Validate input
        if (!validateInput(fullName, email, username, password, confirmPassword)) {
            request.setAttribute("error", "Please fill all fields correctly");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        try {
            // TODO: Add proper user registration logic with database
            boolean registrationSuccess = registerUser(fullName, email, username, password, role);

            if (registrationSuccess) {
                // Redirect to login page with success message
                response.sendRedirect(request.getContextPath() + "/login?registration=success");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred during registration");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }

    private boolean validateInput(String fullName, String email, String username, 
            String password, String confirmPassword) {
        return fullName != null && !fullName.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() && email.contains("@") &&
               username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               confirmPassword != null && !confirmPassword.trim().isEmpty();
    }

    private boolean registerUser(String fullName, String email, String username, 
            String password, String role) {
        // TODO: Implement proper user registration with database
        // This is just a temporary implementation
        return true;
    }
}
