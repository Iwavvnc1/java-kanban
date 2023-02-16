package Test;

import DataTask.*;
import Manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;
    @BeforeEach
    public void beforeEach() {
        task = new Task(0, "name", "description");
    }
    @Test
    void getId() {
        assertEquals(0, task.getId(), "Не верно выводится Id");
    }

    @Test
    void getStatus() {
        assertEquals(Status.NEW, task.getStatus(), "Не верно выводится status");
    }

    @Test
    void getType() {
        assertEquals(TypeTask.TASK, task.getType(), "Не верно выводится TYPE");
    }

    @Test
    void getTitle() {
        assertEquals("name", task.getTitle(), "Не верно выводится title");
    }

    @Test
    void getDescription() {
        assertEquals("description", task.getDescription(), "Не верно выводится description");
    }

    @Test
    void setStatus() {
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus(), "Не верно вводится status");
    }
}