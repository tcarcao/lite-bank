package com.litebank.webserver.application.queries;

import com.litebank.webserver.application.interfaces.cqrs.QueryHandler;
import com.litebank.webserver.application.interfaces.repositories.ReadOnlyMoneyTransferProjectionRepository;
import com.litebank.webserver.domain.model.moneytransfers.projections.MoneyTransferProjection;

public class GetMoneyTransferProjectionQueryHandler implements QueryHandler<GetMoneyTransferProjectionQuery, MoneyTransferProjection> {
    private final ReadOnlyMoneyTransferProjectionRepository moneyTransferProjectionRepository;

    public GetMoneyTransferProjectionQueryHandler(ReadOnlyMoneyTransferProjectionRepository moneyTransferProjectionRepository) {
        this.moneyTransferProjectionRepository = moneyTransferProjectionRepository;
    }

    public MoneyTransferProjection execute(GetMoneyTransferProjectionQuery query) {
        return moneyTransferProjectionRepository.getByIdOrDefault(query.getMoneyTransferId());
    }
}
