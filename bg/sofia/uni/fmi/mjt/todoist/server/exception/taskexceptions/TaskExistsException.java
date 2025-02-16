package bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.TaskException;

public class TaskExistsException extends TaskException {
    public TaskExistsException() {
        super("This task already exists for this user");
    }
}
