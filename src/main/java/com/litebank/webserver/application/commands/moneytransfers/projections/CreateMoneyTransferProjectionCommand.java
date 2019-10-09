package com.litebank.webserver.application.commands.moneytransfers.projections;

import com.litebank.webserver.application.interfaces.cqrs.Command;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateMoneyTransferProjectionCommand implements Command {

    private final UUID moneyTransferId;
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final String currencyCode;

    public CreateMoneyTransferProjectionCommand(UUID moneyTransferId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode) {
        this.moneyTransferId = moneyTransferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currencyCode = currencyCode;
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
}
