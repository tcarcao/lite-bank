package com.litebank.service.application.interfaces.repositories;

import com.litebank.service.domain.model.accounts.projections.AccountProjection;

import java.util.UUID;

public interface ReadOnlyAccountProjectionRepository {
    AccountProjection getByIdOrDefault(UUID accountId);
}
