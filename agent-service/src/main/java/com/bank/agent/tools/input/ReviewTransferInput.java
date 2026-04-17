package com.bank.agent.tools.input;

import java.math.BigDecimal;

public record ReviewTransferInput(String customerId, String recipientName, BigDecimal amount) {}
