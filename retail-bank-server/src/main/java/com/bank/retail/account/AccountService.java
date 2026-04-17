package com.bank.retail.account;

import com.bank.retail.account.model.AccountData;
import com.bank.retail.mock.MockDataStore;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    public AccountData getBalance(String customerId) {
        AccountData account = MockDataStore.ACCOUNTS.get(customerId);
        if (account == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        return account;
    }
}
