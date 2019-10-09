package com.litebank.webserver.application.commands.accounts.projections;

import com.litebank.webserver.application.interfaces.cqrs.Command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreditAccountProjectionCommand implements Command {
    private final UUID accountId;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;

    public CreditAccountProjectionCommand(UUID accountId, BigDecimal amount, LocalDateTime timestamp) {
        this.accountId = accountId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
