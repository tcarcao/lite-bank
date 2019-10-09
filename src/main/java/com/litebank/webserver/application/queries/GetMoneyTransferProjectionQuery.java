package com.litebank.webserver.application.queries;

import com.litebank.webserver.application.interfaces.cqrs.Query;

import java.util.UUID;

public class GetMoneyTransferProjectionQuery implements Query {
    private final UUID moneyTransferId;

    public GetMoneyTransferProjectionQuery(UUID moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public UUID getMoneyTransferId() {
        return moneyTransferId;
    }
}
