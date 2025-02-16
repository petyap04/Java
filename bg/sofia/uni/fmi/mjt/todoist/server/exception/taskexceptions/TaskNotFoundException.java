package bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.TaskException;

public class TaskNotFoundException extends TaskException {
    public TaskNotFoundException() {
        super("The task does not exist!");
    }
}
