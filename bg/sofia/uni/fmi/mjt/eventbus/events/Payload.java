package bg.sofia.uni.fmi.mjt.eventbus.events;

public interface Payload<T> {

    int getSize();

    T getPayload();
}