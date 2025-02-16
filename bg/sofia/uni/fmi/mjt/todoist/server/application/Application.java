package bg.sofia.uni.fmi.mjt.todoist.server.application;

import bg.sofia.uni.fmi.mjt.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.server.database.CollaborationDatabase;
import bg.sofia.uni.fmi.mjt.todoist.server.database.UserDatabase;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationUserExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.InvalidCredentialsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.NoCollaborationPermissionsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.NoUserInCollaborationException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.PasswordFormatException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions.LabelAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions.LabelDoesNotExistException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskAlreadyFinishedException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskNameMissingException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserInSessionException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.todoist.server.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.server.user.PasswordManager;
import bg.sofia.uni.fmi.mjt.todoist.server.user.User;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.nio.channels.SelectionKey;

public class Application {
    private final UserDatabase userDatabase;
    private final CollaborationDatabase collaborationDatabase;
    private final Map<SelectionKey, User> sessionUsers;

    public Application(UserDatabase userDatabase, CollaborationDatabase collaborationDatabase) {
        this.userDatabase = userDatabase;
        this.collaborationDatabase = collaborationDatabase;
        this.sessionUsers = new HashMap<>();
    }

    public void register(SelectionKey key, String username, String password)
        throws UserExistsException, PasswordFormatException, UserInSessionException {
        if (sessionUsers.containsKey(key)) {
            throw new UserInSessionException();
        }

        PasswordManager.validate(password);
        String hashedPassword = PasswordManager.hashPassword(password);

        User user = userDatabase.create(username, hashedPassword);
        sessionUsers.put(key, user);
        userDatabase.save();
    }

    public void login(SelectionKey key, String username, String password)
        throws InvalidCredentialsException, UserInSessionException {
        if (sessionUsers.containsKey(key)) {
            throw new UserInSessionException();
        }

        String hashedPassword = PasswordManager.hashPassword(password);

        User user;
        try {
            user = userDatabase.get(username);
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }

        if (!user.arePasswordsEqual(hashedPassword)) {
            throw new InvalidCredentialsException();
        }
        sessionUsers.put(key, user);
    }

    public void addTask(SelectionKey key, String name, LocalDate date, LocalDate dueDate, String description)
        throws UserNotLoggedInException, TaskExistsException, TaskNameMissingException {
        User user = getUserFromSession(key);

        if (name == null) {
            throw new TaskNameMissingException();
        }

        user.addTask(new Task(name, date, dueDate, description));
        userDatabase.save();
    }

    public void updateTask(SelectionKey key, String name, LocalDate date, LocalDate dueDate, String description)
        throws UserNotLoggedInException, TaskNotFoundException, TaskNameMissingException {
        User user = getUserFromSession(key);

        if (name == null) {
            throw new TaskNameMissingException();
        }

        user.updateTask(new Task(name, date, dueDate, description));
        userDatabase.save();
    }

    public void deleteTask(SelectionKey key, String name, LocalDate date)
        throws UserNotLoggedInException, TaskNotFoundException, TaskNameMissingException {
        User user = getUserFromSession(key);

        if (name == null) {
            throw new TaskNameMissingException();
        }

        user.deleteTask(new Task(name, date, null, null));
        userDatabase.save();
    }

    public String getTask(SelectionKey key, String name, LocalDate date)
        throws UserNotLoggedInException, TaskNotFoundException, TaskNameMissingException {
        User user = getUserFromSession(key);

        if (name == null) {
            throw new TaskNameMissingException();
        }

        return user.getTask(new Task(name, date, null, null));
    }

    public String listTasks(SelectionKey key, LocalDate date, boolean isCompleted) throws UserNotLoggedInException {
        User user = getUserFromSession(key);
        String result = user.listTasks(date, isCompleted);

        if (result == null || result.isBlank()) {
            return "No tasks found matching these parameters";
        } else {
            return result;
        }
    }

    public String listDashboard(SelectionKey key) throws UserNotLoggedInException {
        User user = getUserFromSession(key);
        String result = user.listDashboard();

        if (result == null || result.isBlank()) {
            return "No incomplete tasks for today";
        } else {
            return result;
        }
    }

    public void finishTask(SelectionKey key, String name, LocalDate date)
        throws UserNotLoggedInException, TaskNotFoundException, TaskAlreadyFinishedException, TaskNameMissingException {
        User user = getUserFromSession(key);

        if (name == null) {
            throw new TaskNameMissingException();
        }

        user.finishTask(new Task(name, date, null, null));
        this.userDatabase.save();
    }

