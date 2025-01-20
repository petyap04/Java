package bg.sofia.uni.fmi.newsfeed.exception;

public class TooManyRequestsException extends NewsApiException {
    public TooManyRequestsException(String message) {
        super("Too Many Requests: " + message);
    }
}
