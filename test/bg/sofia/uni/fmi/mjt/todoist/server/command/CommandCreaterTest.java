package bg.sofia.uni.fmi.mjt.todoist.server.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandCreatorTest {

    @Test
    void testNewCommandWithUserRegistration() {
        Command command = CommandCreator.newCommand("register user1 password123");
        assertEquals("register", command.name());
        assertArrayEquals(new String[] {"user1", "password123"}, command.arguments());
    }

    @Test
    void testNewCommandWithLogin() {
        Command command = CommandCreator.newCommand("login user1 password123");
        assertEquals("login", command.name());
        assertArrayEquals(new String[] {"user1", "password123"}, command.arguments());
    }

    @Test
    void testNewCommandWithExit() {
        Command command = CommandCreator.newCommand("exit");
        assertEquals("exit", command.name());
        assertEquals(0, command.arguments().length);
    }

    @Test
    void testNewCommandWithAddTask() {
        Command command = CommandCreator.newCommand(
            "add-task --name=Homework --date=12-02-2025 " + "--due-date=15-02-2025 --description=\"Math homework\"");
        assertEquals("add-task", command.name());
        assertArrayEquals(new String[] {"--name=Homework", "--date=12-02-2025", "--due-date=15-02-2025",
            "--description=Math homework"}, command.arguments());
    }

    @Test
    void testNewCommandWithUpdateTask() {
        Command command = CommandCreator.newCommand("update-task --name=Homework --date=12-02-2025" +
            " --due-date=16-02-2025 --description=\"Updated math homework\"");
        assertEquals("update-task", command.name());
        assertArrayEquals(new String[] {"--name=Homework", "--date=12-02-2025", "--due-date=16-02-2025",
            "--description=Updated math homework"}, command.arguments());
    }

    @Test
    void testNewCommandWithDeleteTask() {
        Command command = CommandCreator.newCommand("delete-task --name=Homework --date=12-02-2025");
        assertEquals("delete-task", command.name());
        assertArrayEquals(new String[] {"--name=Homework", "--date=12-02-2025"}, command.arguments());
    }

    @Test
    void testNewCommandWithListTasksByDate() {
        Command command = CommandCreator.newCommand("list-tasks --date=12-02-2025 --completed");
        assertEquals("list-tasks", command.name());
        assertArrayEquals(new String[] {"--date=12-02-2025", "--completed"}, command.arguments());
    }

    @Test
    void testNewCommandWithListDashboard() {
        Command command = CommandCreator.newCommand("list-dashboard");
        assertEquals("list-dashboard", command.name());
        assertEquals(0, command.arguments().length);
    }

    @Test
    void testNewCommandWithFinishTask() {
        Command command = CommandCreator.newCommand("finish-task --name=Homework --date=12-02-2025");
        assertEquals("finish-task", command.name());
        assertArrayEquals(new String[] {"--name=Homework", "--date=12-02-2025"}, command.arguments());
    }

    @Test
    void testNewCommandWithAddCollaboration() {
        Command command = CommandCreator.newCommand("add-collaboration --name=ProjectX");
        assertEquals("add-collaboration", command.name());
        assertArrayEquals(new String[] {"--name=ProjectX"}, command.arguments());
    }

    @Test
    void testNewCommandWithDeleteCollaboration() {
        Command command = CommandCreator.newCommand("delete-collaboration --name=ProjectX");
        assertEquals("delete-collaboration", command.name());
        assertArrayEquals(new String[] {"--name=ProjectX"}, command.arguments());
    }

    @Test
    void testNewCommandWithListCollaborations() {
        Command command = CommandCreator.newCommand("list-collaborations");
        assertEquals("list-collaborations", command.name());
        assertEquals(0, command.arguments().length);
    }

    @Test
    void testNewCommandWithAddLabel() {
        Command command = CommandCreator.newCommand("add-label --name=Work");
        assertEquals("add-label", command.name());
        assertArrayEquals(new String[] {"--name=Work"}, command.arguments());
    }

    @Test
    void testNewCommandWithDeleteLabel() {
        Command command = CommandCreator.newCommand("delete-label --name=Work");
        assertEquals("delete-label", command.name());
        assertArrayEquals(new String[] {"--name=Work"}, command.arguments());
    }
}