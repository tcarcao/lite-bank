package com.litebank.webserver.domain.model.accounts.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionProjection {
    private final BigDecimal amount;
    private final TransactionType type;
    private final LocalDateTime timestamp;

    public TransactionProjection(BigDecimal amount, TransactionType type, LocalDateTime timestamp) {
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
