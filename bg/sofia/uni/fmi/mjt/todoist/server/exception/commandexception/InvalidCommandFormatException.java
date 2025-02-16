package bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.CommandException;

public class InvalidCommandFormatException extends CommandException {
    public InvalidCommandFormatException(String message) {
        super(message);
    }
}