    public void addCollaboration(SelectionKey key, String name)
        throws CollaborationExistsException, UserNotLoggedInException, CollaborationUserExistsException {
        User user = getUserFromSession(key);
        collaborationDatabase.create(name, user.getId());
        user.addCollaboration(name);

        persistChanges();
    }

    public void deleteCollaboration(SelectionKey key, String name)
        throws UserNotLoggedInException, NoCollaborationPermissionsException, CollaborationNotFoundException {
        User user = getUserFromSession(key);
        Collaboration collab = this.collaborationDatabase.delete(name, user.getId());
        Set<String> users = collab.getUsers();
        this.userDatabase.removeCollaborations(users, name);

        persistChanges();
    }

    public String listCollaborations(SelectionKey key) throws UserNotLoggedInException {
        User user = getUserFromSession(key);
        String result = user.listCollaborations();

        if (result == null || result.isBlank()) {
            return "No collaborations found";
        } else {
            return result;
        }
    }

    public void addUser(SelectionKey key, String collaborationName, String username)
        throws UserNotLoggedInException, UserNotFoundException, CollaborationNotFoundException,
        CollaborationUserExistsException, NoCollaborationPermissionsException {
        User user = getUserFromSession(key);
        Collaboration collab = collaborationDatabase.get(collaborationName);

        if (!collab.getOwnerId().equals(user.getId())) {
            throw new NoCollaborationPermissionsException();
        }

        User targetUser = userDatabase.get(username);
        collab.addUser(targetUser.getId());
        targetUser.addCollaboration(collaborationName);

        persistChanges();
    }

    public void addTask(SelectionKey key, String collaborationName, String username, String taskName, LocalDate date,
                        LocalDate dueDate, String description)
        throws UserNotLoggedInException, UserNotFoundException, CollaborationNotFoundException,
        NoUserInCollaborationException, NoCollaborationPermissionsException, TaskExistsException,
        TaskNameMissingException {
        User user = getUserFromSession(key);
        Collaboration collab = collaborationDatabase.get(collaborationName);

        if (!collab.getOwnerId().equals(user.getId())) {
            throw new NoCollaborationPermissionsException();
        }

        if (taskName == null) {
            throw new TaskNameMissingException();
        }

        User targetUser = userDatabase.get(username);
        collab.addTask(targetUser, new Task(taskName, date, dueDate, description));

        persistChanges();
    }

    public String listCollaborationTasks(SelectionKey key, String collaborationName)
        throws UserNotLoggedInException, CollaborationNotFoundException, NoCollaborationPermissionsException {
        User user = getUserFromSession(key);
        Collaboration collab = collaborationDatabase.get(collaborationName);

        if (!collab.getUsers().contains(user.getId())) {
            throw new NoCollaborationPermissionsException();
        }

        String result = collab.listTasks();
        if (result == null || result.isBlank()) {
            return "No collaboration tasks found";
        } else {
            return result;
        }
    }

    public String listUsers(SelectionKey key, String collaborationName)
        throws UserNotLoggedInException, CollaborationNotFoundException, NoCollaborationPermissionsException {
        User user = getUserFromSession(key);
        Collaboration collab = collaborationDatabase.get(collaborationName);

        if (!collab.getUsers().contains(user.getId())) {
            throw new NoCollaborationPermissionsException();
        }

        return collab.listUsers();
    }

    public void addLabel(SelectionKey key, String label) throws UserNotLoggedInException, LabelAlreadyExistException {
        User user = getUserFromSession(key);
        user.addLabel(label);
    }

    public void deleteLabel(SelectionKey key, String label)
        throws UserNotLoggedInException, LabelDoesNotExistException {
        User user = getUserFromSession(key);
        user.deleteLabel(label);
    }

    public String listLabels(SelectionKey key) throws UserNotLoggedInException {
        User user = getUserFromSession(key);
        return user.listLabels();
    }

    public void labelTask(SelectionKey key, String task, LocalDate date, String label)
        throws UserNotLoggedInException, TaskNotFoundException, LabelDoesNotExistException {
        User user = getUserFromSession(key);
        Task targetedTask = new Task(task, date, null, null);
        user.getTask(targetedTask);
        user.labelTask(targetedTask, label);
    }

    public void exit(SelectionKey key) {
        persistChanges();
        sessionUsers.remove(key);
    }

    private User getUserFromSession(SelectionKey key) throws UserNotLoggedInException {
        if (!this.sessionUsers.containsKey(key)) {
            throw new UserNotLoggedInException();
        }
        return sessionUsers.get(key);
    }

    private void persistChanges() {
        this.userDatabase.save();
        this.collaborationDatabase.save();
    }
}
