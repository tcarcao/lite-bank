package com.litebank.webserver.application.queries;

import com.litebank.webserver.application.interfaces.cqrs.Query;

import java.util.UUID;

public class GetAccountProjectionQuery implements Query {
    private final UUID accountId;

    public GetAccountProjectionQuery(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getAccountId() {
        return accountId;
    }
}
