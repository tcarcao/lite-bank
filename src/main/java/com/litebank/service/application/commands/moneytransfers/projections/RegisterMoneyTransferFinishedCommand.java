package com.litebank.service.application.commands.moneytransfers.projections;

import com.litebank.service.application.interfaces.cqrs.Command;

import java.util.UUID;

public class RegisterMoneyTransferFinishedCommand implements Command {
    private final UUID moneyTransferId;

    public RegisterMoneyTransferFinishedCommand(UUID moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
