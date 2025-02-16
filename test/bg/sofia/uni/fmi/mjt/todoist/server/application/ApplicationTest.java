package bg.sofia.uni.fmi.mjt.todoist.server.application;

import bg.sofia.uni.fmi.mjt.todoist.server.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.server.database.CollaborationDatabase;
import bg.sofia.uni.fmi.mjt.todoist.server.database.UserDatabase;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.collaborationexceptions.NoCollaborationPermissionsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.InvalidCredentialsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.PasswordFormatException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskNameMissingException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserInSessionException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserNotLoggedInException;
import bg.sofia.uni.fmi.mjt.todoist.server.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SelectionKey;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {

    private final SelectionKey mockSelectionKey = mock(SelectionKey.class);
    private final SelectionKey mockInitialKey = mock(SelectionKey.class);
    private final User testUser = mock(User.class);
    private final LocalDate testDate = LocalDate.now();
    private final String initialUserName = "initialTest";
    private final Task testTask = new Task("test", testDate, testDate, "test");

    @Mock
    private UserDatabase mockUserDb;
    @Mock
    private CollaborationDatabase mockCollabDb;
    @InjectMocks
    private Application testApp;

    @BeforeEach
    void setupData() throws Exception {
        String passHash = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";
        when(testUser.getId()).thenReturn(initialUserName);
        when(testUser.arePasswordsEqual(any())).thenReturn(false);
        when(testUser.arePasswordsEqual(eq(passHash))).thenReturn(true);
        when(mockUserDb.get(any())).thenReturn(testUser);
        testApp.login(mockInitialKey, initialUserName, "abc");
    }

    @Test
    void testRegisterWorksCorrectly() throws Exception {
        when(mockUserDb.create(any(), any())).thenReturn(new User("test", "testPassword123"));
        testApp.register(mockSelectionKey, "test", "testPassword123");
        verify(mockUserDb).create(eq("test"), any());
        verify(mockUserDb).save();
    }

    @Test
    void testRegisterThrowsIncorrectPassword() {
        assertThrows(PasswordFormatException.class, () -> testApp.register(mockSelectionKey, "test", "test"));
    }

    @Test
    void testRegisterThrowsUserAddedTwice() throws Exception {
        when(mockUserDb.create(any(), any())).thenReturn(new User("test", "testPassword123"));
        testApp.register(mockSelectionKey, "test", "testPassword123");
        assertThrows(UserInSessionException.class, () -> testApp.register(mockSelectionKey, "test", "testPassword123"));
    }

    @Test
    void testLoginWorksCorrectly() throws Exception {
        String passHash = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";
        when(mockUserDb.get(any())).thenReturn(new User("test", passHash));
        testApp.login(mockSelectionKey, "test", "abc");
        verify(mockUserDb).get(eq("test"));
    }

    @Test
    void testLoginThrowsUserAddedTwice() throws Exception {
        String passHash = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";
        when(mockUserDb.get(any())).thenReturn(new User("test", passHash));
        testApp.login(mockSelectionKey, "test", "abc");
        assertThrows(UserInSessionException.class, () -> testApp.login(mockSelectionKey, "test", "abc"));
    }

    @Test
    void testLoginThrowsWrongPassword() throws Exception {
        when(mockUserDb.get(any())).thenReturn(new User("test", "wrong"));
        assertThrows(InvalidCredentialsException.class, () -> testApp.login(mockSelectionKey, "test", "abc"));
    }

    @Test
    void testLoginThrowsWrongUsername() throws Exception {
        when(mockUserDb.get(any())).thenThrow(UserNotFoundException.class);
        assertThrows(InvalidCredentialsException.class, () -> testApp.login(mockSelectionKey, "test", "abc"));
    }

    @Test
    void testAddTaskWorksCorrectly() throws Exception {
        testApp.addTask(mockInitialKey, "test", testDate, testDate, "test");
        verify(testUser).addTask(eq(testTask));
        verify(mockUserDb).save();
    }

    @Test
    void testAddTaskThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class,
            () -> testApp.addTask(mockSelectionKey, "test", testDate, testDate, "test"));
    }

    @Test
    void testAddTaskThrowsNoTaskName() {
        assertThrows(TaskNameMissingException.class,
            () -> testApp.addTask(mockInitialKey, null, testDate, testDate, "test"));
    }

    @Test
    void testUpdateTaskWorksCorrectly() throws Exception {
        testApp.updateTask(mockInitialKey, "test", testDate, testDate, "test");
        verify(testUser).updateTask(eq(testTask));
        verify(mockUserDb).save();
    }

    @Test
    void testUpdateTaskThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class,
            () -> testApp.updateTask(mockSelectionKey, "test", testDate, testDate, "test"));
    }

    @Test
    void testUpdateTaskThrowsNoTaskName() {
        assertThrows(TaskNameMissingException.class,
            () -> testApp.updateTask(mockInitialKey, null, testDate, testDate, "test"));
    }

    @Test
    void testDeleteTaskWorksCorrectly() throws Exception {
        testApp.deleteTask(mockInitialKey, "test", testDate);
        verify(testUser).deleteTask(eq(testTask));
        verify(mockUserDb).save();
    }

    @Test
    void testDeleteTaskThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.deleteTask(mockSelectionKey, "test", testDate));
    }

    @Test
    void testDeleteTaskThrowsNoTaskName() {
        assertThrows(TaskNameMissingException.class, () -> testApp.deleteTask(mockInitialKey, null, testDate));
    }

    @Test
    void testGetTaskWorksCorrectly() throws Exception {
        when(testUser.getTask(any())).thenReturn("test");
        assertEquals("test", testApp.getTask(mockInitialKey, "test", testDate));
        verify(testUser).getTask(eq(testTask));
    }

    @Test
    void testGetTaskThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.getTask(mockSelectionKey, "test", testDate));
    }

    @Test
    void testGetTaskThrowsNoTaskName() {
        assertThrows(TaskNameMissingException.class, () -> testApp.getTask(mockInitialKey, null, testDate));
    }

    @Test
    void testListTasksWorksCorrectly() throws Exception {
        when(testUser.listTasks(any(), anyBoolean())).thenReturn("test");
        assertEquals("test", testApp.listTasks(mockInitialKey, testDate, true));
        verify(testUser).listTasks(eq(testDate), eq(true));
    }

    @Test
    void testListTasksWorksCorrectlyEmptyResult() throws Exception {
        when(testUser.listTasks(any(), anyBoolean())).thenReturn("");
        assertEquals("No tasks found matching these parameters", testApp.listTasks(mockInitialKey, testDate, true));
        verify(testUser).listTasks(eq(testDate), eq(true));
    }

    @Test
    void testListTasksThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.listTasks(mockSelectionKey, testDate, true));
    }

    @Test
    void testListDashboardWorksCorrectly() throws Exception {
        when(testUser.listDashboard()).thenReturn("test");
        assertEquals("test", testApp.listDashboard(mockInitialKey));
        verify(testUser).listDashboard();
    }

    @Test
    void testListDashboardWorksCorrectlyEmptyResult() throws Exception {
        when(testUser.listDashboard()).thenReturn("");
        assertEquals("No incomplete tasks for today", testApp.listDashboard(mockInitialKey));
        verify(testUser).listDashboard();
    }

    @Test
    void testListDashboardThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.listDashboard(mockSelectionKey));
    }

    @Test
    void testFinishTaskWorksCorrectly() throws Exception {
        testApp.finishTask(mockInitialKey, "test", testDate);
        verify(testUser).finishTask(eq(testTask));
        verify(mockUserDb).save();
    }

    @Test
    void testFinishTaskThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.finishTask(mockSelectionKey, "test", testDate));
    }

    @Test
    void testFinishTaskThrowsNoTaskName() {
        assertThrows(TaskNameMissingException.class, () -> testApp.finishTask(mockInitialKey, null, testDate));
    }

    @Test
    void testAddCollaborationWorksCorrectly() throws Exception {
        testApp.addCollaboration(mockInitialKey, "test");
        verify(testUser).addCollaboration(eq("test"));
        verify(mockCollabDb).create(eq("test"), eq(initialUserName));
        verify(mockUserDb).save();
        verify(mockCollabDb).save();
    }

    @Test
    void testAddCollaborationThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.addCollaboration(mockSelectionKey, "test"));
    }

    @Test
    void testDeleteCollaborationWorksCorrectly() throws Exception {
        when(mockCollabDb.delete(any(), any())).thenReturn(new Collaboration("test", initialUserName));
        testApp.deleteCollaboration(mockInitialKey, "test");
        verify(mockUserDb).removeCollaborations(any(), eq("test"));
        verify(mockCollabDb).delete(eq("test"), eq(initialUserName));
        verify(mockUserDb).save();
        verify(mockCollabDb).save();
    }

    @Test
    void testDeleteCollaborationThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.deleteCollaboration(mockSelectionKey, "test"));
    }

    @Test
    void testListCollaborationsWorksCorrectly() throws Exception {
        when(testUser.listCollaborations()).thenReturn("test");
        assertEquals("test", testApp.listCollaborations(mockInitialKey));
        verify(testUser).listCollaborations();
    }

    @Test
    void testListCollaborationsWorksCorrectlyNoCollaborations() throws Exception {
        when(testUser.listCollaborations()).thenReturn("");
        assertEquals("No collaborations found", testApp.listCollaborations(mockInitialKey));
        verify(testUser).listCollaborations();
    }

    @Test
    void testListCollaborationsThrowsMissingUser() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.listCollaborations(mockSelectionKey));
    }

    @Test
    void testAddUserWorksCorrectly() throws Exception {
        User newUser = new User("testUser", "test");
        Collaboration newCollab = new Collaboration("test", initialUserName);
        when(mockUserDb.get(any())).thenReturn(newUser);
        when(mockCollabDb.get(any())).thenReturn(newCollab);

        testApp.addUser(mockInitialKey, "test", "test");
        assertTrue(newUser.getCollaborations().contains("test"));
        assertTrue(newCollab.getUsers().contains("testUser"));

        verify(mockUserDb).save();
        verify(mockCollabDb).save();
    }

    @Test
    void testAddUserThrowsInsufficientPermissions() throws Exception {
        when(mockCollabDb.get(any())).thenReturn(new Collaboration("test", "wrong"));
        assertThrows(NoCollaborationPermissionsException.class, () -> testApp.addUser(mockInitialKey, "test", "test"));
    }

    @Test
    void testAddUserThrowsUserNotLogged() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.addUser(mockSelectionKey, "test", "test"));
    }

    @Test
    void testAddTaskToCollaborationWorksCorrectly() throws Exception {
        Collaboration newCollab = new Collaboration("test", initialUserName);
        when(mockCollabDb.get(any())).thenReturn(newCollab);

        testApp.addTask(mockInitialKey, "test", initialUserName, "test", testDate, testDate, "test");
        assertEquals(
            "Task: test, Date: " + testDate + ", Due: " + testDate + ", Description: test, Finished: false Assignee:" +
                initialUserName, newCollab.listTasks());

        verify(mockUserDb, times(2)).get(eq(initialUserName));
        verify(mockCollabDb).get(eq("test"));
        verify(mockUserDb).save();
        verify(mockCollabDb).save();
    }

    @Test
    void testAddTaskToCollaborationThrowsInsufficientPermissions() throws Exception {
        when(mockCollabDb.get(any())).thenReturn(new Collaboration("test", "wrong"));
        assertThrows(NoCollaborationPermissionsException.class,
            () -> testApp.addTask(mockInitialKey, "test", "test", "test", null, null, null));
    }

    @Test
    void testAddTaskToCollaborationThrowsUserNotLogged() {
        assertThrows(UserNotLoggedInException.class,
            () -> testApp.addTask(mockSelectionKey, "test", "test", "test", null, null, null));
    }

    @Test
    void testAddTaskToCollaborationThrowsMissingTask() throws Exception {
        when(mockCollabDb.get(any())).thenReturn(new Collaboration("test", initialUserName));
        assertThrows(TaskNameMissingException.class,
            () -> testApp.addTask(mockInitialKey, "test", "test", null, null, null, null));
    }

    @Test
    void testListCollaborationTasksWorksCorrectly() throws Exception {
        Collaboration newCollab = new Collaboration("test", initialUserName);
        newCollab.addTask(testUser, testTask);
        when(mockCollabDb.get(any())).thenReturn(newCollab);

        String result = testApp.listCollaborationTasks(mockInitialKey, "test");
        assertEquals(
            "Task: test, Date: " + testDate + ", Due: " + testDate + ", Description: test, Finished: false Assignee:" +
                initialUserName, result);
        verify(mockCollabDb).get(eq("test"));
    }

    @Test
    void testListCollaborationTasksWorksCorrectlyNoTasks() throws Exception {
        Collaboration newCollab = new Collaboration("test", initialUserName);
        when(mockCollabDb.get(any())).thenReturn(newCollab);

        String result = testApp.listCollaborationTasks(mockInitialKey, "test");
        assertEquals("No collaboration tasks found", result);
        verify(mockCollabDb).get(eq("test"));
    }

    @Test
    void testListCollaborationTasksThrowsInsufficientPermissions() throws Exception {
        when(mockCollabDb.get(any())).thenReturn(new Collaboration("test", "wrong"));
        assertThrows(NoCollaborationPermissionsException.class,
            () -> testApp.listCollaborationTasks(mockInitialKey, "test"));
    }

    @Test
    void testListCollaborationTasksThrowsUserNotLogged() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.listCollaborationTasks(mockSelectionKey, "test"));
    }

    @Test
    void testListUsersWorksCorrectly() throws Exception {
        Collaboration newCollab = new Collaboration("test", initialUserName);
        when(mockCollabDb.get(any())).thenReturn(newCollab);

        String result = testApp.listUsers(mockInitialKey, "test");
        assertEquals(initialUserName, result);
        verify(mockCollabDb).get(eq("test"));
    }

    @Test
    void testListUsersWorksCorrectlyMultipleUsers() throws Exception {
        Collaboration newCollab = new Collaboration("test", initialUserName);
        newCollab.addUser("testUser");
        when(mockCollabDb.get(any())).thenReturn(newCollab);

        String result = testApp.listUsers(mockInitialKey, "test");
        assertTrue(result.contains(initialUserName));
        assertTrue(result.contains("testUser"));
        verify(mockCollabDb).get(eq("test"));
    }

    @Test
    void testListUsersThrowsInsufficientPermissions() throws Exception {
        when(mockCollabDb.get(any())).thenReturn(new Collaboration("test", "wrong"));
        assertThrows(NoCollaborationPermissionsException.class, () -> testApp.listUsers(mockInitialKey, "test"));
    }

    @Test
    void testListUsersThrowsUserNotLogged() {
        assertThrows(UserNotLoggedInException.class, () -> testApp.listUsers(mockSelectionKey, "test"));
    }

    @Test
    void testExitWorksCorrectly() {
        testApp.exit(mockInitialKey);
        verify(mockUserDb).save();
        verify(mockCollabDb).save();
    }

}
