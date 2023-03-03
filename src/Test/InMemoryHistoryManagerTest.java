package Test;

import DataTask.Task;
import Manager.InMemoryHistoryManager;
import Manager.InMemoryTaskManager;
import MyException.IdRepitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryTaskManager manager;
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("name", "description");
        task2 = new Task("name", "description");
        task3 = new Task("name", "description");
    }

    @Test
    void add()  {
        manager.addTask(task1);
        manager.getTask(task1.getId());
        historyManager.add(task1);
        assertEquals(historyManager.getHistory(),manager.getHistoryManager().getHistory(),
                "Не правильно сохраняются данные в истории.");
    }

    @Test
    void addDouble()  {
        manager.addTask(task1);
        manager.getTask(task1.getId());
        manager.getTask(task1.getId());
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(historyManager.getHistory(),manager.getHistoryManager().getHistory(),
                "Не правильно сохраняются данные в истории при дублировании.");
    }

    @Test
    void getHistory()  {
        manager.addTask(task1);
        manager.getTask(task1.getId());
        historyManager.add(task1);
        assertEquals(historyManager.getHistory(),manager.getHistoryManager().getHistory(),
                "Не правильно выводятся данные из истории.");
    }

    @Test
    void getHistoryDouble()  {
        try {
            manager.addTask(task1);
            manager.addTask(task1);
            manager.getTask(task1.getId());
            historyManager.add(task1);
            historyManager.add(task1);
        } catch (IdRepitException e) {
            assertNotEquals("", e.getMessage());
        }
    }

    @Test
    void removeBeginning()  {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        manager.deleteTask(task1.getId());
        historyManager.remove(task1.getId());
        assertEquals(historyManager.getHistory(),manager.getHistory(),
                "Не правильно удаляется история с начала списка");
    }

    @Test
    void removeMiddle()  {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        manager.deleteTask(task2.getId());
        assertEquals(historyManager.getHistory(),manager.getHistory(),
                "Не правильно удаляется история с середины списка");
    }

    @Test
    void removeEnd()  {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        manager.deleteTask(task3.getId());
        historyManager.remove(task3.getId());
        assertEquals(historyManager.getHistory(),manager.getHistory(),
                "Не правильно удаляется история с середины списка");
    }
}