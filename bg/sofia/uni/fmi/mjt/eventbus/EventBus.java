package bg.sofia.uni.fmi.mjt.eventbus;

import java.time.Instant;
import java.util.Collection;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

public interface EventBus {

    <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber);

    <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException;

    <T extends Event<?>> void publish(T event);

    void clear();

    Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from,
                                                Instant to);

    <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType);

}