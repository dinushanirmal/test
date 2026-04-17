package com.bank.retail.account.model;

import java.math.BigDecimal;

public record AccountData(
    String customerId,
    String accountName,
    BigDecimal balance,
    String currency
) {}
