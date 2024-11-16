package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public interface Subscriber<T extends Event<?>> {

    void onEvent(T event);

}