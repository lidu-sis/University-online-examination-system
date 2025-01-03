package view;

import realtime.WebSocketClient;
import realtime.MessagePayload;
import model.User;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Panel for real-time chat functionality
 */
public class ChatPanel extends JPanel {
    private final WebSocketClient webSocketClient;
    private final User currentUser;
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JComboBox<String> targetGroupCombo;
    private final Gson gson = new Gson();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * Constructor
     * @param webSocketClient WebSocket client for communication
     * @param currentUser Current logged-in user
     */
    public ChatPanel(WebSocketClient webSocketClient, User currentUser) {
        this.webSocketClient = webSocketClient;
        this.currentUser = currentUser;
        
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(300, 400));

        // Message input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        
        // Target group selection
        targetGroupCombo = new JComboBox<>(new String[]{"All", "Teachers", "Students"});
        
        // Add components to input panel
        JPanel topInputPanel = new JPanel(new BorderLayout(5, 0));
        topInputPanel.add(new JLabel("To: "), BorderLayout.WEST);
        topInputPanel.add(targetGroupCombo, BorderLayout.CENTER);
        
        inputPanel.add(topInputPanel, BorderLayout.NORTH);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add main components
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Setup message field key listener
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        // Setup WebSocket message handler
        setupMessageHandler();
    }

    /**
     * Sets up the WebSocket message handler
     */
    private void setupMessageHandler() {
        webSocketClient.setMessageHandler(message -> {
            MessagePayload payload = gson.fromJson(message, MessagePayload.class);
            if ("CHAT".equals(payload.getType())) {
                SwingUtilities.invokeLater(() -> {
                    String timestamp = timeFormat.format(new Date(payload.getTimestamp()));
                    String sender = payload.getUser().getUsername();
                    String content = payload.getContent();
                    String targetGroup = payload.getTargetGroup();
                    
                    StringBuilder messageText = new StringBuilder();
                    messageText.append("[").append(timestamp).append("] ");
                    messageText.append(sender).append(": ");
                    if (targetGroup != null && !targetGroup.equals("All")) {
                        messageText.append("(to ").append(targetGroup).append(") ");
                    }
                    messageText.append(content).append("\n");
                    
                    chatArea.append(messageText.toString());
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                });
            }
        });

        webSocketClient.setErrorHandler(throwable -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                    "Error in chat: " + throwable.getMessage(),
                    "Chat Error",
                    JOptionPane.ERROR_MESSAGE);
            });
        });
    }

    /**
     * Sends a chat message
     */
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                String targetGroup = targetGroupCombo.getSelectedItem().toString();
                targetGroup = "All".equals(targetGroup) ? null : targetGroup;
                
                webSocketClient.sendChatMessage(message, currentUser, targetGroup);
                messageField.setText("");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error sending message: " + e.getMessage(),
                    "Send Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Adds a system message to the chat
     * @param message System message to add
     */
    public void addSystemMessage(String message) {
        String timestamp = timeFormat.format(new Date());
        chatArea.append("[" + timestamp + "] SYSTEM: " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    /**
     * Clears the chat area
     */
    public void clearChat() {
        chatArea.setText("");
    }

    /**
     * Enables or disables the chat panel
     * @param enabled true to enable, false to disable
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        messageField.setEnabled(enabled);
        targetGroupCombo.setEnabled(enabled);
        if (!enabled) {
            addSystemMessage("Chat disabled");
        } else {
            addSystemMessage("Chat enabled");
        }
    }
}
