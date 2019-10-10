package com.litebank.webserver.infrastructure.data;

import com.litebank.webserver.application.interfaces.EventBus;
import com.litebank.webserver.application.interfaces.repositories.MoneyTransferRepository;
import com.litebank.webserver.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.webserver.domain.model.moneytransfers.MoneyTransfer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InMemoryMoneyTransferRepository implements MoneyTransferRepository {
    private final EventBus eventBus;
    private final String moneyTransfersStreamId;

    public InMemoryMoneyTransferRepository(EventBus eventBus, String moneyTransfersStreamId) {
        this.eventBus = eventBus;
        this.moneyTransfersStreamId = moneyTransfersStreamId;
    }

    @Override
    public MoneyTransfer getById(UUID id) throws MoneyTransferNotFoundException {
        var moneyTransfer = new MoneyTransfer(id);

        var events = eventBus.loadEntity(moneyTransfersStreamId, id);

        if (events.isEmpty()) {
            throw new MoneyTransferNotFoundException("MoneyTransfer '" + id + "' not found");
        }

        moneyTransfer.apply(events);

        return moneyTransfer;
    }

    @Override
    public CompletableFuture<UUID> save(MoneyTransfer moneyTransfer) {
        var events = moneyTransfer.getNewEvents();

        var completableFutures = events.stream().map(event -> eventBus.addEventToStream(moneyTransfersStreamId, event)).collect(Collectors.toList());
        var completableFuturesArray = completableFutures.toArray(new CompletableFuture[completableFutures.size()]);

        return CompletableFuture.allOf(completableFuturesArray).thenApply(v -> moneyTransfer.getId());
    }
}
