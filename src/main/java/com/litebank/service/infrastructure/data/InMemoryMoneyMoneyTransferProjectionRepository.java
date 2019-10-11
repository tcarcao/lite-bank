package com.litebank.service.infrastructure.data;

import com.litebank.service.application.interfaces.repositories.MoneyTransferProjectionRepository;
import com.litebank.service.domain.model.moneytransfers.projections.MoneyTransferProjection;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMoneyMoneyTransferProjectionRepository implements MoneyTransferProjectionRepository {
    private final Map<UUID, MoneyTransferProjection> moneyTransferProjections;

    public InMemoryMoneyMoneyTransferProjectionRepository() {
        this.moneyTransferProjections = new ConcurrentHashMap<>();
    }

    @Override
    public MoneyTransferProjection getByIdOrDefault(UUID moneyTransferId) {
        return moneyTransferProjections.get(moneyTransferId);
    }

    @Override
    public void save(MoneyTransferProjection moneyTransferProjection) {
        synchronized (moneyTransferProjections) {
            moneyTransferProjections.put(moneyTransferProjection.getMoneyTransferId(), moneyTransferProjection);
        }
    }
}
