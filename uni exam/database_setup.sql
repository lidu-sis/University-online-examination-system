-- Create the database
CREATE DATABASE IF NOT EXISTS exam_system;
USE exam_system;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Create exams table
CREATE TABLE IF NOT EXISTS exams (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    duration INT NOT NULL,
    created_by INT,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Create questions table
CREATE TABLE IF NOT EXISTS questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT,
    question_text TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    correct_answer TEXT NOT NULL,
    points INT NOT NULL,
    options TEXT,
    FOREIGN KEY (exam_id) REFERENCES exams(id)
);

-- Create exam_results table
CREATE TABLE IF NOT EXISTS exam_results (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exam_id INT,
    user_id INT,
    score DECIMAL(5,2) NOT NULL,
    completion_date DATETIME NOT NULL,
    FOREIGN KEY (exam_id) REFERENCES exams(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert admin user
INSERT INTO users (username, password, role, full_name, email) 
VALUES ('admin', 'admin123', 'admin', 'Administrator', 'admin@example.com')
ON DUPLICATE KEY UPDATE username=username;
