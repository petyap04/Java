package bg.sofia.uni.fmi.mjt.todoist.server.exception.base;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.TodoistException;

public class CommandException extends TodoistException {
    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
