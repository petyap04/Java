package bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.CollaborationException;

public class CollaborationNotFoundException extends CollaborationException {
    public CollaborationNotFoundException() {
        super("Collaboration with name does not exist");
    }
}
