package com.litebank.webserver.application.dtos.accounts;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountOpenedDto {
    private UUID accountId;
    private BigDecimal balance;
    private UUID customerId;

    public AccountOpenedDto() {
    }

    public AccountOpenedDto(UUID accountId, BigDecimal balance, UUID customerId) {
        this.accountId = accountId;
        this.balance = balance;
        this.customerId = customerId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}
