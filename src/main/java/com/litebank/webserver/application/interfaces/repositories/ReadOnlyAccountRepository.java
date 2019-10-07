package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.accounts.Account;
import com.litebank.webserver.domain.model.exceptions.AccountNotFoundException;

import java.util.UUID;

public interface ReadOnlyAccountRepository {
    Account getById(UUID id) throws AccountNotFoundException;
}
