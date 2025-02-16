package bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.LabelException;

public class LabelDoesNotExistException extends LabelException {
    public LabelDoesNotExistException() {
        super("This label does not exist for this user!");
    }
}
