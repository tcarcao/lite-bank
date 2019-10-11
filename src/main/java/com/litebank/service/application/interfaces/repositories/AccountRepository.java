package com.litebank.service.application.interfaces.repositories;

import com.litebank.service.domain.model.accounts.Account;

import java.util.concurrent.CompletableFuture;

public interface AccountRepository extends ReadOnlyAccountRepository {
    CompletableFuture<Void> save(Account account);
}
