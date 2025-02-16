package bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.TaskException;

public class TaskNameMissingException extends TaskException {
    public TaskNameMissingException() {
        super("Cannot create a task with no name");
    }
}
