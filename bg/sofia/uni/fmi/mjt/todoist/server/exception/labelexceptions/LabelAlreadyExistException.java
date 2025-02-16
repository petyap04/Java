package bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.base.LabelException;

public class LabelAlreadyExistException extends LabelException {
    public LabelAlreadyExistException() {
        super("This label already exist for this user!");
    }
}
