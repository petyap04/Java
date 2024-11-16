package bg.sofia.uni.fmi.mjt.eventbus.exception;

public class MissingSubscriptionException extends RuntimeException {
    public MissingSubscriptionException() {
        super("You need subscription");
    }
}