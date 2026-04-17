package com.bank.retail.payment;

import com.bank.retail.common.ToolResponse;
import com.bank.retail.common.UiAction;
import com.bank.retail.common.UiSchema;
import com.bank.retail.payment.model.PayeeInfo;
import com.bank.retail.payment.model.ReviewTransferRequest;
import com.bank.retail.payment.model.TransferRequest;
import com.bank.retail.payment.model.TransferResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tools/payment")
public class PaymentToolController {

    private final PaymentService paymentService;

    public PaymentToolController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Validates payee + checks limits, returns transfer_review UI schema.
     * Agent service calls this; Angular renders the review card and awaits confirmation.
     */
    @PostMapping("/review")
    public ToolResponse<Map<String, Object>> reviewTransfer(@RequestBody ReviewTransferRequest request) {
        PayeeInfo payee = paymentService.validatePayee(request.recipientName());
        paymentService.checkLimit(request.amount());

        Map<String, Object> data = Map.of(
            "payeeId",       payee.payeeId(),
            "recipientName", payee.name(),
            "amount",        request.amount(),
            "currency",      "LKR",
            "customerId",    request.customerId()
        );

        UiSchema uiSchema = new UiSchema(
            "transfer_review",
            Map.of(
                "to",          payee.name(),
                "amount",      request.amount(),
                "fee",         0,
                "fromAccount", "Savings",
                "currency",    "LKR"
            ),
            List.of(
                new UiAction("Confirm", "CONFIRM_TRANSFER"),
                new UiAction("Cancel",  "CANCEL")
            )
        );

        return new ToolResponse<>(data, uiSchema);
    }

    /**
     * Executes the confirmed transfer. Only called by agent-service after explicit user confirmation.
     */
    @PostMapping("/transfer")
    public ToolResponse<TransferResult> executeTransfer(@RequestBody TransferRequest request) {
        TransferResult result = paymentService.execute(
            request.customerId(), request.recipientName(), request.amount()
        );

        UiSchema uiSchema = new UiSchema(
            "transfer_success",
            Map.of(
                "referenceId", result.referenceId(),
                "to",          result.recipientName(),
                "amount",      result.amount(),
                "currency",    result.currency(),
                "timestamp",   result.timestamp().toString()
            ),
            null
        );

        return new ToolResponse<>(result, uiSchema);
    }
}
