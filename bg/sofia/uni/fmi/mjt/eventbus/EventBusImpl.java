package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EventBusImpl implements EventBus {

    private final Map<Class<? extends Event<?>>, Collection<Subscriber<?>>> subscribers;
    private final Collection<Event<?>> events;

    EventBusImpl() {
        subscribers = new HashMap<>();
        events = new HashSet<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null) {
            throw new IllegalArgumentException();
        }

        if (subscriber == null) {
            throw new IllegalArgumentException();
        }

        Collection<Subscriber<?>> currSubscribers = subscribers.get(eventType);

        if (currSubscribers == null) {
            currSubscribers = new HashSet<>();
            subscribers.put(eventType, currSubscribers);
        }

        currSubscribers.add(subscriber);
        currSubscribers.add(subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException {
        if (eventType == null) {
            throw new IllegalArgumentException();
        }

        if (subscriber == null) {
            throw new IllegalArgumentException();
        }

        Collection<Subscriber<?>> currSubscribers = subscribers.get(eventType);

        if (currSubscribers == null || !currSubscribers.remove(subscriber)) {
            throw new MissingSubscriptionException();
        }

        if (currSubscribers.isEmpty()) {
            subscribers.remove(eventType);
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }

        Collection<Subscriber<?>> subscribersForEvent = subscribers.get(event.getClass());

        if (subscribersForEvent != null) {
            for (Subscriber<?> currentSubscriber : subscribersForEvent) {
                @SuppressWarnings("unchecked")
                Subscriber<? super T> typedSubscriber = (Subscriber<? super T>) currentSubscriber;
                typedSubscriber.onEvent(event);
            }
        }

        events.add(event);
    }

    @Override
    public void clear() {
        subscribers.clear();
        events.clear();
    }

    @SuppressWarnings("checkstyle:LineLength")
    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null) {
            throw new IllegalArgumentException();
        }

        if (from == null) {
            throw new IllegalArgumentException();
        }

        if (to == null) {
            throw new IllegalArgumentException();
        }

        if (events == null) {
            return Collections.emptyList();
        }

        List<Event<?>> filteredEvents = new ArrayList<>();

        for (Event<?> event : events) {
            if (eventType.isInstance(event) &&
                    !event.getTimestamp().isBefore(from) && event.getTimestamp().isBefore(to)) {
                filteredEvents.add(event);
            }
        }

        filteredEvents.sort(Comparator.comparing(Event::getTimestamp));
        return Collections.unmodifiableCollection(filteredEvents);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("");
        }

        Collection<Subscriber<?>> currSubscribers = subscribers.get(eventType);
        if (currSubscribers == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableCollection(currSubscribers);
    }
}