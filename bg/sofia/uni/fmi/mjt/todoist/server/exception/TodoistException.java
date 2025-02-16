package bg.sofia.uni.fmi.mjt.todoist.server.exception;

public class TodoistException extends Exception {
    public TodoistException(String message) {
        super(message);
    }

    public TodoistException(String message, Throwable cause) {
        super(message, cause);
    }
}
