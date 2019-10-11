package com.litebank.service.application.queries;

import com.litebank.service.application.interfaces.cqrs.QueryHandler;
import com.litebank.service.application.interfaces.repositories.ReadOnlyMoneyTransferProjectionRepository;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferProjection;

public class GetMoneyTransferProjectionQueryHandler implements QueryHandler<GetMoneyTransferProjectionQuery, MoneyTransferProjection> {
    private final ReadOnlyMoneyTransferProjectionRepository moneyTransferProjectionRepository;

    public GetMoneyTransferProjectionQueryHandler(ReadOnlyMoneyTransferProjectionRepository moneyTransferProjectionRepository) {
        this.moneyTransferProjectionRepository = moneyTransferProjectionRepository;
    }

    public MoneyTransferProjection execute(GetMoneyTransferProjectionQuery query) {
        return moneyTransferProjectionRepository.getByIdOrDefault(query.getMoneyTransferId());
    }
}
