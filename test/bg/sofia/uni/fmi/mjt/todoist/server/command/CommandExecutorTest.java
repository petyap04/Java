package bg.sofia.uni.fmi.mjt.todoist.server.command;

import bg.sofia.uni.fmi.mjt.todoist.server.application.Application;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CommandExecutorTest {
    private CommandExecutor executor;
    private Application application;
    private SelectionKey key;

    @BeforeEach
    void setUp() {
        application = mock(Application.class);
        key = mock(SelectionKey.class);
        executor = new CommandExecutor(application);
    }

    @Test
    void testExecuteRegisterCommand() throws Exception {
        Command command = new Command("register", new String[] {"user1", "password123"});
        String result = executor.execute(key, command);
        assertEquals("User user1 registered successfully", result);
        verify(application).register(key, "user1", "password123");
    }

    @Test
    void testExecuteLoginCommand() throws Exception {
        Command command = new Command("login", new String[] {"user1", "password123"});
        String result = executor.execute(key, command);
        assertEquals("User user1 logged in successfully", result);
        verify(application).login(key, "user1", "password123");
    }

    @Test
    void testExecuteExitCommand() {
        Command command = new Command("exit", new String[] {});
        String result = executor.execute(key, command);
        assertEquals("exit", result);
        verify(application).exit(key);
    }

    @Test
    void testExecuteUnknownCommand() {
        Command command = new Command("unknown-command", new String[] {});
        String result = executor.execute(key, command);
        assertEquals("Unknown command", result);
    }

    @Test
    void testExecuteCommandThrowsTodoistException() throws Exception {
        Command command = new Command("register", new String[] {"user1", "password123"});
        doThrow(new UserExistsException()).when(application).register(key, "user1", "password123");

        String result = executor.execute(key, command);
        assertTrue(result.contains("User with name already exists!"));
    }

    @Test
    void testExecuteCommandThrowsGenericException() throws Exception {
        Command command = new Command("register", new String[] {"user1", "password123"});
        doThrow(new RuntimeException("Unexpected error")).when(application).register(key, "user1", "password123");

        String result = executor.execute(key, command);
        assertTrue(result.contains("ERROR: An error has occurred during the processing of the request"));
    }
}
