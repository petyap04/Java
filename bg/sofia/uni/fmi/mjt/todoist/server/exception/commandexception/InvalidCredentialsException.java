package bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.UserException;

public class InvalidCredentialsException extends UserException {
    public InvalidCredentialsException() {
        super("The provided username or password is wrong!");
    }

}
