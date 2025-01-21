package bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class TooManyRequestsException extends NewsApiException {
    public TooManyRequestsException(String message) {
        super("Too Many Requests: " + message);
    }
}
