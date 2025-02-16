package bg.sofia.uni.fmi.mjt.todoist.server.task;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.taskexceptions.TaskAlreadyFinishedException;

import java.util.Objects;
import java.time.LocalDate;
import java.io.Serializable;

public class Task implements Serializable {
    private final String name;
    private final LocalDate date;
    private final LocalDate dueDate;
    private final String description;
    private boolean isFinished;

    public Task(String name, LocalDate date, LocalDate dueDate, String description) {
        if (name == null) {
            throw new IllegalArgumentException("The name of the task can not be null!");
        }

        this.name = name;
        this.date = date;
        this.dueDate = dueDate;
        this.description = description;

        this.isFinished = false;
    }

    public void markAsFinished() throws TaskAlreadyFinishedException {
        if (this.isFinished) {
            throw new TaskAlreadyFinishedException();
        }

        this.isFinished = true;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public boolean hasEqualDate(LocalDate targetDate) {
        if (date == null && targetDate == null) {
            return true;
        } else if ((date != null && targetDate != null)) {
            return Objects.equals(date, targetDate);
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;

        if (date == null && task.date == null) {
            return Objects.equals(name, task.name);
        } else if ((date != null && task.date != null)) {
            return Objects.equals(name, task.name) && Objects.equals(date, task.date);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date);
    }

    @Override
    public String toString() {
        return "Task: " + this.name + ", Date: " + getStringData(this.date) + ", Due: " + getStringData(this.dueDate) +
            ", Description: " + getStringData(this.description) + ", Finished: " + this.isFinished;
    }

    private <T> String getStringData(T field) {
        if (field == null) {
            return "???";
        }

        return field.toString();
    }
}
