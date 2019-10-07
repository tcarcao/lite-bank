package com.litebank.webserver.application.interfaces;

import com.litebank.webserver.domain.model.Event;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface EventBus {
    void addEventToStream(String streamId, Event event);

    List<Event> loadEntity(String streamId, UUID entityId);

    void subscribeToStream(String streamId, Consumer<List<Event>> consumer);
}
