package com.litebank.webserver.infrastructure.data;

import com.litebank.webserver.application.interfaces.repositories.AccountProjectionRepository;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountProjectionRepository implements AccountProjectionRepository {
    private final Map<UUID, AccountProjection> accountsProjections;

    public InMemoryAccountProjectionRepository() {
        this.accountsProjections = new ConcurrentHashMap<>();
    }

    @Override
    public AccountProjection getByIdOrDefault(UUID accountId) {
        return accountsProjections.get(accountId);
    }

    @Override
    public void save(AccountProjection account) {
        synchronized (accountsProjections){
            accountsProjections.put(account.getAccountId(), account);
        }
    }
}
