package com.litebank.webserver.domain.model.accounts;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private final UUID id;
    private UUID customerId;
    private BigDecimal balance;

    public Account(UUID id) {
        this.id = id;
    }

    public Account(UUID id, UUID customerId, BigDecimal balance) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
