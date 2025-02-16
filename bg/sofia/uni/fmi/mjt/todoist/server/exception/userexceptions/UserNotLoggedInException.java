package bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.UserException;

public class UserNotLoggedInException extends UserException {
    public UserNotLoggedInException() {
        super("User not logged in");
    }
}
