package com.bank.retail.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResult(
    String referenceId,
    String recipientName,
    BigDecimal amount,
    String currency,
    LocalDateTime timestamp
) {}
