package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.accounts.Account;

import java.util.concurrent.CompletableFuture;

public interface AccountRepository extends ReadOnlyAccountRepository {
    CompletableFuture<Void> save(Account account);
}
