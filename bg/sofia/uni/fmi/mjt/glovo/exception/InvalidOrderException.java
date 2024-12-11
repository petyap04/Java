package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String str) {
        super(str);
    }
}