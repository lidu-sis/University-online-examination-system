# University Online Examination System API Documentation

## Overview
This document provides comprehensive documentation for the University Online Examination System API. The API supports user authentication, exam management, question bank operations, and real-time collaboration features.

## Authentication

### Login
```http
POST /api/auth/login
```
Authenticates a user and returns a JWT token.

**Request Body:**
```json
{
    "username": "string",
    "password": "string"
}
```

**Response:**
```json
{
    "token": "string",
    "user": {
        "id": "integer",
        "username": "string",
        "role": "string",
        "fullName": "string",
        "email": "string"
    }
}
```

### Two-Factor Authentication Setup
```http
POST /api/auth/2fa/setup
```
Generates 2FA secret and QR code for user setup.

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
    "secret": "string",
    "qrCodeUrl": "string"
}
```

### Verify 2FA Code
```http
POST /api/auth/2fa/verify
```
Verifies a 2FA code.

**Request Body:**
```json
{
    "code": "string"
}
```

## Exam Management

### Create Exam
```http
POST /api/exams
```
Creates a new exam.

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
    "title": "string",
    "subject": "string",
    "startTime": "datetime",
    "duration": "integer",
    "questions": [
        {
            "id": "integer",
            "marks": "integer"
        }
    ]
}
```

### Get Available Exams
```http
GET /api/exams/available
```
Returns list of available exams for the student.

### Submit Exam
```http
POST /api/exams/{examId}/submit
```
Submits exam answers.

**Request Body:**
```json
{
    "answers": [
        {
            "questionId": "integer",
            "selectedOption": "integer"
        }
    ]
}
```

## Question Bank

### Add Question
```http
POST /api/questions
```
Adds a new question to the question bank.

**Request Body:**
```json
{
    "questionText": "string",
    "options": ["string"],
    "correctOption": "integer",
    "marks": "integer",
    "subject": "string",
    "difficulty": "string"
}
```

### Search Questions
```http
GET /api/questions/search?query={query}
```
Searches questions based on query.

## Real-time Collaboration

### WebSocket Connection
```
WSS://host/websocket
```
Establishes WebSocket connection for real-time features.

### Message Types
1. Chat Message
```json
{
    "type": "CHAT",
    "content": "string",
    "targetGroup": "string"
}
```

2. Question Discussion
```json
{
    "type": "QUESTION_DISCUSSION",
    "questionId": "string",
    "content": "string"
}
```

3. Exam Monitoring
```json
{
    "type": "EXAM_MONITORING",
    "examId": "string",
    "content": "string"
}
```

## Analytics

### Student Progress
```http
GET /api/analytics/student/{studentId}/progress
```
Returns detailed progress analysis for a student.

### Exam Analytics
```http
GET /api/analytics/exam/{examId}
```
Returns comprehensive exam analytics.

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
    "error": "string",
    "message": "string"
}
```

### 401 Unauthorized
```json
{
    "error": "Unauthorized",
    "message": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
    "error": "Forbidden",
    "message": "Insufficient permissions"
}
```

### 500 Internal Server Error
```json
{
    "error": "Internal Server Error",
    "message": "An unexpected error occurred"
}
```

## Rate Limiting

The API implements rate limiting with the following rules:
- Authentication endpoints: 5 requests per minute
- Other endpoints: 60 requests per minute per user

## Security Considerations

1. All endpoints require HTTPS
2. JWT tokens expire after 1 hour
3. 2FA is required for sensitive operations
4. Passwords must meet minimum complexity requirements
5. All requests are logged for security auditing

## WebSocket Events

### Connection Events
- `onConnect`: Fired when connection is established
- `onDisconnect`: Fired when connection is closed
- `onError`: Fired when an error occurs

### Message Events
- `onMessage`: Handles incoming messages
- `onBroadcast`: Handles broadcast messages
- `onPrivate`: Handles private messages

## Best Practices

1. Always validate JWT tokens
2. Implement proper error handling
3. Use appropriate HTTP methods
4. Follow rate limiting guidelines
5. Implement retry logic for failed requests
6. Cache responses where appropriate
7. Use compression for large payloads

## Support

For API support, contact:
- Email: support@university.com
- Documentation: https://docs.university.com
- Status: https://status.university.com
