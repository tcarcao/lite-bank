package com.litebank.webserver.infrastructure.data;

import com.litebank.webserver.application.interfaces.EventBus;
import com.litebank.webserver.application.interfaces.repositories.AccountRepository;
import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.domain.model.exceptions.AccountNotFoundException;

import java.util.UUID;

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
    public void save(Account account) {
        var events = account.getNewEvents();

        events.forEach(event -> eventBus.addEventToStream(accountsStreamId, event));
    }
}
