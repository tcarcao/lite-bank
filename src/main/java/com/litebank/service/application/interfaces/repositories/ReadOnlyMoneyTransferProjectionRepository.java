package com.litebank.service.application.interfaces.repositories;

import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferProjection;

import java.util.UUID;

public interface ReadOnlyMoneyTransferProjectionRepository {
    MoneyTransferProjection getByIdOrDefault(UUID moneyTransferId);
}
