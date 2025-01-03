# University Online Examination System

A comprehensive online examination platform built using Java technologies including JDBC, JFC (Swing), Servlets, Network Programming, and Multithreading.

## Features
- User Authentication (Admin, Teachers, Students)
- Question Bank Management
- Exam Creation and Scheduling
- Real-time Exam Taking Interface
- Automated Result Evaluation
- Performance Analytics

## Technologies Used
- Java 8+
- JDBC for Database Connectivity
- Swing for Desktop GUI
- Servlets for Web Interface
- MySQL Database
- Multithreading for Concurrent User Handling
- Network Programming for Real-time Communication

## Project Structure
- `src/` - Source code
  - `database/` - Database connection and operations
  - `model/` - Data models
  - `controller/` - Business logic
  - `view/` - GUI components
  - `util/` - Utility classes
  - `network/` - Network communication
  
## Setup Instructions
1. Install Java JDK 8 or higher
2. Install MySQL Server
3. Create database using the provided SQL script
4. Configure database connection in `database.properties`
5. Run the application using the main class

## Database Setup
Run the provided `database.sql` script to create necessary tables and initial data.
