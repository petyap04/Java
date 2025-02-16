package bg.sofia.uni.fmi.mjt.todoist.server.user;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationUserExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions.LabelAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.labelexceptions.LabelDoesNotExistException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskAlreadyFinishedException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.server.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "Password123");
        task = new Task("Test Task", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 12), null);
    }

    @Test
    void testAddTaskSuccessfully() throws TaskExistsException {
        user.addTask(task);
        assertTrue(user.getUserTasks().contains(task));
    }

    @Test
    void testAddDuplicateTaskThrowsException() throws TaskExistsException {
        user.addTask(task);
        assertThrows(TaskExistsException.class, () -> user.addTask(task));
    }

    @Test
    void testUpdateTaskSuccessfully() throws TaskNotFoundException, TaskExistsException {
        user.addTask(task);
        Task updatedTask =
            new Task("Test Task", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 12), "Updated Description");
        user.updateTask(updatedTask);
        assertTrue(user.getUserTasks().contains(updatedTask));
    }

    @Test
    void testUpdateNonExistentTaskThrowsException() {
        assertThrows(TaskNotFoundException.class, () -> user.updateTask(task));
    }

    @Test
    void testDeleteTaskSuccessfully() throws TaskNotFoundException, TaskExistsException {
        user.addTask(task);
        user.deleteTask(task);
        assertFalse(user.getUserTasks().contains(task));
    }

    @Test
    void testDeleteNonExistentTaskThrowsException() {
        assertThrows(TaskNotFoundException.class, () -> user.deleteTask(task));
    }

    @Test
    void testGetTaskSuccessfully() throws TaskExistsException, TaskNotFoundException {
        user.addTask(task);
        assertEquals(task.toString(), user.getTask(task));
    }

    @Test
    void testGetNonExistentTaskThrowsException() {
        assertThrows(TaskNotFoundException.class, () -> user.getTask(task));
    }

    @Test
    void testFinishTaskSuccessfully() throws TaskExistsException, TaskNotFoundException, TaskAlreadyFinishedException {
        user.addTask(task);
        user.finishTask(task);
        assertTrue(task.isFinished());
    }

    @Test
    void testFinishAlreadyFinishedTaskThrowsException()
        throws TaskExistsException, TaskNotFoundException, TaskAlreadyFinishedException {
        user.addTask(task);
        user.finishTask(task);
        assertThrows(TaskAlreadyFinishedException.class, () -> user.finishTask(task));
    }

    @Test
    void testAddCollaborationSuccessfully() throws CollaborationUserExistsException {
        user.addCollaboration("ProjectX");
        assertTrue(user.getCollaborations().contains("ProjectX"));
    }

    @Test
    void testAddDuplicateCollaborationThrowsException() throws CollaborationUserExistsException {
        user.addCollaboration("ProjectX");
        assertThrows(CollaborationUserExistsException.class, () -> user.addCollaboration("ProjectX"));
    }

    @Test
    void testRemoveCollaborationSuccessfully() throws CollaborationUserExistsException {
        user.addCollaboration("ProjectX");
        user.removeCollaboration("ProjectX");
        assertFalse(user.getCollaborations().contains("ProjectX"));
    }

    @Test
    void testAddLabelSuccessfully() throws LabelAlreadyExistException {
        user.addLabel("Work");
        assertTrue(user.listLabels().contains("Work"));
    }

    @Test
    void testAddDuplicateLabelThrowsException() throws LabelAlreadyExistException {
        user.addLabel("Work");
        assertThrows(LabelAlreadyExistException.class, () -> user.addLabel("Work"));
    }

    @Test
    void testDeleteLabelSuccessfully() throws LabelAlreadyExistException, LabelDoesNotExistException {
        user.addLabel("Work");
        user.deleteLabel("Work");
        assertFalse(user.listLabels().contains("Work"));
    }

    @Test
    void testDeleteNonExistentLabelThrowsException() {
        assertThrows(LabelDoesNotExistException.class, () -> user.deleteLabel("Work"));
    }
}
