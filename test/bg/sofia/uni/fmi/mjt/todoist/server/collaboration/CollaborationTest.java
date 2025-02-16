package bg.sofia.uni.fmi.mjt.todoist.server.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.CollaborationUserExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.NoUserInCollaborationException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollaborationTest {
    private Collaboration collaboration;
    private User owner;
    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        owner = new User("owner", "password");
        user = new User("user1", "password");
        task = new Task("Task 1", null, null, "Test task");
        collaboration = new Collaboration("ProjectX", owner.getId());
    }

    @Test
    void testAddUserSuccessfully() throws CollaborationUserExistsException {
        collaboration.addUser(user.getId());
        assertTrue(collaboration.getUsers().contains(user.getId()));
    }

    @Test
    void testAddDuplicateUserThrowsException() throws CollaborationUserExistsException {
        collaboration.addUser(user.getId());
        assertThrows(CollaborationUserExistsException.class, () -> collaboration.addUser(user.getId()));
    }

    @Test
    void testAddTaskSuccessfully()
        throws NoUserInCollaborationException, TaskExistsException, CollaborationUserExistsException {
        collaboration.addUser(user.getId());
        collaboration.addTask(user, task);
        assertTrue(collaboration.listTasks().contains(task.toString()));
    }

    @Test
    void testAddTaskToNonMemberThrowsException() {
        assertThrows(NoUserInCollaborationException.class, () -> collaboration.addTask(user, task));
    }

    @Test
    void testAddDuplicateTaskThrowsException()
        throws NoUserInCollaborationException, TaskExistsException, CollaborationUserExistsException {
        collaboration.addUser(user.getId());
        collaboration.addTask(user, task);
        assertThrows(TaskExistsException.class, () -> collaboration.addTask(user, task));
    }

    @Test
    void testListUsers() throws CollaborationUserExistsException {
        collaboration.addUser(user.getId());
        assertTrue(collaboration.listUsers().contains(user.getId()));
    }

    @Test
    void testGetOwnerId() {
        assertEquals(owner.getId(), collaboration.getOwnerId());
    }
}
