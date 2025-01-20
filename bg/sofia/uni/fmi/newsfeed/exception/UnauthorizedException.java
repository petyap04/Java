package bg.sofia.uni.fmi.newsfeed.exception;

public class UnauthorizedException extends NewsApiException {
    public UnauthorizedException(String message) {
        super("Unauthorized: " + message);
    }
}
