package Test;

import DataTask.Status;
import DataTask.Task;
import DataTask.TypeTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void getId() {
        Task task = new Task(0, "name", "description");
        assertEquals(0, task.getId(), "Не верно выводится Id");
    }

    @Test
    void getStatus() {
        Task task = new Task(0, "name", "description");
        assertEquals(Status.NEW, task.getStatus(), "Не верно выводится status");
    }

    @Test
    void getType() {
        Task task = new Task(0, "name", "description");
        assertEquals(TypeTask.TASK, task.getType(), "Не верно выводится TYPE");
    }

    @Test
    void getTitle() {
        Task task = new Task(0, "name", "description");
        assertEquals("name", task.getTitle(), "Не верно выводится title");
    }

    @Test
    void getDescription() {
        Task task = new Task(0, "name", "description");
        assertEquals("description", task.getDescription(), "Не верно выводится description");
    }

    @Test
    void setStatus() {
        Task task = new Task(0, "name", "description");
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus(), "Не верно вводится status");
    }
}