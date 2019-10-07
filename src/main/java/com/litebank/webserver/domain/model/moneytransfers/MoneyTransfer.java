package com.litebank.webserver.domain.model.moneytransfers;

import java.math.BigDecimal;
import java.util.UUID;

public class MoneyTransfer {
    private final UUID id;
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final String currencyCode;
    private final MoneyTransferState state;

    public MoneyTransfer(UUID id, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.state = MoneyTransferState.INITIAL;
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

    public MoneyTransferState getState() {
        return state;
    }

}
