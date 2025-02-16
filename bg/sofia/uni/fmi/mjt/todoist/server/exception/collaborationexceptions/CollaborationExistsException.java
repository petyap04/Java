package bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.CollaborationException;

public class CollaborationExistsException extends CollaborationException {
    public CollaborationExistsException() {
        super("Collaboration with name already exists");
    }
}
