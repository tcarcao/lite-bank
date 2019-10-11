package com.litebank.service.application.commands.accounts.projections;

import com.litebank.service.application.interfaces.cqrs.Command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DebitAccountProjectionCommand implements Command {
    private final UUID accountId;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;

    public DebitAccountProjectionCommand(UUID accountId, BigDecimal amount, LocalDateTime timestamp) {
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
