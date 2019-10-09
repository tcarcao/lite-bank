package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferProjection;

public interface MoneyTransferProjectionRepository extends ReadOnlyMoneyTransferProjectionRepository {
    void save(MoneyTransferProjection moneyTransferProjection);
}
