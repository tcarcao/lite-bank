package com.litebank.clients.java.interfaces;

import com.litebank.service.application.dtos.moneytransfers.MoneyTransferDto;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MoneyTransfersClient {
    CompletableFuture<MoneyTransferDto> createMoneyTransfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currencyCode);

    CompletableFuture<Optional<MoneyTransferDto>> getMoneyTransfer(UUID moneyTransfer);
}
