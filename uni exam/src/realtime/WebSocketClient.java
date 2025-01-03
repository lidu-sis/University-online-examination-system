package realtime;
import com.google.gson.Gson;
import model.User;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * WebSocket client for handling real-time communication
 */
@ClientEndpoint
public class WebSocketClient {
    private Session session;
    private final String serverUri;
    private final Gson gson = new Gson();
    private Consumer<String> messageHandler;
    private Consumer<Throwable> errorHandler;
    private final CountDownLatch connectLatch = new CountDownLatch(1);

    /**
     * Constructor
     * @param serverUri WebSocket server URI
     */
    public WebSocketClient(String serverUri) {
        this.serverUri = serverUri;
    }

    /**
     * Connects to the WebSocket server
     * @throws Exception if connection fails
     */
    public void connect() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(serverUri));
        connectLatch.await(); // Wait for connection to be established
    }

    /**
     * Handles WebSocket connection opening
     * @param session WebSocket session
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection established");
        this.session = session;
        connectLatch.countDown();
    }

    /**
     * Handles incoming messages
     * @param message Received message
     */
    @OnMessage
    public void onMessage(String message) {
        if (messageHandler != null) {
            messageHandler.accept(message);
        }
    }

    /**
     * Handles WebSocket errors
     * @param throwable Error that occurred
     */
    @OnError
    public void onError(Throwable throwable) {
        if (errorHandler != null) {
            errorHandler.accept(throwable);
        }
    }

    /**
     * Handles WebSocket connection closure
     */
    @OnClose
    public void onClose() {
        System.out.println("WebSocket connection closed");
        this.session = null;
    }

    /**
     * Sends a chat message
     * @param content Message content
     * @param user Sender user
     * @param targetGroup Target group (optional)
     * @throws IOException if sending fails
     */
    public void sendChatMessage(String content, User user, String targetGroup) throws IOException {
        MessagePayload payload = new MessagePayload(
            "CHAT",
            content,
            user,
            targetGroup,
            null,
            null,
            null
        );
        sendMessage(payload);
    }

    /**
     * Sends a question discussion message
     * @param questionId Question ID
     * @param content Discussion content
     * @param user User participating in discussion
     * @throws IOException if sending fails
     */
    public void sendQuestionDiscussion(String questionId, String content, User user) throws IOException {
        MessagePayload payload = new MessagePayload(
            "QUESTION_DISCUSSION",
            content,
            user,
            null,
            questionId,
            null,
            null
        );
        sendMessage(payload);
    }

    /**
     * Sends an exam monitoring update
     * @param examId Exam ID
     * @param content Monitoring update content
     * @param user Teacher monitoring the exam
     * @throws IOException if sending fails
     */
    public void sendExamMonitoring(String examId, String content, User user) throws IOException {
        MessagePayload payload = new MessagePayload(
            "EXAM_MONITORING",
            content,
            user,
            null,
            null,
            examId,
            null
        );
        sendMessage(payload);
    }

    /**
     * Authenticates the WebSocket connection
     * @param user User to authenticate
     * @param token Authentication token
     * @throws IOException if sending fails
     */
    public void authenticate(User user, String token) throws IOException {
        MessagePayload payload = new MessagePayload(
            "USER_AUTHENTICATION",
            "Authentication request",
            user,
            null,
            null,
            null,
            token
        );
        sendMessage(payload);
    }

    /**
     * Sends a message payload to the server
     * @param payload Message payload to send
     * @throws IOException if sending fails
     */
    private void sendMessage(MessagePayload payload) throws IOException {
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(gson.toJson(payload));
        } else {
            throw new IOException("WebSocket connection is not open");
        }
    }

    /**
     * Sets the message handler
     * @param messageHandler Handler for incoming messages
     */
    public void setMessageHandler(Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Sets the error handler
     * @param errorHandler Handler for WebSocket errors
     */
    public void setErrorHandler(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Closes the WebSocket connection
     * @throws IOException if closing fails
     */
    public void close() throws IOException {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}
