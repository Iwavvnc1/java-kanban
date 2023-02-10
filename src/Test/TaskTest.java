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
        assertEquals(0, task.getId());
    }

    @Test
    void getStatus() {
        Task task = new Task(0, "name", "description");
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    void getType() {
        Task task = new Task(0, "name", "description");
        assertEquals(TypeTask.TASK, task.getType());
    }

    @Test
    void getTitle() {
        Task task = new Task(0, "name", "description");
        assertEquals("name", task.getTitle());
    }

    @Test
    void getDescription() {
        Task task = new Task(0, "name", "description");
        assertEquals("description", task.getDescription());
    }

    @Test
    void setStatus() {
        Task task = new Task(0, "name", "description");
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void testToString() {
        Task task = new Task(0, "name", "description");
        assertEquals("0,,TASK,,name,,NEW,,description,," + "\n", task.toString());
    }
}