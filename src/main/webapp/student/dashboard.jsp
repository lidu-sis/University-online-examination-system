<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Dashboard - Online Examination System</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <link rel="stylesheet" type="text/css" href="../css/dashboard.css">
</head>
<body>
    <div class="dashboard-container">
        <nav class="sidebar">
            <div class="user-info">
                <h3>Welcome, ${user.fullName}</h3>
                <p>Student Dashboard</p>
            </div>
            <ul>
                <li><a href="#" class="active">Dashboard</a></li>
                <li><a href="upcoming-exams">Upcoming Exams</a></li>
                <li><a href="my-courses">My Courses</a></li>
                <li><a href="exam-history">Exam History</a></li>
                <li><a href="../logout">Logout</a></li>
            </ul>
        </nav>
        
        <main class="content">
            <div class="dashboard-header">
                <h2>My Dashboard</h2>
            </div>
            
            <div class="dashboard-stats">
                <div class="stat-card">
                    <h3>Upcoming Exams</h3>
                    <p class="stat-number">3</p>
                </div>
                <div class="stat-card">
                    <h3>Courses Enrolled</h3>
                    <p class="stat-number">5</p>
                </div>
                <div class="stat-card">
                    <h3>Completed Exams</h3>
                    <p class="stat-number">12</p>
                </div>
            </div>
            
            <div class="upcoming-exams">
                <h3>Next Scheduled Exams</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>Exam Title</th>
                            <th>Date</th>
                            <th>Duration</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${upcomingExams}" var="exam">
                            <tr>
                                <td>${exam.courseName}</td>
                                <td>${exam.title}</td>
                                <td>${exam.startTime}</td>
                                <td>${exam.duration} mins</td>
                                <td>
                                    <c:if test="${exam.status == 'ongoing'}">
                                        <a href="take-exam?id=${exam.id}" class="btn-take-exam">Take Exam</a>
                                    </c:if>
                                    <c:if test="${exam.status != 'ongoing'}">
                                        <span class="exam-pending">Pending</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
    <script src="../js/dashboard.js"></script>
</body>
</html>
