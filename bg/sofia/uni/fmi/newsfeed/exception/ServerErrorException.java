package bg.sofia.uni.fmi.newsfeed.exception;

public class ServerErrorException extends NewsApiException {
    public ServerErrorException(String message) {
        super("Server Error: " + message);
    }
}
