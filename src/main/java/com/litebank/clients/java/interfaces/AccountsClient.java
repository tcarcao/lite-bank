package com.litebank.clients.java.interfaces;

import com.litebank.webserver.application.dtos.accounts.AccountDto;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AccountsClient {
    CompletableFuture<AccountDto> openAccount(BigDecimal openingAmount, UUID customerId);

    CompletableFuture<Optional<AccountDto>> getAccount(UUID accountId);
}
