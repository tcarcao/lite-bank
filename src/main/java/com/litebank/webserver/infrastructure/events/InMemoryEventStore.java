package com.litebank.webserver.infrastructure.events;

import com.litebank.webserver.application.interfaces.EventBus;
import com.litebank.webserver.domain.model.Event;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class InMemoryEventStore implements EventBus {

    private static Random random = new Random();

    private final Map<String, List<Consumer<List<Event>>>> streamsSubscriptions;
    private final Map<String, List<Event>> streams;

    public InMemoryEventStore() {
        this.streamsSubscriptions =  new ConcurrentHashMap<>();
        this.streams = new ConcurrentHashMap<>();
    }

    @Override
    public CompletableFuture<Void> addEventToStream(String streamId, Event event) {
        var completableFuture = new CompletableFuture<Void>();
        this.streams.putIfAbsent(streamId, new ArrayList<>());

        // just to simulate some randomness in the time taken
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        long latencyAdding = getLatencyAdding();

        scheduler.schedule(() -> {
            synchronized (this.streams) {
                var eventStreamObject = this.streams.get(streamId);

                // optimistic locking
                var eventsFromAggregate = eventStreamObject.stream().filter(e -> e.getAggregateId().equals(event.getAggregateId()));
                var lastEvent = eventsFromAggregate.reduce((first, second) -> second).orElse(null);

                if (lastEvent == null || lastEvent.getVersion() < event.getVersion()) {
                    eventStreamObject.add(event);
                }

                completableFuture.complete(null);
            }

            notifyListeners(streamId, event);
        }, latencyAdding, TimeUnit.MILLISECONDS);

        return completableFuture;
    }

    @Override
    public List<Event> loadEntity(String streamId, UUID entityId) {
        List<Event> events = emptyList();

        if (streams.containsKey(streamId)) {
            events = streams.get(streamId).stream()
                    .filter(event -> event.getAggregateId().equals(entityId))
                    .sorted(Comparator.comparingInt(Event::getVersion))
                    .collect(Collectors.toList());
        }

        return events;
    }

    @Override
    public void subscribeToStream(String streamId, Consumer<List<Event>> consumer) {
        this.streamsSubscriptions.putIfAbsent(streamId, new ArrayList<>());

        synchronized (this.streamsSubscriptions) {
            var streamSubscriptions = this.streamsSubscriptions.get(streamId);
            streamSubscriptions.add(consumer);
        }

        var events = this.streams.get(streamId);

        notifyListeners(streamId, events);
    }

    private void notifyListeners(String streamId, Event event) {
        notifyListeners(streamId, Arrays.asList(event));
    }

    private void notifyListeners(String streamId, List<Event> events) {
        if (events != null) {
            var streamSubscriptions = streamsSubscriptions.get(streamId);

            ExecutorService executorService = Executors.newCachedThreadPool();

            var eventsSorted = events.stream().sorted(Comparator.comparingInt(Event::getVersion)).collect(Collectors.toList());
            for (var streamSubscription : streamSubscriptions) {
                executorService.execute(() -> streamSubscription.accept(eventsSorted));
            }
        }
    }

    private long getLatencyAdding() {
        long minimumLatency = 5;

        long maximumLatency = random.nextInt() < 0.95 ? 100 : 500;

        return minimumLatency + maximumLatency;
    }
}
