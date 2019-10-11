package com.litebank.service.application.commands.accounts.projections;

import com.litebank.service.application.interfaces.cqrs.Command;

import java.math.BigDecimal;
import java.util.UUID;

public class OpenAccountProjectionCommand implements Command {
    private final UUID accountId;
    private final BigDecimal balance;

    public OpenAccountProjectionCommand(UUID accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
