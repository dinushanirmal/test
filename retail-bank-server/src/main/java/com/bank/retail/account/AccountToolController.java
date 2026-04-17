package com.bank.retail.account;

import com.bank.retail.account.model.AccountData;
import com.bank.retail.common.ToolResponse;
import com.bank.retail.common.UiSchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tools/account")
public class AccountToolController {

    private final AccountService accountService;

    public AccountToolController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ToolResponse<AccountData> getBalance(@RequestParam String customerId) {
        AccountData data = accountService.getBalance(customerId);

        UiSchema uiSchema = new UiSchema(
            "balance_card",
            Map.of(
                "account",  data.accountName(),
                "balance",  data.balance(),
                "currency", data.currency()
            ),
            null
        );

        return new ToolResponse<>(data, uiSchema);
    }
}
