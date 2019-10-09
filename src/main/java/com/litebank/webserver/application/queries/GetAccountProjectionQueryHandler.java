package com.litebank.webserver.application.queries;

import com.litebank.webserver.application.interfaces.cqrs.QueryHandler;
import com.litebank.webserver.application.interfaces.repositories.ReadOnlyAccountProjectionRepository;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;

public class GetAccountProjectionQueryHandler implements QueryHandler<GetAccountProjectionQuery, AccountProjection> {
    private final ReadOnlyAccountProjectionRepository accountProjectionRepository;

    public GetAccountProjectionQueryHandler(ReadOnlyAccountProjectionRepository accountProjectionRepository) {
        this.accountProjectionRepository = accountProjectionRepository;
    }

    public AccountProjection execute(GetAccountProjectionQuery query) {
        return accountProjectionRepository.getByIdOrDefault(query.getAccountId());
    }
}
