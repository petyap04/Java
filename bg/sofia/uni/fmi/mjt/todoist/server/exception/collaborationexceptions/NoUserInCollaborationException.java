package bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.CollaborationException;

public class NoUserInCollaborationException extends CollaborationException {
    public NoUserInCollaborationException() {
        super("Cannot add a task to user that is not in the collaboration");
    }
}
