package com.litebank.webserver.application.commands.moneytransfers;

import com.litebank.webserver.application.interfaces.cqrs.Command;

import java.util.UUID;

public class FailMoneyTransferDueToInsufficientFundsCommand implements Command {
    private final UUID moneyTransferId;

    public FailMoneyTransferDueToInsufficientFundsCommand(UUID moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
