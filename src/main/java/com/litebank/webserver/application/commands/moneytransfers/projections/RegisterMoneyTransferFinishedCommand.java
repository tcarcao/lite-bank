package com.litebank.webserver.application.commands.moneytransfers.projections;

import com.litebank.webserver.application.interfaces.cqrs.Command;

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
