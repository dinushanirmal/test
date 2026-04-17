package com.bank.retail.mock;

import com.bank.retail.account.model.AccountData;
import com.bank.retail.payment.model.PayeeInfo;

import java.math.BigDecimal;
import java.util.Map;

public final class MockDataStore {

    public static final Map<String, AccountData> ACCOUNTS = Map.of(
        "C001", new AccountData("C001", "Savings",  new BigDecimal("250000.00"), "LKR"),
        "C002", new AccountData("C002", "Current",  new BigDecimal("85000.00"),  "LKR")
    );

    // Keyed by first name in lowercase for fuzzy LLM-provided name matching
    public static final Map<String, PayeeInfo> PAYEES = Map.of(
        "amal",   new PayeeInfo("P001", "Amal Perera",       "7710234567"),
        "kumari", new PayeeInfo("P002", "Kumari Silva",      "7724567890"),
        "nimal",  new PayeeInfo("P003", "Nimal Karunaratne", "7689012345")
    );

    public static final BigDecimal TRANSFER_LIMIT = new BigDecimal("500000.00");
    public static final BigDecimal MIN_TRANSFER   = new BigDecimal("100.00");

    private MockDataStore() {}
}
