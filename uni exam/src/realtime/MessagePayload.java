package realtime;

import model.User;

/**
 * Represents a message payload for WebSocket communication
 * Contains all necessary information for different types of real-time messages
 */
public class MessagePayload {
    private String type;           // Type of message (CHAT, QUESTION_DISCUSSION, etc.)
    private String content;        // Message content
    private User user;            // User who sent the message
    private String targetGroup;    // Target group for the message
    private String questionId;     // ID of the question being discussed
    private String examId;         // ID of the exam being monitored
    private String token;          // Authentication token
    private long timestamp;        // Message timestamp

    /**
     * Constructor for simple messages
     * @param type Message type
     * @param content Message content
     */
    public MessagePayload(String type, String content) {
        this.type = type;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Full constructor with all fields
     * @param type Message type
     * @param content Message content
     * @param user Sender user
     * @param targetGroup Target group
     * @param questionId Question ID
     * @param examId Exam ID
     * @param token Authentication token
     */
    public MessagePayload(String type, String content, User user, String targetGroup, 
                         String questionId, String examId, String token) {
        this.type = type;
        this.content = content;
        this.user = user;
        this.targetGroup = targetGroup;
        this.questionId = questionId;
        this.examId = examId;
        this.token = token;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTargetGroup() { return targetGroup; }
    public void setTargetGroup(String targetGroup) { this.targetGroup = targetGroup; }

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
