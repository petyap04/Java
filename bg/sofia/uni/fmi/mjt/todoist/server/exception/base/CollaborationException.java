package bg.sofia.uni.fmi.mjt.todoist.server.exception.base;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.TodoistException;

public class CollaborationException extends TodoistException {
    public CollaborationException(String message) {
        super(message);
    }

    public CollaborationException(String message, Throwable cause) {
        super(message, cause);
    }
}
