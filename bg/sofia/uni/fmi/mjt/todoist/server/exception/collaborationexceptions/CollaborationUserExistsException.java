package bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.CollaborationException;

public class CollaborationUserExistsException extends CollaborationException {
    public CollaborationUserExistsException() {
        super("User already added to collaboration ");
    }
}
