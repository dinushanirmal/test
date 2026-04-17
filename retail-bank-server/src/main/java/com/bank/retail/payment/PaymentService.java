package com.bank.retail.payment;

import com.bank.retail.mock.MockDataStore;
import com.bank.retail.payment.model.PayeeInfo;
import com.bank.retail.payment.model.TransferResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    public PayeeInfo validatePayee(String recipientName) {
        // Match by first name (lowercased) to handle LLM-provided full names gracefully
        String key = recipientName.toLowerCase().split("\\s+")[0];
        PayeeInfo payee = MockDataStore.PAYEES.get(key);
        if (payee == null) {
            throw new IllegalArgumentException("Payee not found: " + recipientName
                + ". Known payees: Amal, Kumari, Nimal.");
        }
        return payee;
    }

    public void checkLimit(BigDecimal amount) {
        if (amount.compareTo(MockDataStore.MIN_TRANSFER) < 0) {
            throw new IllegalArgumentException(
                "Amount " + amount + " is below the minimum transfer of " + MockDataStore.MIN_TRANSFER);
        }
        if (amount.compareTo(MockDataStore.TRANSFER_LIMIT) > 0) {
            throw new IllegalArgumentException(
                "Amount " + amount + " exceeds the transfer limit of " + MockDataStore.TRANSFER_LIMIT);
        }
    }

    public TransferResult execute(String customerId, String recipientName, BigDecimal amount) {
        return new TransferResult(
            "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
            recipientName,
            amount,
            "LKR",
            LocalDateTime.now()
        );
    }
}
