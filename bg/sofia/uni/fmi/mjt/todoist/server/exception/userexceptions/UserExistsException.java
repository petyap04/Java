package bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.UserException;

public class UserExistsException extends UserException {
    public UserExistsException() {
        super("User with name already exists!");
    }
}
