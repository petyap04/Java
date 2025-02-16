package bg.sofia.uni.fmi.mjt.todoist.server.task;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskAlreadyFinishedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {
    private Task task;
    private final String taskName = "Test Task";
    private final LocalDate taskDate = LocalDate.now();
    private final LocalDate dueDate = LocalDate.now().plusDays(5);
    private final String description = "This is a test task.";

    @BeforeEach
    void setUp() {
        task = new Task(taskName, taskDate, dueDate, description);
    }

    @Test
    void testTaskInitialization() {
        assertEquals(taskName, task.getName());
        assertEquals(taskDate, task.getDate());
        assertEquals(dueDate, task.getDueDate());
        assertEquals(description, task.getDescription());
        assertFalse(task.isFinished());
    }

    @Test
    void testMarkAsFinishedSuccessfully() throws TaskAlreadyFinishedException {
        task.markAsFinished();
        assertTrue(task.isFinished());
    }

    @Test
    void testMarkAsFinishedTwiceThrowsException() throws TaskAlreadyFinishedException {
        task.markAsFinished();
        assertThrows(TaskAlreadyFinishedException.class, task::markAsFinished);
    }

    @Test
    void testHasEqualDateWithSameDate() {
        assertTrue(task.hasEqualDate(taskDate));
    }

    @Test
    void testHasEqualDateWithDifferentDate() {
        assertFalse(task.hasEqualDate(taskDate.plusDays(1)));
    }

    @Test
    void testHasEqualDateWithNullDate() {
        Task taskWithNullDate = new Task(taskName, null, dueDate, description);
        assertTrue(taskWithNullDate.hasEqualDate(null));
    }

    @Test
    void testEqualsWithSameTask() {
        Task sameTask = new Task(taskName, taskDate, dueDate, description);
        assertEquals(task, sameTask);
    }

    @Test
    void testEqualsWithDifferentTask() {
        Task differentTask = new Task("Another Task", taskDate, dueDate, description);
        assertNotEquals(task, differentTask);
    }

    @Test
    void testEqualsWithDifferentDate() {
        Task differentDateTask = new Task(taskName, taskDate.plusDays(1), dueDate, description);
        assertNotEquals(task, differentDateTask);
    }

    @Test
    void testHashCodeConsistency() {
        Task sameTask = new Task(taskName, taskDate, dueDate, description);
        assertEquals(task.hashCode(), sameTask.hashCode());
    }

    @Test
    void testToStringOutput() {
        String expectedOutput =
            "Task: " + taskName + ", Date: " + taskDate.toString() + ", Due: " + dueDate.toString() +
                ", Description: " + description + ", Finished: false";
        assertEquals(expectedOutput, task.toString());
    }
}
