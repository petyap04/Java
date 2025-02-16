package bg.sofia.uni.fmi.mjt.todoist.server.command;

public enum CommandType {
    REGISTER("register"),
    LOGIN("login"),
    EXIT("exit"),
    ADD_TASK("add-task"),
    UPDATE_TASK("update-task"),
    DELETE_TASK("delete-task"),
    GET_TASK("get-task"),
    LIST_DASHBOARD("list-dashboard"),
    FINISH_TASK("finish-task"),
    ADD_COLLABORATION("add-collaboration"),
    DELETE_COLLABORATION("delete-collaboration"),
    LIST_COLLABORATIONS("list-collaborations"),
    ADD_USER("add-user"),
    ASSIGN_TASK("assign-task"),
    LIST_TASKS("list-tasks"),
    LIST_USERS("list-users"),
    ADD_LABEL("add-label"),
    DELETE_LABEL("delete-label"),
    LIST_LABELS("list-labels"),
    LABEL_TASK("label-task"),
    UNKNOWN("unknown");

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}