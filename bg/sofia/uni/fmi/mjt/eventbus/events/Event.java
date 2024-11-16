package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public interface Event<T extends Payload<?>> extends Comparable<Event<T>> {

    Instant getTimestamp();

    int getPriority();

    String getSource();

    T getPayload();

    public default int compareTo(Event<T> o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }
}