package bg.sofia.uni.fmi.mjt.todoist.server.exception.base;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.TodoistException;

public class TaskException extends TodoistException {
    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
