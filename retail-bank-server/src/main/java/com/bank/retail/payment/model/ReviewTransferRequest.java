package com.bank.retail.payment.model;

import java.math.BigDecimal;

public record ReviewTransferRequest(String customerId, String recipientName, BigDecimal amount) {}
