package bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.UserException;

public class UserInSessionException extends UserException {
    public UserInSessionException() {
        super("User is already logged in");
    }
}
