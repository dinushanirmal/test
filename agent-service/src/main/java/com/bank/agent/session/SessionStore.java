package com.bank.agent.session;

import com.bank.agent.agent.ConversationSession;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore {

    private final ConcurrentHashMap<String, ConversationSession> sessions = new ConcurrentHashMap<>();

    public ConversationSession getOrCreate(String sessionId) {
        return sessions.computeIfAbsent(sessionId, ConversationSession::new);
    }

    public ConversationSession get(String sessionId) {
        ConversationSession session = sessions.get(sessionId);
        if (session == null) {
            throw new IllegalStateException("Session not found: " + sessionId);
        }
        return session;
    }
}
