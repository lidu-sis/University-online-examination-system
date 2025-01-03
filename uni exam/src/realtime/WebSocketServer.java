package realtime;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import model.User;
import util.SecurityUtil;

/**
 * WebSocket server for handling real-time communication between users
 * Supports features like live chat, collaborative question discussions,
 * and real-time exam monitoring
 */
@ServerEndpoint(value = "/websocket")
public class WebSocketServer {
    // Static map to store all active sessions
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    // Map to store user information for each session
    private static final Map<String, User> sessionUsers = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();

    /**
     * Handles new WebSocket connections
     * @param session The WebSocket session that is being opened
     */
    @OnOpen
    public void onOpen(Session session) {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        System.out.println("New WebSocket connection: " + sessionId);
    }

    /**
     * Handles incoming WebSocket messages
     * @param session The WebSocket session
     * @param message The message received
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            // Parse the message
            MessagePayload payload = gson.fromJson(message, MessagePayload.class);
            
            // Handle different types of messages
            switch (payload.getType()) {
                case "CHAT":
                    broadcastChat(payload);
                    break;
                case "QUESTION_DISCUSSION":
                    handleQuestionDiscussion(payload);
                    break;
                case "EXAM_MONITORING":
                    handleExamMonitoring(payload);
                    break;
                case "USER_AUTHENTICATION":
                    handleUserAuthentication(session, payload);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(session, "Error processing message: " + e.getMessage());
        }
    }

    /**
     * Handles WebSocket connection closures
     * @param session The WebSocket session that is being closed
     */
    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        sessionUsers.remove(sessionId);
        System.out.println("WebSocket connection closed: " + sessionId);
    }

    /**
     * Handles WebSocket errors
     * @param session The WebSocket session where the error occurred
     * @param throwable The error that occurred
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error for session " + session.getId());
        throwable.printStackTrace();
    }

    /**
     * Broadcasts a chat message to all connected users or specific group
     * @param payload The message payload to broadcast
     */
    private void broadcastChat(MessagePayload payload) {
        String message = gson.toJson(payload);
        if (payload.getTargetGroup() != null) {
            // Send to specific group
            sessions.values().stream()
                .filter(session -> isUserInGroup(session, payload.getTargetGroup()))
                .forEach(session -> sendMessage(session, message));
        } else {
            // Broadcast to all
            sessions.values().forEach(session -> sendMessage(session, message));
        }
    }

    /**
     * Handles question discussion messages
     * @param payload The message payload containing question discussion data
     */
    private void handleQuestionDiscussion(MessagePayload payload) {
        // Create a discussion room if it doesn't exist
        String questionId = payload.getQuestionId();
        String message = gson.toJson(payload);

        // Send to all users discussing this question
        sessions.values().stream()
            .filter(session -> isDiscussingQuestion(session, questionId))
            .forEach(session -> sendMessage(session, message));
    }

    /**
     * Handles exam monitoring messages
     * @param payload The message payload containing exam monitoring data
     */
    private void handleExamMonitoring(MessagePayload payload) {
        String examId = payload.getExamId();
        String message = gson.toJson(payload);

        // Send to teachers monitoring this exam
        sessions.values().stream()
            .filter(session -> isMonitoringExam(session, examId))
            .forEach(session -> sendMessage(session, message));
    }

    /**
     * Handles user authentication for WebSocket connections
     * @param session The WebSocket session to authenticate
     * @param payload The authentication payload
     */
    private void handleUserAuthentication(Session session, MessagePayload payload) {
        User user = payload.getUser();
        if (user != null && SecurityUtil.validateToken(payload.getToken())) {
            sessionUsers.put(session.getId(), user);
            sendMessage(session, gson.toJson(new MessagePayload("AUTH_SUCCESS", "Authentication successful")));
        } else {
            sendError(session, "Authentication failed");
        }
    }

    /**
     * Sends a message to a specific WebSocket session
     * @param session The target session
     * @param message The message to send
     */
    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an error message to a specific session
     * @param session The target session
     * @param error The error message
     */
    private void sendError(Session session, String error) {
        MessagePayload errorPayload = new MessagePayload("ERROR", error);
        sendMessage(session, gson.toJson(errorPayload));
    }

    /**
     * Checks if a user is in a specific group
     * @param session The user's session
     * @param group The group to check
     * @return true if the user is in the group
     */
    private boolean isUserInGroup(Session session, String group) {
        User user = sessionUsers.get(session.getId());
        return user != null && user.getRole().equals(group);
    }

    /**
     * Checks if a user is discussing a specific question
     * @param session The user's session
     * @param questionId The question ID
     * @return true if the user is discussing the question
     */
    private boolean isDiscussingQuestion(Session session, String questionId) {
        // Implementation depends on how you track question discussions
        return true; // Simplified for example
    }

    /**
     * Checks if a user is monitoring a specific exam
     * @param session The user's session
     * @param examId The exam ID
     * @return true if the user is monitoring the exam
     */
    private boolean isMonitoringExam(Session session, String examId) {
        User user = sessionUsers.get(session.getId());
        return user != null && user.getRole().equals("TEACHER");
    }
}
