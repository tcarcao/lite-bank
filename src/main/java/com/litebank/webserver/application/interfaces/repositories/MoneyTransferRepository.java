package com.litebank.webserver.application.interfaces.repositories;

import com.litebank.webserver.domain.model.exceptions.MoneyTransferNotFoundException;
import com.litebank.webserver.domain.model.moneytransfers.MoneyTransfer;

import java.util.UUID;

public interface MoneyTransferRepository {
    MoneyTransfer getById(UUID id) throws MoneyTransferNotFoundException;

    UUID save(MoneyTransfer moneyTransfer);
}
