package com.litebank.service.application.commands.moneytransfers;

import com.litebank.service.application.interfaces.cqrs.Command;

import java.util.UUID;

public class CompleteMoneyTransferCommand implements Command {
    private final UUID moneyTransferId;

    public CompleteMoneyTransferCommand(UUID moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
