package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;

import java.util.UUID;

public interface ReadOnlyAccountProjectionRepository {
    AccountProjection getByIdOrDefault(UUID accountId);
}
