package bg.sofia.uni.fmi.mjt.todoist.server.exception.base;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.TodoistException;

public class LabelException extends TodoistException {
    public LabelException(String message) {
        super(message);
    }

    public LabelException(String message, Throwable cause) {
        super(message, cause);
    }
}
