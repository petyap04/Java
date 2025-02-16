package bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.UserException;

public class PasswordFormatException extends UserException {
    public PasswordFormatException(String message) {
        super(message);
    }

    public PasswordFormatException() {
        super("You are not following the needed format!");
    }
}
