package com.litebank.webserver.domain.model.moneytransfers.projections;

import java.math.BigDecimal;
import java.util.UUID;

public class MoneyTransferProjection {
    private final UUID moneyTransferId;
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final String currencyCode;
    private MoneyTransferStateProjection state;
    private MoneyTransferErrorProjection error;

    public MoneyTransferProjection(UUID moneyTransferId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode, MoneyTransferStateProjection state) {
        this.moneyTransferId = moneyTransferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.state = state;
    }

    public MoneyTransferProjection(UUID moneyTransferId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, MoneyTransferStateProjection state, String currencyCode, MoneyTransferErrorProjection error) {
        this(moneyTransferId, fromAccountId, toAccountId, amount, currencyCode, state);
        this.error = error;
    }

    public void fail(String reason) {
        state = MoneyTransferStateProjection.FAILED;
        error = new MoneyTransferErrorProjection(reason, reason);
    }

    public void finish() {
        state = MoneyTransferStateProjection.FINISHED;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
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

    public MoneyTransferStateProjection getState() {
        return state;
    }

    public MoneyTransferErrorProjection getError() {
        return error;
    }
}
