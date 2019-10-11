package com.litebank.service.application.interfaces.repositories;

import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferProjection;

public interface MoneyTransferProjectionRepository extends ReadOnlyMoneyTransferProjectionRepository {
    void save(MoneyTransferProjection moneyTransferProjection);
}
