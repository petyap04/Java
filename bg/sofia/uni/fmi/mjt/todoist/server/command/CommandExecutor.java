package bg.sofia.uni.fmi.mjt.todoist.server.command;

import bg.sofia.uni.fmi.mjt.todoist.server.application.Application;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.TodoistException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationUserExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.DateArgumentFormatException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.InvalidCommandFormatException;
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

import java.util.Arrays;
import java.util.Locale;
import java.time.LocalDate;
import java.nio.channels.SelectionKey;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CommandExecutor {
    private static final String ERROR_MESSAGE = "ERROR: ";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String NAME_PARAM = "--name=";
    private static final String DATE_PARAM = "--date=";
    private static final String DUE_DATE_PARAM = "--due-date=";
    private static final String DESCRIPTION_PARAM = "--description=";
    private static final String COMPLETED_PARAM = "--completed";
    private static final String COLLABORATION_PARAM = "--collaboration=";
    private static final String USER_PARAM = "--user=";
    private static final String TASK_PARAM = "--task=";
    private static final String LABEL_PARAM = "--label=";
    private static final int CREDENTIALS_SIZE = 2;
    private final DateTimeFormatter dateFormatter;
    private final Application application;

    public CommandExecutor(Application application) {
        this.application = application;
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
    }

    public String execute(SelectionKey key, Command command) {
        try {
            return executeCommand(key, command);
        } catch (TodoistException e) {
            System.out.println("A logic error has occurred: " + e.getMessage());
            return ERROR_MESSAGE + e.getMessage();
        } catch (Exception e) {
            System.out.println("An execution error has occurred: " + e.getMessage());
            return ERROR_MESSAGE + "An error has occurred during the processing of the request";
        }
    }

    private String executeCommand(SelectionKey key, Command command)
        throws TodoistException {
        return switch (getCommandType(command)) {
            case REGISTER -> register(key, command.arguments());
            case LOGIN -> login(key, command.arguments());
            case EXIT -> exit(key);
            case ADD_TASK -> addTask(key, command.arguments());
            case UPDATE_TASK -> updateTask(key, command.arguments());
            case DELETE_TASK -> deleteTask(key, command.arguments());
            case GET_TASK -> getTask(key, command.arguments());
            case LIST_TASKS -> listTasks(key, command.arguments());
            case LIST_DASHBOARD -> listDashboard(key);
            case FINISH_TASK -> finishTask(key, command.arguments());
            case ADD_COLLABORATION -> addCollaboration(key, command.arguments());
            case DELETE_COLLABORATION -> deleteCollaboration(key, command.arguments());
            case LIST_COLLABORATIONS -> listCollaborations(key);
            case ADD_USER -> addUser(key, command.arguments());
            case ASSIGN_TASK -> assignTask(key, command.arguments());
            case LIST_USERS -> listUsers(key, command.arguments());
            case ADD_LABEL -> addLabel(key, command.arguments());
            case DELETE_LABEL -> deleteLabel(key, command.arguments());
            case LIST_LABELS -> listLabels(key, command.arguments());
            case LABEL_TASK -> labelTask(key, command.arguments());
            case UNKNOWN -> unknown();
        };
    }

    private String register(SelectionKey key, String[] arguments)
        throws UserExistsException, PasswordFormatException, InvalidCommandFormatException, UserInSessionException {

        if (arguments.length != CREDENTIALS_SIZE) {
            throw new InvalidCommandFormatException("Invalid register command arguments count");
        }

        this.application.register(key, arguments[0], arguments[1]);
        return String.format("User %s registered successfully", arguments[0]);
    }

    private String login(SelectionKey key, String[] arguments)
        throws InvalidCredentialsException, InvalidCommandFormatException, UserInSessionException {

        if (arguments.length != CREDENTIALS_SIZE) {
            throw new InvalidCommandFormatException("Invalid login command arguments count");
        }

        this.application.login(key, arguments[0], arguments[1]);
        return String.format("User %s logged in successfully", arguments[0]);
    }

    private String exit(SelectionKey key) {
        this.application.exit(key);
        return "exit";
    }

    private String addTask(SelectionKey key, String[] arguments)
        throws DateArgumentFormatException, UserNotLoggedInException, TaskExistsException, TaskNameMissingException {

        String name = getArgument(arguments, NAME_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));
        LocalDate dueDate = toLocalDate(getArgument(arguments, DUE_DATE_PARAM));
        String description = getArgument(arguments, DESCRIPTION_PARAM);

        this.application.addTask(key, name, date, dueDate, description);
        return String.format("Task %s added successfully", name);
    }

    private String updateTask(SelectionKey key, String[] arguments)
        throws DateArgumentFormatException, TaskNotFoundException, UserNotLoggedInException, TaskNameMissingException {

        String name = getArgument(arguments, NAME_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));
        LocalDate dueDate = toLocalDate(getArgument(arguments, DUE_DATE_PARAM));
        String description = getArgument(arguments, DESCRIPTION_PARAM);

        application.updateTask(key, name, date, dueDate, description);
        return String.format("Task %s updated successfully", name);
    }

    private String deleteTask(SelectionKey key, String[] arguments)
        throws DateArgumentFormatException, TaskNotFoundException, UserNotLoggedInException, TaskNameMissingException {

        String name = getArgument(arguments, NAME_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));

        application.deleteTask(key, name, date);
        return String.format("Task %s deleted successfully", name);
    }

    private String getTask(SelectionKey key, String[] arguments)
        throws DateArgumentFormatException, TaskNotFoundException, UserNotLoggedInException, TaskNameMissingException {

        String name = getArgument(arguments, NAME_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));

        return application.getTask(key, name, date);
    }

    private String listTasks(SelectionKey key, String[] arguments)
        throws DateArgumentFormatException, UserNotLoggedInException, NoCollaborationPermissionsException,
        CollaborationNotFoundException {

        String collaboration = getArgument(arguments, COLLABORATION_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));
        boolean isCompleted = getArgument(arguments, COMPLETED_PARAM) != null;

        if (collaboration != null) {
            return this.application.listCollaborationTasks(key, collaboration);
        } else {
            return this.application.listTasks(key, date, isCompleted);
        }
    }

    private String listDashboard(SelectionKey key) throws UserNotLoggedInException {
        return application.listDashboard(key);
    }

    private String finishTask(SelectionKey key, String[] arguments)
        throws UserNotLoggedInException, DateArgumentFormatException, TaskNotFoundException,
        TaskAlreadyFinishedException, TaskNameMissingException {

        String name = getArgument(arguments, NAME_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));

        application.finishTask(key, name, date);
        return String.format("Task %s finished successfully", name);
    }

    private String addCollaboration(SelectionKey key, String[] arguments)
        throws CollaborationExistsException, UserNotLoggedInException, CollaborationUserExistsException {

        String name = getArgument(arguments, NAME_PARAM);

        application.addCollaboration(key, name);
        return String.format("Collaboration %s added successfully", name);
    }

    private String deleteCollaboration(SelectionKey key, String[] arguments)
        throws NoCollaborationPermissionsException, UserNotLoggedInException, CollaborationNotFoundException {

        String name = getArgument(arguments, NAME_PARAM);

        this.application.deleteCollaboration(key, name);
        return String.format("Collaboration %s deleted successfully", name);
    }

    private String listCollaborations(SelectionKey key) throws UserNotLoggedInException {
        return this.application.listCollaborations(key);
    }

    private String addUser(SelectionKey key, String[] arguments)
        throws UserNotFoundException, NoCollaborationPermissionsException, UserNotLoggedInException,
        CollaborationUserExistsException, CollaborationNotFoundException {

        String collaboration = getArgument(arguments, COLLABORATION_PARAM);
        String user = getArgument(arguments, USER_PARAM);

        this.application.addUser(key, collaboration, user);
        return String.format("User %s added successfully to collaboration", user);
    }

    private String assignTask(SelectionKey key, String[] arguments)
        throws UserNotFoundException, NoUserInCollaborationException, NoCollaborationPermissionsException,
        UserNotLoggedInException, CollaborationNotFoundException, DateArgumentFormatException, TaskExistsException,
        TaskNameMissingException {

        String collaboration = getArgument(arguments, COLLABORATION_PARAM);
        String user = getArgument(arguments, USER_PARAM);
        String task = getArgument(arguments, TASK_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));
        LocalDate dueDate = toLocalDate(getArgument(arguments, DUE_DATE_PARAM));
        String description = getArgument(arguments, DESCRIPTION_PARAM);

        application.addTask(key, collaboration, user, task, date, dueDate, description);
        return String.format("Task %s added successfully to collaboration", task);
    }

    private String listUsers(SelectionKey key, String[] arguments)
        throws NoCollaborationPermissionsException, UserNotLoggedInException, CollaborationNotFoundException {

        String collaboration = getArgument(arguments, COLLABORATION_PARAM);

        return application.listUsers(key, collaboration);
    }

    private String addLabel(SelectionKey key, String[] arguments)
        throws UserNotLoggedInException, LabelAlreadyExistException {
        String label = getArgument(arguments, NAME_PARAM);
        application.addLabel(key, label);
        return String.format("Label %s added successfully", label);
    }

    private String deleteLabel(SelectionKey key, String[] arguments)
        throws UserNotLoggedInException, LabelDoesNotExistException {
        String label = getArgument(arguments, NAME_PARAM);
        application.deleteLabel(key, label);
        return String.format("Label %s deleted successfully", label);
    }

    private String listLabels(SelectionKey key, String[] arguments) throws UserNotLoggedInException {
        return application.listLabels(key);
    }

    private String labelTask(SelectionKey key, String[] arguments)
        throws DateArgumentFormatException, TaskNotFoundException, UserNotLoggedInException,
        LabelDoesNotExistException {
        String task = getArgument(arguments, NAME_PARAM);
        LocalDate date = toLocalDate(getArgument(arguments, DATE_PARAM));
        String label = getArgument(arguments, LABEL_PARAM);
        application.labelTask(key, task, date, label);
        return String.format("Label %s added successfully to task", label);
    }

    private String getArgument(String[] arguments, String argumentPattern) {
        for (String argument : arguments) {
            if (argument.startsWith(argumentPattern)) {
                return argument.substring(argumentPattern.length());
            }
        }

        return null;
    }

    private LocalDate toLocalDate(String dateArgument) throws DateArgumentFormatException {
        if (dateArgument == null) {
            return null;
        }

        try {
            return LocalDate.parse(dateArgument, this.dateFormatter);
        } catch (DateTimeParseException e) {
            throw new DateArgumentFormatException();
        }
    }

    private String unknown() {
        return "Unknown command";
    }

    private CommandType getCommandType(Command command) {
        return Arrays.stream(CommandType.values()).filter(type -> type.getName().equalsIgnoreCase(command.name()))
            .findFirst().orElse(CommandType.UNKNOWN);
    }
}