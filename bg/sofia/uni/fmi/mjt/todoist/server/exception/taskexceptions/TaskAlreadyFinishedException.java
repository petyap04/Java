package bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.TaskException;

public class TaskAlreadyFinishedException extends TaskException {
    public TaskAlreadyFinishedException() {
        super("Cannot mark a task as finished when it is already finished");
    }
}
