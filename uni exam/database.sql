-- Create the database
CREATE DATABASE IF NOT EXISTS exam_system;
USE exam_system;

-- Users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Questions table
CREATE TABLE questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    question_text TEXT NOT NULL,
    option1 TEXT NOT NULL,
    option2 TEXT NOT NULL,
    option3 TEXT NOT NULL,
    option4 TEXT NOT NULL,
    correct_option INT NOT NULL,
    marks INT NOT NULL,
    subject VARCHAR(50) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    created_by INT,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Exams table
CREATE TABLE exams (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    subject VARCHAR(50) NOT NULL,
    start_time DATETIME NOT NULL,
    duration INT NOT NULL,
    total_marks INT NOT NULL,
    is_active BOOLEAN DEFAULT false,
    created_by INT,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Exam Questions table
CREATE TABLE exam_questions (
    exam_id INT,
    question_id INT,
    question_number INT NOT NULL,
    PRIMARY KEY (exam_id, question_id),
    FOREIGN KEY (exam_id) REFERENCES exams(id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- Student Exam Answers table
CREATE TABLE student_exam_answers (
    student_id INT,
    exam_id INT,
    question_id INT,
    selected_option INT,
    marks_obtained INT,
    PRIMARY KEY (student_id, exam_id, question_id),
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (exam_id) REFERENCES exams(id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- Exam Results table
CREATE TABLE exam_results (
    student_id INT,
    exam_id INT,
    total_marks_obtained INT,
    percentage DECIMAL(5,2),
    submission_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id, exam_id),
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (exam_id) REFERENCES exams(id)
);

-- Insert sample admin user
INSERT INTO users (username, password, role, full_name, email) 
VALUES ('admin', 'admin123', 'ADMIN', 'System Administrator', 'admin@university.com');
