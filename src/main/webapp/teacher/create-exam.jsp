<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create Exam - Online Examination System</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <link rel="stylesheet" type="text/css" href="../css/dashboard.css">
</head>
<body>
    <div class="dashboard-container">
        <nav class="sidebar">
            <div class="user-info">
                <h3>Welcome, ${user.fullName}</h3>
                <p>Teacher Dashboard</p>
            </div>
            <ul>
                <li><a href="dashboard.jsp">Dashboard</a></li>
                <li><a href="create-exam.jsp" class="active">Create Exam</a></li>
                <li><a href="my-exams">My Exams</a></li>
                <li><a href="grade-exams">Grade Exams</a></li>
                <li><a href="../logout">Logout</a></li>
            </ul>
        </nav>
        
        <main class="content">
            <div class="dashboard-header">
                <h2>Create New Exam</h2>
            </div>
            
            <div class="exam-form">
                <form action="create-exam" method="post" id="examForm">
                    <div class="form-group">
                        <label for="course">Select Course:</label>
                        <select id="course" name="courseId" required>
                            <c:forEach items="${courses}" var="course">
                                <option value="${course.id}">${course.courseName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="title">Exam Title:</label>
                        <input type="text" id="title" name="title" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea id="description" name="description" rows="4"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="startTime">Start Time:</label>
                        <input type="datetime-local" id="startTime" name="startTime" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="duration">Duration (minutes):</label>
                        <input type="number" id="duration" name="duration" required>
                    </div>
                    
                    <div id="questions-container">
                        <h3>Questions</h3>
                        <button type="button" onclick="addQuestion()" class="btn-add">Add Question</button>
                        
                        <div class="question-list">
                            <!-- Questions will be added here dynamically -->
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-primary">Create Exam</button>
                        <button type="button" onclick="saveAsDraft()" class="btn-secondary">Save as Draft</button>
                    </div>
                </form>
            </div>
        </main>
    </div>
    
    <script>
        function addQuestion() {
            const questionList = document.querySelector('.question-list');
            const questionNumber = questionList.children.length + 1;
            
            const questionDiv = document.createElement('div');
            questionDiv.className = 'question-item';
            questionDiv.innerHTML = `
                <h4>Question ${questionNumber}</h4>
                <div class="form-group">
                    <label>Question Text:</label>
                    <textarea name="questions[${questionNumber}].text" required></textarea>
                </div>
                <div class="form-group">
                    <label>Question Type:</label>
                    <select name="questions[${questionNumber}].type" onchange="handleQuestionType(this)">
                        <option value="multiple_choice">Multiple Choice</option>
                        <option value="true_false">True/False</option>
                        <option value="essay">Essay</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Marks:</label>
                    <input type="number" name="questions[${questionNumber}].marks" required>
                </div>
                <div class="options-container">
                    <!-- Options will be added here based on question type -->
                </div>
                <button type="button" onclick="removeQuestion(this)" class="btn-remove">Remove Question</button>
            `;
            
            questionList.appendChild(questionDiv);
        }
        
        function removeQuestion(button) {
            button.parentElement.remove();
            updateQuestionNumbers();
        }
        
        function updateQuestionNumbers() {
            const questions = document.querySelectorAll('.question-item');
            questions.forEach((question, index) => {
                question.querySelector('h4').textContent = `Question ${index + 1}`;
            });
        }
        
        function handleQuestionType(select) {
            const optionsContainer = select.parentElement.parentElement.querySelector('.options-container');
            optionsContainer.innerHTML = '';
            
            if (select.value === 'multiple_choice') {
                optionsContainer.innerHTML = `
                    <div class="form-group">
                        <label>Options:</label>
                        <div class="options-list">
                            <div class="option-item">
                                <input type="text" name="option1" required>
                                <input type="radio" name="correct" value="1" required>
                                <label>Correct</label>
                            </div>
                            <div class="option-item">
                                <input type="text" name="option2" required>
                                <input type="radio" name="correct" value="2">
                                <label>Correct</label>
                            </div>
                        </div>
                        <button type="button" onclick="addOption(this)" class="btn-add-option">Add Option</button>
                    </div>
                `;
            } else if (select.value === 'true_false') {
                optionsContainer.innerHTML = `
                    <div class="form-group">
                        <label>Correct Answer:</label>
                        <select name="correct" required>
                            <option value="true">True</option>
                            <option value="false">False</option>
                        </select>
                    </div>
                `;
            }
        }
        
        function addOption(button) {
            const optionsList = button.previousElementSibling;
            const optionNumber = optionsList.children.length + 1;
            
            const optionDiv = document.createElement('div');
            optionDiv.className = 'option-item';
            optionDiv.innerHTML = `
                <input type="text" name="option${optionNumber}" required>
                <input type="radio" name="correct" value="${optionNumber}">
                <label>Correct</label>
                <button type="button" onclick="removeOption(this)" class="btn-remove-option">Remove</button>
            `;
            
            optionsList.appendChild(optionDiv);
        }
        
        function removeOption(button) {
            button.parentElement.remove();
        }
        
        function saveAsDraft() {
            const form = document.getElementById('examForm');
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'status';
            input.value = 'draft';
            form.appendChild(input);
            form.submit();
        }
    </script>
</body>
</html>
