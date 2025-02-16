package bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.CommandException;

public class DateArgumentFormatException extends CommandException {
    public DateArgumentFormatException() {
        super("Date arguments must be in format: dd-MM-yyyy");
    }
}
