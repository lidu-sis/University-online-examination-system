package com.university.exam.servlet;

import com.university.exam.dao.ExamDAO;
import com.university.exam.model.Exam;
import com.university.exam.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/teacher/create-exam")
public class CreateExamServlet extends HttpServlet {
    private ExamDAO examDAO = new ExamDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"teacher".equals(user.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        
        try {
            Exam exam = new Exam();
            exam.setCourseId(Integer.parseInt(request.getParameter("courseId")));
            exam.setTitle(request.getParameter("title"));
            exam.setDescription(request.getParameter("description"));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date startTime = dateFormat.parse(request.getParameter("startTime"));
            exam.setStartTime(startTime);
            
            exam.setDurationMinutes(Integer.parseInt(request.getParameter("duration")));
            exam.setStatus(request.getParameter("status") != null ? 
                         request.getParameter("status") : "published");
            exam.setCreatedBy(user.getId());
            
            // Calculate total marks based on questions
            int totalMarks = 0;
            String[] questionMarks = request.getParameterValues("questions[].marks");
            if (questionMarks != null) {
                for (String marks : questionMarks) {
                    totalMarks += Integer.parseInt(marks);
                }
            }
            exam.setTotalMarks(totalMarks);
            
            // Create the exam
            exam = examDAO.createExam(exam);
            
            // Add questions and options
            // This part would need to be implemented based on your Question and Option models
            
            response.sendRedirect("my-exams?success=true");
            
        } catch (ParseException e) {
            request.setAttribute("error", "Invalid date format");
            request.getRequestDispatcher("create-exam.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error creating exam: " + e.getMessage());
            request.getRequestDispatcher("create-exam.jsp").forward(request, response);
        }
    }
}
