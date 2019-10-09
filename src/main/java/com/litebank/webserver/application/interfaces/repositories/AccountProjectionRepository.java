package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;

public interface AccountProjectionRepository extends ReadOnlyAccountProjectionRepository {
    void save(AccountProjection account);
}
