package com.litebank.webserver.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequestDto {
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String currencyCode;

    public TransferRequestDto() {
    }

    public TransferRequestDto(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(UUID fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(UUID toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
