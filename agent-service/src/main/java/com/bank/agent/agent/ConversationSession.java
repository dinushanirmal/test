package com.bank.agent.agent;

import com.bank.agent.tools.input.ReviewTransferInput;

public class ConversationSession {

    private final String sessionId;
    private boolean pendingConfirmation = false;
    private ReviewTransferInput pendingTransferDetails;

    public ConversationSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isPendingConfirmation() {
        return pendingConfirmation;
    }

    public void setPendingConfirmation(boolean pendingConfirmation) {
        this.pendingConfirmation = pendingConfirmation;
    }

    public ReviewTransferInput getPendingTransferDetails() {
        return pendingTransferDetails;
    }

    public void setPendingTransferDetails(ReviewTransferInput details) {
        this.pendingTransferDetails = details;
    }

    public void clearPendingState() {
        this.pendingConfirmation = false;
        this.pendingTransferDetails = null;
    }
}
