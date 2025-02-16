package bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.CollaborationException;

public class NoCollaborationPermissionsException extends CollaborationException {
    public NoCollaborationPermissionsException() {
        super("Cannot view or add users and tasks to collaboration that you don't own");
    }
}
