package com.litebank.service.application.interfaces.repositories;

import com.litebank.service.domain.model.accounts.projections.AccountProjection;

public interface AccountProjectionRepository extends ReadOnlyAccountProjectionRepository {
    void save(AccountProjection account);
}
