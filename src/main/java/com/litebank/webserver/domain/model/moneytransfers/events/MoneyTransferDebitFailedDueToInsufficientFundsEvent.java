package com.litebank.webserver.domain.model.moneytransfers.events;

import com.litebank.webserver.domain.model.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public class MoneyTransferDebitFailedDueToInsufficientFundsEvent extends Event {

    public MoneyTransferDebitFailedDueToInsufficientFundsEvent(UUID aggregateId, LocalDateTime timestamp, int version) {
        super(aggregateId, timestamp, version);
    }
}
