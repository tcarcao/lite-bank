package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.accounts.Account;

public interface AccountRepository extends ReadOnlyAccountRepository {
    void save(Account account);
}
