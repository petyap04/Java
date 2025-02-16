package bg.sofia.uni.fmi.mjt.todoist.server.user;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions.LabelAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions.LabelDoesNotExistException;
import bg.sofia.uni.fmi.mjt.todoist.server.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.server.database.identifiable.Identifiable;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskAlreadyFinishedException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationUserExistsException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.stream.Collectors;

public class User implements Identifiable, Serializable {
    private final String username;
    private final String password;
    private final Set<Task> userTasks;
    private final Map<String, Set<Task>> userTasksByLabels;
    private final Set<String> collaborations;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.userTasks = new HashSet<>();
        this.userTasksByLabels = new LinkedHashMap<>();
        this.collaborations = new HashSet<>();
    }

    public boolean arePasswordsEqual(String password) {
        return this.password.equals(password);
    }

    public Set<Task> getUserTasks() {
        return userTasks;
    }

    public Set<String> getCollaborations() {
        return collaborations;
    }

    public void addTask(Task task) throws TaskExistsException {
        if (task == null) {
            throw new IllegalArgumentException("The task can not be null!");
        }

        if (userTasks.contains(task)) {
            throw new TaskExistsException();
        }

        userTasks.add(task);
    }

    public void updateTask(Task task) throws TaskNotFoundException {
        if (task == null) {
            throw new IllegalArgumentException("Cannot update a null task for user");
        }

        if (!userTasks.contains(task)) {
            throw new TaskNotFoundException();
        }

        userTasks.remove(task);
        userTasks.add(task);
    }

    public void deleteTask(Task task) throws TaskNotFoundException {
        if (task == null) {
            throw new IllegalArgumentException("The task can not be null!");
        }

        if (!userTasks.contains(task)) {
            throw new TaskNotFoundException();
        }

        this.userTasks.remove(task);
    }

    public String getTask(Task targetTask) throws TaskNotFoundException {
        if (targetTask == null) {
            throw new IllegalArgumentException("The task can not be null!");
        }

        return userTasks.stream().filter(task -> task.equals(targetTask)).findFirst()
            .map(Task::toString)//по този начин преработваме за да стане от Optional<Task> до Optional<String>
            // и ако не съществува ще върне празен Optional
            .orElseThrow(TaskNotFoundException::new);
    }

    public String listTasks(LocalDate targetDate, boolean isCompleted) {
        return userTasks.stream().filter(task -> task.hasEqualDate(targetDate) && task.isFinished() == isCompleted)
            .map(Task::toString).collect(Collectors.joining(System.lineSeparator()));
    }

    public String listDashboard() {
        return this.listTasks(null, false);
    }

    public void finishTask(Task targetTask) throws TaskNotFoundException, TaskAlreadyFinishedException {
        if (targetTask == null) {
            throw new IllegalArgumentException("The task can not be null!");
        }

        if (!userTasks.contains(targetTask)) {
            throw new TaskNotFoundException();
        }

        Task userTask = userTasks.stream().filter(task -> task.equals(targetTask)).findFirst()
            .orElseThrow(TaskNotFoundException::new);

        userTask.markAsFinished();
    }

    public void addCollaboration(String name) throws CollaborationUserExistsException {
        if (name == null) {
            throw new IllegalArgumentException("The task can not be null!");
        }

        if (collaborations.contains(name)) {
            throw new CollaborationUserExistsException();
        }

        collaborations.add(name);
    }

    public String listCollaborations() {
        return collaborations.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public void removeCollaboration(String collaboration) {
        this.collaborations.remove(collaboration);
    }

    public void addLabel(String label) throws LabelAlreadyExistException {
        if (label == null) {
            throw new IllegalArgumentException("The label cannot be null!");
        }
        if (userTasksByLabels.containsKey(label)) {
            throw new LabelAlreadyExistException();
        }
        userTasksByLabels.put(label, new HashSet<>());
    }

    public void deleteLabel(String label) throws LabelDoesNotExistException {
        if (label == null) {
            throw new IllegalArgumentException("The label cannot be null!");
        }
        if (!userTasksByLabels.containsKey(label)) {
            throw new LabelDoesNotExistException();
        }
        userTasksByLabels.remove(label);
    }

    public String listLabels() {
        return userTasksByLabels.keySet().stream().sorted().collect(Collectors.joining(System.lineSeparator()));
    }

    public void labelTask(Task task, String label) throws LabelDoesNotExistException {
        if (task == null) {
            throw new IllegalArgumentException("The task cannot be null!");
        }
        if (label == null) {
            throw new IllegalArgumentException("The label cannot be null!");
        }
        if (!userTasksByLabels.containsKey(label)) {
            throw new LabelDoesNotExistException();
        }

        userTasksByLabels.get(label).add(task);
    }

    @Override
    public String getId() {
        return this.username;
    }
}
