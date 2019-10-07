package com.litebank.webserver.domain.model.accounts.events;

import com.litebank.webserver.domain.model.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountCreditedEvent extends Event {
    private final BigDecimal amount;
    private UUID moneyTransferId;

    public AccountCreditedEvent(UUID aggregateId, LocalDateTime timestamp, int version, BigDecimal amount) {
        super(aggregateId, timestamp, version);
        this.amount = amount;
    }

    public AccountCreditedEvent(UUID aggregateId, LocalDateTime timestamp, int version, BigDecimal amount, UUID moneyTransferId) {
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
