package bg.sofia.uni.fmi.mjt.todoist.server.exception.base;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.TodoistException;

public class UserException extends TodoistException {
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
