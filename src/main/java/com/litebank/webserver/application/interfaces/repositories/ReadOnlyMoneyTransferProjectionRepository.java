package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferProjection;

import java.util.UUID;

public interface ReadOnlyMoneyTransferProjectionRepository {
    MoneyTransferProjection getByIdOrDefault(UUID moneyTransferId);
}
