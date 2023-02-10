package Test;

import DataTask.Epic;
import DataTask.Status;
import DataTask.SubTask;
import DataTask.Task;
import Manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void TestMethodAddTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        Task task = new Task(0, "name", "description");

        manager.addTask(epic);
        assertEquals(epic.getId(), manager.getEpicTasks().get(epic.getId()).getId());
        manager.addTask(task);
        assertEquals(task.getId(), manager.getTasks().get(task.getId()).getId());
        manager.addTask(sub);
        assertEquals(sub.getId(), manager.getSubTasks().get(sub.getId()).getId());
    }

    @Test
    void TestMethodUpdateTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        Task task = new Task(0, "name", "description");
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(sub);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, manager.getTasks().get(task.getId()).getStatus());
        sub.setStatus(Status.DONE);
        manager.updateTask(sub);
        assertEquals(Status.DONE, manager.getSubTasks().get(sub.getId()).getStatus());
        epic.setStatus(Status.DONE);
        manager.updateTask(epic);
        assertEquals(Status.DONE, manager.getSubTasks().get(sub.getId()).getStatus());


    }

    @Test
    void TestMethodUpdateStatusEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        manager.addTask(epic);
        manager.addTask(sub);
        sub.setStatus(Status.DONE);
        manager.updateTask(sub);
        manager.updateStatusEpic(sub.getEpicId());
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void TestMethodDeleteAll() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        Task task = new Task(0, "name", "description");
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(sub);
        manager.deleteAll();
        assertEquals(0, manager.getAllTask().size());
        assertEquals(0, manager.getTasks().size());
        assertEquals(0, manager.getSubTasks().size());
        assertEquals(0, manager.getEpicTasks().size());
    }

    @Test
    void TestMethodDeleteTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        Task task = new Task(0, "name", "description");
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(sub);
        manager.deleteTask(task.getId());
        assertEquals(0, manager.getTasks().size());
        manager.deleteTask(sub.getId());
        assertEquals(0, manager.getSubTasks().size());
        manager.deleteTask(epic.getId());
        assertEquals(0, manager.getEpicTasks().size());
        assertEquals(0, manager.getAllTask().size());

    }

    @Test
    void TestMethodGetTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        Task task = new Task(0, "name", "description");
        manager.addTask(epic);
        assertEquals(epic, manager.getTask(epic.getId()));
        manager.addTask(task);
        assertEquals(task, manager.getTask(task.getId()));
        manager.addTask(sub);
        assertEquals(sub, manager.getTask(sub.getId()));
    }

    @Test
    void TestMethodGetHistory() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        Task task = new Task(0, "name", "description");
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(sub);
    }

    @Test
    void TestMethodGetAllTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        Task task = new Task(0, "name", "description");
        manager.addTask(epic);
        manager.addTask(task);
        manager.addTask(sub);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(epic.getId(), epic);
        expected.put(task.getId(), task);
        expected.put(sub.getId(), sub);
        assertEquals(expected, manager.getAllTask());
    }

    @Test
    void TestMethodGetAllSubTaskInEpic() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub = new SubTask(2, "name", "description", 1);
        manager.addTask(epic);
        manager.addTask(sub);
        assertEquals(sub.getId(), epic.getIdSubTasks().get(0));

    }

    @Test
    void TestMethodGetTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task(1, "name", "description");
        Task task2 = new Task(0, "name", "description");
        manager.addTask(task1);
        manager.addTask(task2);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(task1.getId(), task1);
        expected.put(task2.getId(), task2);
        assertEquals(expected, manager.getTasks());

    }

    @Test
    void TestMethodGetSubTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "name", "description");
        SubTask sub1 = new SubTask(2, "name", "description", 1);
        SubTask sub2 = new SubTask(3, "name", "description", 1);
        manager.addTask(epic);
        manager.addTask(sub1);
        manager.addTask(sub2);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(sub1.getId(), sub1);
        expected.put(sub2.getId(), sub2);
        assertEquals(expected, manager.getSubTasks());
    }

    @Test
    void TestMethodGetEpicTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic1 = new Epic(1, "name", "description");
        Epic epic2 = new Epic(2, "name", "description");
        manager.addTask(epic1);
        manager.addTask(epic2);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(epic1.getId(), epic1);
        expected.put(epic2.getId(), epic2);
        assertEquals(expected, manager.getEpicTasks());
    }
}