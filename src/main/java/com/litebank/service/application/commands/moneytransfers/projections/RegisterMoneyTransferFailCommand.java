package com.litebank.service.application.commands.moneytransfers.projections;

import com.litebank.service.application.interfaces.cqrs.Command;

import java.util.UUID;

public class RegisterMoneyTransferFailCommand implements Command {
    private final UUID moneyTransferId;
    private final String reason;

    public RegisterMoneyTransferFailCommand(UUID moneyTransferId, String reason) {
        this.moneyTransferId = moneyTransferId;
        this.reason = reason;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }

    public String getReason() {
        return reason;
    }
}
