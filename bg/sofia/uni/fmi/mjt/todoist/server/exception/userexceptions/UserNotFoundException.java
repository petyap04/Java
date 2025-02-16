package bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.UserException;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super("User with name does not exist!");
    }
}
