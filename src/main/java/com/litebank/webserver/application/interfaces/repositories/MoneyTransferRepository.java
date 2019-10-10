package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.webserver.domain.model.moneytransfers.MoneyTransfer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MoneyTransferRepository {
    MoneyTransfer getById(UUID id) throws MoneyTransferNotFoundException;

    CompletableFuture<UUID> save(MoneyTransfer moneyTransfer);
}
