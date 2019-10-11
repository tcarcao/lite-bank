package com.litebank.service.application.interfaces.repositories;

import com.litebank.service.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.service.domain.model.moneytransfers.MoneyTransfer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MoneyTransferRepository {
    MoneyTransfer getById(UUID id) throws MoneyTransferNotFoundException;

    CompletableFuture<UUID> save(MoneyTransfer moneyTransfer);
}
