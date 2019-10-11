package com.litebank.service.application.queries;

import com.litebank.service.application.interfaces.cqrs.Query;

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
