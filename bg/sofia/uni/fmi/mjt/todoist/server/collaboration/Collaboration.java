package bg.sofia.uni.fmi.mjt.todoist.server.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.server.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.database.identifiable.Identifiable;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.NoUserInCollaborationException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationUserExistsException;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;
import java.util.stream.Collectors;

public class Collaboration implements Identifiable, Serializable {
    private final String name;
    private final String ownerId;
    private final Set<String> users;
    private final Map<String, Set<Task>> collaborationTasks;

    public Collaboration(String name, String ownerId) {
        this.name = name;
        this.ownerId = ownerId;
        this.users = new HashSet<>();
        this.collaborationTasks = new HashMap<>();

        users.add(ownerId);
        this.collaborationTasks.put(ownerId, new HashSet<>());
    }

    public void addUser(String username) throws CollaborationUserExistsException {
        if (username == null) {
            throw new IllegalArgumentException("Cannot add a null user to collaboration");
        }

        if (this.users.contains(username)) {
            throw new CollaborationUserExistsException();
        }

        this.users.add(username);
        this.collaborationTasks.put(username, new HashSet<>());
    }

    public Set<String> getUsers() {
        return this.users;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public String listTasks() {
        return this.collaborationTasks.entrySet().stream()
            .flatMap(entry -> entry.getValue().stream().map(task -> task.toString() + " Assignee:" + entry.getKey()))
            .collect(Collectors.joining(System.lineSeparator()));
    }

    public void addTask(User assignee, Task task) throws NoUserInCollaborationException, TaskExistsException {
        if (task == null) {
            throw new IllegalArgumentException("Cannot add a null task to collaboration");
        }

        if (assignee == null) {
            throw new IllegalArgumentException("Cannot add a task to null assignee in collaboration");
        }

        if (!this.users.contains(assignee.getId())) {
            throw new NoUserInCollaborationException();
        }

        Set<Task> assigneeTasks = this.collaborationTasks.get(assignee.getId());

        if (assigneeTasks.contains(task)) {
            throw new TaskExistsException();
        }
        assigneeTasks.add(task);
    }

    public String listUsers() {
        return this.users.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String getId() {
        return this.name;
    }
}
