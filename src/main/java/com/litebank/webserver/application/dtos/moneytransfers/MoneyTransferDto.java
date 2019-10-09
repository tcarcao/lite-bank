package com.litebank.webserver.application.dtos.moneytransfers;

import java.math.BigDecimal;
import java.util.UUID;

public class MoneyTransferDto {
    private UUID moneyTransferId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String currencyCode;
    private MoneyTransferStateDto state;
    private MoneyTransferErrorDto error;

    public MoneyTransferDto() {
    }

    public MoneyTransferDto(UUID moneyTransferId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode, MoneyTransferStateDto state) {
        this.moneyTransferId = moneyTransferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.state = state;
    }

    public MoneyTransferDto(UUID moneyTransferId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode, MoneyTransferStateDto state, MoneyTransferErrorDto error) {
        this(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode, state);
        this.error = error;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }

    public void setMoneyTransferId(UUID moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
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

    public MoneyTransferStateDto getState() {
        return state;
    }

    public void setState(MoneyTransferStateDto state) {
        this.state = state;
    }

    public MoneyTransferErrorDto getError() {
        return error;
    }

    public void setError(MoneyTransferErrorDto error) {
        this.error = error;
    }
}
