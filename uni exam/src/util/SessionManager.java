package util;

import model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;

public class SessionManager {
    private static SessionManager instance;
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30 minutes

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String createSession(User user) {
        String token = SecurityUtil.generateSessionToken();
        sessions.put(token, new SessionInfo(user, Instant.now().toEpochMilli()));
        return token;
    }

    public User getUser(String token) {
        SessionInfo session = sessions.get(token);
        if (session != null && !isSessionExpired(session)) {
            session.lastAccessed = Instant.now().toEpochMilli();
            return session.user;
        }
        return null;
    }

    public void invalidateSession(String token) {
        sessions.remove(token);
    }

    private boolean isSessionExpired(SessionInfo session) {
        long currentTime = Instant.now().toEpochMilli();
        if (currentTime - session.lastAccessed > SESSION_TIMEOUT) {
            sessions.values().removeIf(s -> currentTime - s.lastAccessed > SESSION_TIMEOUT);
            return true;
        }
        return false;
    }

    private static class SessionInfo {
        User user;
        long lastAccessed;

        SessionInfo(User user, long lastAccessed) {
            this.user = user;
            this.lastAccessed = lastAccessed;
        }
    }
}
