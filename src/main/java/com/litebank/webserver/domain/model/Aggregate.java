package com.litebank.webserver.domain.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class Aggregate {
    private UUID id;
    private int baseVersion;
    private List<Event> newEvents;

    public Aggregate(UUID id) {
        this.id = id;
        this.newEvents = new ArrayList<>();
    }

    protected int getNextVersion() {
        return baseVersion + newEvents.size() + 1;
    }

    public UUID getId() {
        return id;
    }

    public List<Event> getNewEvents() {
        return newEvents;
    }

    public void apply(Event event) {
        try {
            var applyMethod = this.getClass().getMethod("apply", event.getClass());

            applyMethod.invoke(this, event);
        }
        catch (SecurityException e) { }
        catch (NoSuchMethodException e) { }
        catch (IllegalAccessException e) { }
        catch (InvocationTargetException e) { }
    }

    protected void applyNewEvent(Event event) {
        newEvents.add(event);

        this.apply(event.getClass().cast(event));
    }

    public void apply(List<Event> events) {
        for (var event : events) {
            this.apply(event);

            this.baseVersion = event.getVersion();
        }
    }

    public Optional<Event> getNewerEvent() {
        if (newEvents.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(newEvents.get(newEvents.size() - 1));
    }
}
