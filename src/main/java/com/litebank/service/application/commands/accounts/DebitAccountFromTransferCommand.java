package com.litebank.service.application.commands.accounts;

import com.litebank.service.application.interfaces.cqrs.Command;

import java.math.BigDecimal;
import java.util.UUID;

public class DebitAccountFromTransferCommand implements Command {
    private final BigDecimal amount;
    private final UUID accountToDebitId;
    private final UUID moneyTransferId;

    public DebitAccountFromTransferCommand(BigDecimal amount, UUID accountToDebitId, UUID moneyTransferId) {
        this.amount = amount;
        this.accountToDebitId = accountToDebitId;
        this.moneyTransferId = moneyTransferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UUID getAccountToDebitId() {
        return accountToDebitId;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
