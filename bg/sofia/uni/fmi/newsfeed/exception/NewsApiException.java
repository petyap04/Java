package bg.sofia.uni.fmi.newsfeed.exception;

public class NewsApiException extends Exception {
    public NewsApiException(String message) {
        super(message);
    }

    public NewsApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
