package com.litebank.service.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Event {
    private final UUID aggregateId;
    private final LocalDateTime timestamp;
    private final int version;

    public Event(UUID aggregateId, LocalDateTime timestamp, int version) {
        this.aggregateId = aggregateId;
        this.timestamp = timestamp;
        this.version = version;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getVersion() {
        return version;
    }
}
