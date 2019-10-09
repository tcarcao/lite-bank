package com.litebank.clients.java.interfaces;

import com.litebank.webserver.application.dtos.accounts.AccountOpenedDto;
import com.litebank.webserver.domain.model.accounts.projections.AccountProjection;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AccountsClient {
    CompletableFuture<AccountOpenedDto> openAccount(BigDecimal openingAmount, UUID customerId);

    CompletableFuture<Optional<AccountProjection>> getAccount(UUID accountId);
}
