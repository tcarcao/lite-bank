package com.litebank.service.domain.model.moneytransfers.events;

import com.litebank.service.domain.model.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public class MoneyTransferFailedDueToInvalidStateEvent extends Event {

    public MoneyTransferFailedDueToInvalidStateEvent(UUID aggregateId, LocalDateTime timestamp, int version) {
        super(aggregateId, timestamp, version);
    }
}
