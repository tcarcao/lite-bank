package com.litebank.service.infrastructure.data;

import com.litebank.service.application.interfaces.EventBus;
import com.litebank.service.application.interfaces.repositories.AccountRepository;
import com.litebank.service.domain.model.accounts.Account;
import com.litebank.service.domain.model.exceptions.AccountNotFoundException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InMemoryAccountRepository implements AccountRepository {

    private final EventBus eventBus;
    private final String accountsStreamId;

    public InMemoryAccountRepository(EventBus eventBus, String accountsStreamId) {
        this.eventBus = eventBus;
        this.accountsStreamId = accountsStreamId;
    }

    @Override
    public Account getById(UUID id) throws AccountNotFoundException {
        var events = eventBus.loadEntity(accountsStreamId, id);

        if (events.isEmpty()) {
            throw new AccountNotFoundException("Account '" + id + "' not found");
        }

        var account = new Account(id);
        account.apply(events);

        return account;
    }

    @Override
    public CompletableFuture<Void> save(Account account) {
        var events = account.getNewEvents();

        var completableFutures = events.stream().map(event -> eventBus.addEventToStream(accountsStreamId, event)).collect(Collectors.toList());
        var completableFuturesArray = completableFutures.toArray(new CompletableFuture[completableFutures.size()]);

        return CompletableFuture.allOf(completableFuturesArray);
    }
}
