package bg.sofia.uni.fmi.newsfeed.exception;

public class BadRequestException extends NewsApiException {
    public BadRequestException(String message) {
        super("Bad Request: " + message);
    }
}
