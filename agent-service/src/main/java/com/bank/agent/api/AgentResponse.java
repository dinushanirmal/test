package com.bank.agent.api;

public record AgentResponse(
    Object uiSchema,
    boolean awaitingConfirmation,
    boolean cancelled,
    boolean error,
    String errorMessage
) {
    public static AgentResponse complete(Object schema) {
        return new AgentResponse(schema, false, false, false, null);
    }

    public static AgentResponse awaitingConfirmation(Object schema) {
        return new AgentResponse(schema, true, false, false, null);
    }

    public static AgentResponse cancelled() {
        return new AgentResponse(null, false, true, false, null);
    }

    public static AgentResponse error(String message) {
        return new AgentResponse(null, false, false, true, message);
    }
}
