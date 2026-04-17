package com.bank.agent.tools;

import com.bank.agent.tools.input.ReviewTransferInput;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class BankingToolClient {

    private final RestClient restClient;

    public BankingToolClient(@Value("${banking.tools.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public ToolResult getBalance(String customerId) {
        JsonNode response = restClient.get()
            .uri("/tools/account/balance?customerId={id}", customerId)
            .retrieve()
            .body(JsonNode.class);
        return toToolResult(response);
    }

    public ToolResult reviewTransfer(ReviewTransferInput input) {
        JsonNode response = restClient.post()
            .uri("/tools/payment/review")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of(
                "customerId",    input.customerId(),
                "recipientName", input.recipientName(),
                "amount",        input.amount()
            ))
            .retrieve()
            .body(JsonNode.class);
        return toToolResult(response);
    }

    public ToolResult executeTransfer(String customerId, String recipientName, BigDecimal amount) {
        JsonNode response = restClient.post()
            .uri("/tools/payment/transfer")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of(
                "customerId",    customerId,
                "recipientName", recipientName,
                "amount",        amount
            ))
            .retrieve()
            .body(JsonNode.class);
        return toToolResult(response);
    }

    private ToolResult toToolResult(JsonNode node) {
        JsonNode data     = node.path("data");
        JsonNode uiSchema = node.path("uiSchema");
        return new ToolResult(
            data.isMissingNode()     ? null : data,
            uiSchema.isMissingNode() || uiSchema.isNull() ? null : uiSchema
        );
    }
}
