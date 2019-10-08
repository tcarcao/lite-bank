package com.litebank.webserver.domain.model.moneytransfers.events;

import com.litebank.webserver.domain.model.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MoneyTransferCreatedEvent extends Event {
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final String currencyCode;

    public MoneyTransferCreatedEvent(UUID aggregateId, LocalDateTime timestamp, int version, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode) {
        super(aggregateId, timestamp, version);

        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
