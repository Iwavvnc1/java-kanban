package Test;

import DataTask.Epic;
import DataTask.Status;
import DataTask.SubTask;
import DataTask.Task;
import Manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask sub1;
    SubTask sub2;
    SubTask sub3;
    SubTask sub4;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        task1 = new Task(1, "name", "description");
        task2 = new Task(2, "name", "description");
        epic1 = new Epic(3, "name", "description");
        epic2 = new Epic(4, "name", "description");
        sub1 = new SubTask(5, "name", "description", 3);
        sub2 = new SubTask(6, "name", "description", 3);
        sub3 = new SubTask(7, "name", "description", 3);
        sub4 = new SubTask(8, "name", "description", 3);
    }

    @Test
    void TestMethodAddTaskEpic() {
        manager.addTask(epic1);
        Task savedEpic = manager.getTask(epic1.getId());
        assertNotNull(savedEpic, "Epic не найден.");
        assertEquals(epic1.getId(), manager.getEpicTasks().get(epic1.getId()).getId(), "Epic не совпададают.");
        final Map<Integer, Task> epics = manager.getEpicTasks();
        assertNotNull(epics, "Epic не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Epic.");
        assertEquals(epic1, epics.get(epic1.getId()), "Epic не совпадают.");
    }

    @Test
    void TestMethodAddTaskTask() {
        manager.addTask(task1);
        Task savedTask = manager.getTask(task1.getId());
        assertNotNull(savedTask, "Task не найден.");
        assertEquals(task1.getId(), manager.getTasks().get(task1.getId()).getId(), "Task не совпададают.");
        final Map<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Task не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество Task.");
        assertEquals(task1, tasks.get(task1.getId()), "Task не совпадают.");
    }

    @Test
    void TestMethodAddTaskSub() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        Task savedSub1 = manager.getTask(sub1.getId());
        assertNotNull(savedSub1, "Sub не найден.");
        assertEquals(sub1.getId(), manager.getSubTasks().get(sub1.getId()).getId(), "Sub не совпададают.");
        final Map<Integer, Task> subs = manager.getSubTasks();
        assertNotNull(subs, "Sub не возвращаются.");
        assertEquals(1, subs.size(), "Неверное количество Sub.");
        assertEquals(sub1, subs.get(sub1.getId()), "Sub не совпадают.");

    }

    @Test
    void TestMethodUpdateTaskTask() {
        manager.addTask(task1);
        task1.setStatus(Status.DONE);
        manager.updateTask(task1);
        assertEquals(Status.DONE, manager.getTasks().get(task1.getId()).getStatus(), "Task не обновляется");
    }

    @Test
    void TestMethodUpdateTaskTaskEmpty() {
        task1.setStatus(Status.DONE);
        manager.updateTask(task1);
        assertEquals(Status.DONE, manager.getTasks().get(task1.getId()).getStatus(), "Task не обновляется " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodUpdateTaskSub() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        sub1.setStatus(Status.DONE);
        manager.updateTask(sub1);
        assertEquals(Status.DONE, manager.getSubTasks().get(sub1.getId()).getStatus(), "Sub не обновляется");
    }

    @Test
    void TestMethodUpdateTaskSubEmpty() {
        sub1.setStatus(Status.DONE);
        manager.updateTask(sub1);
        assertEquals(Status.DONE, manager.getSubTasks().get(sub1.getId()).getStatus(), "Sub не обновляется " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodUpdateTaskEpic() {
        manager.addTask(epic1);
        epic1.setStatus(Status.DONE);
        manager.updateTask(epic1);
        assertEquals(Status.DONE, manager.getEpicTasks().get(epic1.getId()).getStatus(), "Epic не обновляется");
    }

    @Test
    void TestMethodUpdateTaskEpicEmpty() {
        epic1.setStatus(Status.DONE);
        manager.updateTask(epic1);
        assertEquals(Status.DONE, manager.getEpicTasks().get(epic1.getId()).getStatus(), "Epic не " +
                "обновляется при пустом списке задач");
    }

    @Test
    void TestMethodUpdateStatusEpicWithoutSub() {
        manager.addTask(epic1);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.NEW, epic1.getStatus(), "неправильный расчет статуса без сабтасков");
    }

    @Test
    void TestMethodUpdateStatusEpicWithoutSubEmpty() {
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.NEW, epic1.getStatus(), "неправильный расчет статуса без сабтасков " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodUpdateStatusEpicWithNEWSub() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        manager.addTask(sub2);
        manager.addTask(sub3);
        manager.addTask(sub4);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.NEW, epic1.getStatus(), "Нерпавильный расчет статуса при статусе сабтасков NEW");
    }

    @Test
    void TestMethodUpdateStatusEpicWithNEWSubEmpty() {
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.NEW, epic1.getStatus(), "Нерпавильный расчет статуса при статусе сабтасков NEW " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodUpdateStatusEpicWithDONESub() {
        manager.addTask(epic1);
        sub1.setStatus(Status.DONE);
        sub2.setStatus(Status.DONE);
        sub3.setStatus(Status.DONE);
        sub4.setStatus(Status.DONE);
        manager.addTask(sub1);
        manager.addTask(sub2);
        manager.addTask(sub3);
        manager.addTask(sub4);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.DONE, epic1.getStatus(), "Неправильный расчет статуса при статусе сабтасков DONE");
    }

    @Test
    void TestMethodUpdateStatusEpicWithDONESubEmpty() {
        sub1.setStatus(Status.DONE);
        sub2.setStatus(Status.DONE);
        sub3.setStatus(Status.DONE);
        sub4.setStatus(Status.DONE);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.NEW, epic1.getStatus(), "Не правильный расчет статуса при статусе сабтасков DONE " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodUpdateStatusEpicWithNEWAndDONESub() {
        manager.addTask(epic1);
        sub1.setStatus(Status.NEW);
        sub2.setStatus(Status.DONE);
        sub3.setStatus(Status.NEW);
        sub4.setStatus(Status.DONE);
        manager.addTask(sub1);
        manager.addTask(sub2);
        manager.addTask(sub3);
        manager.addTask(sub4);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Нерпавильный расчет статуса при статусе " +
                "сабтасков NEW и DONE");
    }
    @Test
    void TestMethodUpdateStatusEpicWithNEWAndDONESubEmpty() {
        sub1.setStatus(Status.NEW);
        sub2.setStatus(Status.DONE);
        sub3.setStatus(Status.NEW);
        sub4.setStatus(Status.DONE);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.NEW, epic1.getStatus(), "Нерпавильный расчет статуса при статусе " +
                "сабтасков NEW и DONE при пустом списке задач");
    }

    @Test
    void TestMethodUpdateStatusEpicWithIN_PROGRESSSub() {
        manager.addTask(epic1);
        sub1.setStatus(Status.IN_PROGRESS);
        sub2.setStatus(Status.IN_PROGRESS);
        sub3.setStatus(Status.IN_PROGRESS);
        sub4.setStatus(Status.IN_PROGRESS);
        manager.addTask(sub1);
        manager.addTask(sub2);
        manager.addTask(sub3);
        manager.addTask(sub4);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Нерпавильный расчет статуса при статусе сабтасков" +
                " IN_PROGRESS");
    }
    @Test
    void TestMethodUpdateStatusEpicWithIN_PROGRESSSubEmpty() {
        sub1.setStatus(Status.IN_PROGRESS);
        sub2.setStatus(Status.IN_PROGRESS);
        sub3.setStatus(Status.IN_PROGRESS);
        sub4.setStatus(Status.IN_PROGRESS);
        manager.updateStatusEpic(epic1.getId());
        assertEquals(Status.NEW, epic1.getStatus(), "Нерпавильный расчет статуса при статусе сабтасков" +
                " IN_PROGRESS при пустом списке задач");
    }

    @Test
    void TestMethodDeleteAll() {
        manager.addTask(epic1);
        manager.addTask(task1);
        manager.addTask(sub1);
        manager.deleteAll();
        assertEquals(0, manager.getAllTasks().size(), "AllTask не очищен.");
        assertEquals(0, manager.getTasks().size(), "tasks не очищен.");
        assertEquals(0, manager.getSubTasks().size(), "subTasks не очищен.");
        assertEquals(0, manager.getEpicTasks().size(), "epicTasks не очищен.");
    }
    @Test
    void TestMethodDeleteAllEmpty() {
        manager.deleteAll();
        assertEquals(0, manager.getAllTasks().size(), "AllTask не очищен при пустом списке задач.");
        assertEquals(0, manager.getTasks().size(), "tasks не очищен при пустом списке задач.");
        assertEquals(0, manager.getSubTasks().size(), "subTasks не очищен при пустом списке задач.");
        assertEquals(0, manager.getEpicTasks().size(), "epicTasks не очищен при пустом списке задач.");
    }

    @Test
    void TestMethodDeleteTaskEpic() {
        manager.addTask(epic1);
        manager.deleteTask(epic1.getId());
        assertEquals(0, manager.getEpicTasks().size(), "Epic не удаляется из epicTasks");
        assertEquals(0, manager.getAllTasks().size(), "Epic не удаляется из allTasks");
    }
    @Test
    void TestMethodDeleteTaskEpicEmpty() {
        manager.addTask(epic1);
        manager.deleteTask(epic1.getId());
        assertEquals(0, manager.getEpicTasks().size(), "Epic не удаляется из epicTasks " +
                "при пустом списке задач");
        assertEquals(0, manager.getAllTasks().size(), "Epic не удаляется из allTasks " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodDeleteTaskTask() {
        manager.addTask(task1);
        manager.deleteTask(task1.getId());
        assertEquals(0, manager.getTasks().size(), "Task не удаляется из Tasks");
        assertEquals(0, manager.getAllTasks().size(), "Task не удаляется из allTasks");
    }
    @Test
    void TestMethodDeleteTaskTaskEmpty() {
        manager.deleteTask(task1.getId());
        assertEquals(0, manager.getTasks().size(), "Task не удаляется из Tasks " +
                "при пустом списке задач");
        assertEquals(0, manager.getAllTasks().size(), "Task не удаляется из allTasks " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodDeleteTaskSub() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        manager.deleteTask(sub1.getId());
        assertEquals(0, manager.getSubTasks().size(), "Sub не удаляется из subTasks");
        assertEquals(1, manager.getAllTasks().size(), "Sub не удаляется из allTasks");
    }

    @Test
    void TestMethodDeleteTaskSubEmpty() {
        manager.deleteTask(sub1.getId());
        assertEquals(0, manager.getSubTasks().size(), "Sub не удаляется из subTasks " +
                "при пустом списке задач");
        assertEquals(0, manager.getAllTasks().size(), "Sub не удаляется из allTasks " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodGetTaskEpic() {
        manager.addTask(epic1);
        assertEquals(epic1, manager.getTask(epic1.getId()), "Не правильно работает метод getTask() с Epic");
    }

    @Test
    void TestMethodGetTaskEpicEmpty() {
        assertNull(manager.getTask(epic1.getId()), "Не правильно работает метод getTask() с Epic " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodGetTaskTask() {
        manager.addTask(task1);
        assertEquals(task1, manager.getTask(task1.getId()), "Не правильно работает метод getTask() с Task");
    }

    @Test
    void TestMethodGetTaskTaskEmpty() {
        assertNull( manager.getTask(task1.getId()), "Не правильно работает метод getTask() с Task " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodGetTaskSub() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        assertEquals(sub1, manager.getTask(sub1.getId()), "Не правильно работает метод getTask() с Sub");
    }

    @Test
    void TestMethodGetTaskSubEmpty() {
        assertNull(manager.getTask(sub1.getId()), "Не правильно работает метод getTask() с Sub " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodGetHistory() {
        manager.addTask(epic1);
        manager.addTask(task1);
        manager.addTask(sub1);
        List<Task> history = new ArrayList<>();
        history.add(manager.getTask(epic1.getId()));
        history.add(manager.getTask(task1.getId()));
        history.add(manager.getTask(sub1.getId()));
        assertEquals(history, manager.getHistory(), "Не правильно сохраняется история.");
    }

    @Test
    void TestMethodGetHistoryEmpty() {
        List<Task> history = new ArrayList<>();
        assertEquals(history, manager.getHistory(), "Не правильно сохраняется история " +
                "при пустом списке задач.");
    }

    @Test
    void TestMethodGetAllTask() {
        manager.addTask(epic1);
        manager.addTask(task1);
        manager.addTask(sub1);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(epic1.getId(), epic1);
        expected.put(task1.getId(), task1);
        expected.put(sub1.getId(), sub1);
        assertEquals(expected, manager.getAllTasks(), "Не верно выводятся задачи из allTasks");
    }

    @Test
    void TestMethodGetAllTaskEmpty() {
        Map<Integer, Task> expected = new HashMap<>();
        assertEquals(expected, manager.getAllTasks(), "Не верно выводятся задачи из allTasks " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodGetAllSubTaskInEpicWithoutSub() {
        manager.addTask(epic1);
        List<Integer> expectedIdSubTasks = new ArrayList<>();
        assertEquals(expectedIdSubTasks, epic1.getIdSubTasks(), "Не верно заполняется subTaskInEpic");

    }

    @Test
    void TestMethodGetAllSubTaskInEpicWithSub() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        List<Integer> expectedIdSubTasks = new ArrayList<>();
        expectedIdSubTasks.add(sub1.getId());
        assertEquals(expectedIdSubTasks, epic1.getIdSubTasks(), "Не верно заполняется subTaskInEpic");
    }

    @Test
    void TestMethodGetAllSubTaskInEpicWithSubWithUpdate() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        manager.addTask(sub2);
        List<Integer> expectedIdSubTasks = new ArrayList<>();
        expectedIdSubTasks.add(sub1.getId());
        manager.deleteTask(sub2.getId());
        assertEquals(expectedIdSubTasks, epic1.getIdSubTasks(), "Не верно заполняется subTaskInEpic");
    }

    @Test
    void TestMethodGetAllSubTaskInEpicWithoutSubEmpty() {
        List<Integer> expectedIdSubTasks = new ArrayList<>();
        assertEquals(expectedIdSubTasks, epic1.getIdSubTasks(), "Не верно заполняется " +
                "subTaskInEpic при пустом списке задач");

    }

    @Test
    void TestMethodGetTasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(task1.getId(), task1);
        expected.put(task2.getId(), task2);
        assertEquals(expected, manager.getTasks(), "Не верно выводится tasks");
    }

    @Test
    void TestMethodGetTasksEmpty() {
        Map<Integer, Task> expected = new HashMap<>();
        assertEquals(expected, manager.getTasks(), "Не верно выводится tasks при пустом списке задач");
    }

    @Test
    void TestMethodGetSubTasks() {
        manager.addTask(epic1);
        manager.addTask(sub1);
        manager.addTask(sub2);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(sub1.getId(), sub1);
        expected.put(sub2.getId(), sub2);
        assertEquals(expected, manager.getSubTasks(), "Не верно выводится subTasks");
    }

    @Test
    void TestMethodGetSubTasksEmpty() {
        Map<Integer, Task> expected = new HashMap<>();
        assertEquals(expected, manager.getSubTasks(), "Не верно выводится subTasks " +
                "при пустом списке задач");
    }

    @Test
    void TestMethodGetEpicTasks() {
        manager.addTask(epic1);
        manager.addTask(epic2);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(epic1.getId(), epic1);
        expected.put(epic2.getId(), epic2);
        assertEquals(expected, manager.getEpicTasks(), "Не верно выводится epicTasks");
    }

    @Test
    void TestMethodGetEpicTasksEmpty() {
        Map<Integer, Task> expected = new HashMap<>();
        assertEquals(expected, manager.getEpicTasks(), "Не верно выводится epicTasks " +
                "при пустом списке задач");
    }
}