package com.bank.agent.agent;

import com.bank.agent.api.AgentResponse;
import com.bank.agent.prompt.SystemPromptBuilder;
import com.bank.agent.session.SessionStore;
import com.bank.agent.tools.BankingToolClient;
import com.bank.agent.tools.ToolResult;
import com.bank.agent.tools.input.GetBalanceInput;
import com.bank.agent.tools.input.ReviewTransferInput;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class AgentLoop {

    private final ChatClient chatClient;
    private final BankingToolClient toolClient;
    private final SystemPromptBuilder promptBuilder;
    private final SessionStore sessionStore;

    public AgentLoop(ChatClient chatClient, BankingToolClient toolClient,
                     SystemPromptBuilder promptBuilder, SessionStore sessionStore) {
        this.chatClient    = chatClient;
        this.toolClient    = toolClient;
        this.promptBuilder = promptBuilder;
        this.sessionStore  = sessionStore;
    }

    /**
     * Entry point for new user messages.
     * Runs the LLM with pre-confirmation tools (getBalance, reviewTransfer).
     * The LLM cannot call executeTransfer — it is not offered.
     */
    public AgentResponse process(String sessionId, String userMessage) {
        ConversationSession session = sessionStore.getOrCreate(sessionId);

        AtomicReference<Object>              capturedSchema          = new AtomicReference<>();
        AtomicReference<ReviewTransferInput> capturedTransferDetails = new AtomicReference<>();

        FunctionCallback getBalanceCb = FunctionCallback.builder()
            .function("getBalance", (GetBalanceInput input) -> {
                ToolResult result = toolClient.getBalance(input.customerId());
                if (result.uiSchema() != null) capturedSchema.set(result.uiSchema());
                return result.data();
            })
            .description("Get account balance for a customer. Default customerId is C001.")
            .inputType(GetBalanceInput.class)
            .build();

        FunctionCallback reviewTransferCb = FunctionCallback.builder()
            .function("reviewTransfer", (ReviewTransferInput input) -> {
                ToolResult result = toolClient.reviewTransfer(input);
                if (result.uiSchema() != null) {
                    capturedSchema.set(result.uiSchema());
                    capturedTransferDetails.set(input);
                }
                return result.data();
            })
            .description("Validate payee and check transfer limits. Returns review details. Call before awaiting confirmation.")
            .inputType(ReviewTransferInput.class)
            .build();

        // Spring AI auto-loops until LLM stops calling tools.
        // Since executeTransfer is not offered, the LLM cannot trigger it.
        chatClient.prompt()
            .system(promptBuilder.build())
            .user(userMessage)
            .tools(getBalanceCb, reviewTransferCb)
            .call()
            .content();

        Object schema = capturedSchema.get();
        if (schema == null) {
            return AgentResponse.error("No UI schema produced. Ensure the appropriate tool was called.");
        }

        if (isTransferReview(schema)) {
            session.setPendingConfirmation(true);
            session.setPendingTransferDetails(capturedTransferDetails.get());
            return AgentResponse.awaitingConfirmation(schema);
        }

        return AgentResponse.complete(schema);
    }

    /**
     * Entry point for user confirmation.
     * Executes the transfer directly — no LLM call needed for a deterministic confirm step.
     */
    public AgentResponse confirm(String sessionId, String action) {
        ConversationSession session = sessionStore.get(sessionId);

        if (!session.isPendingConfirmation()) {
            return AgentResponse.error("No pending confirmation for session: " + sessionId);
        }

        if (!"CONFIRM_TRANSFER".equals(action)) {
            session.clearPendingState();
            return AgentResponse.cancelled();
        }

        ReviewTransferInput details = session.getPendingTransferDetails();
        if (details == null) {
            return AgentResponse.error("Pending transfer details missing from session.");
        }

        ToolResult result = toolClient.executeTransfer(
            details.customerId(), details.recipientName(), details.amount()
        );
        session.clearPendingState();

        if (result.uiSchema() == null) {
            return AgentResponse.error("Transfer executed but retail-bank-server returned no UI schema.");
        }

        return AgentResponse.complete(result.uiSchema());
    }

    private boolean isTransferReview(Object schema) {
        if (schema instanceof JsonNode node) {
            JsonNode type = node.path("type");
            return !type.isMissingNode() && "transfer_review".equals(type.asText());
        }
        return false;
    }
}
