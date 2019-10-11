package com.litebank.service.domain.model.accounts.events;

import com.litebank.service.domain.model.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountDebitedEvent extends Event {
    private final BigDecimal amount;
    private UUID moneyTransferId;

    public AccountDebitedEvent(UUID aggregateId, LocalDateTime timestamp, int version, BigDecimal amount) {
        super(aggregateId, timestamp, version);
        this.amount = amount;
    }

    public AccountDebitedEvent(UUID aggregateId, LocalDateTime timestamp, int version, BigDecimal amount, UUID moneyTransferId) {
        super(aggregateId, timestamp, version);
        this.amount = amount;
        this.moneyTransferId = moneyTransferId;
    }

    public boolean isFromTransfer() {
        return moneyTransferId != null;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
