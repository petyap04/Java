package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {
    private final TreeSet<T> events;

    public DeferredEventSubscriber() {
        this.events = new TreeSet<>(
                Comparator.<T>comparingInt(Event::getPriority)
                        .thenComparing(Event::getTimestamp)
        );
    }

    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }
        events.add(event);
    }

    @Override
    public Iterator<T> iterator() {
        return events.iterator();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}