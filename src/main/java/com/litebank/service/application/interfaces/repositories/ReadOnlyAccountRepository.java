package com.litebank.service.application.interfaces.repositories;

import com.litebank.service.domain.model.accounts.Account;
import com.litebank.service.domain.model.exceptions.AccountNotFoundException;

import java.util.UUID;

public interface ReadOnlyAccountRepository {
    Account getById(UUID id) throws AccountNotFoundException;
}
