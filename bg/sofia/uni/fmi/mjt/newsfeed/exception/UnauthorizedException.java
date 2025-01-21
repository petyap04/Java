package bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class UnauthorizedException extends NewsApiException {
    public UnauthorizedException(String message) {
        super("Unauthorized: " + message);
    }
}
