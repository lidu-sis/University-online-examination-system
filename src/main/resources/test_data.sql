-- Insert test users
INSERT INTO users (username, password, user_type, full_name, email) VALUES
('admin', 'admin123', 'admin', 'Admin User', 'admin@university.com'),
('teacher1', 'teacher123', 'teacher', 'John Smith', 'john.smith@university.com'),
('student1', 'student123', 'student', 'Jane Doe', 'jane.doe@university.com');

-- Insert test course
INSERT INTO courses (course_code, course_name, teacher_id) VALUES
('CS101', 'Introduction to Computer Science', 2);

-- Enroll student in course
INSERT INTO enrollments (student_id, course_id) VALUES
(3, 1);
