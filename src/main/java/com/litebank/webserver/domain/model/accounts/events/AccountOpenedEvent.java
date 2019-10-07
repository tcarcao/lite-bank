package com.litebank.webserver.domain.model.accounts.events;

import com.litebank.webserver.domain.model.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountOpenedEvent extends Event {

    private final UUID customerId;
    private final BigDecimal balance;

    public AccountOpenedEvent(UUID aggregateId, LocalDateTime timestamp, int version, UUID customerId, BigDecimal balance) {
        super(aggregateId, timestamp, version);
        this.customerId = customerId;
        this.balance = balance;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
