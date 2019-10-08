package com.litebank.webserver.application.commands.accounts;

import com.litebank.webserver.application.interfaces.cqrs.Command;

import java.math.BigDecimal;
import java.util.UUID;

public class CreditAccountFromTransferCommand implements Command {
    private final BigDecimal amount;
    private final UUID moneyTransferId;

    public CreditAccountFromTransferCommand(BigDecimal amount, UUID moneyTransferId) {
        this.amount = amount;
        this.moneyTransferId = moneyTransferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
