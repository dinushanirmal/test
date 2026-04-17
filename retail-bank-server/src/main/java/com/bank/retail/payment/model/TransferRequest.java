package com.bank.retail.payment.model;

import java.math.BigDecimal;

public record TransferRequest(String customerId, String recipientName, BigDecimal amount) {}
