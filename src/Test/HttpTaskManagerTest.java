package Test;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;
import KVServer.KVServer;
import Manager.FileBackedTasksManager;
import Manager.HttpTaskManager;
import TypeAdapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest {

    protected KVServer kvserver;
    protected HttpTaskManager manager;

    protected final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    Task task1 = new Task("task1", "task1Description",
            LocalDateTime.of(2012, 3, 11, 10, 0), 10);
    Task task2 = new Task("task2", "task2Description",
            LocalDateTime.of(2012, 3, 11, 10, 15), 10);
    Epic epic1 = new Epic("epic1", "epic1Description");
    SubTask subtask1 = new SubTask("subtask1", "subtask1Description", epic1.getId(),
            LocalDateTime.of(2012, 3, 11, 10, 30), 10);


    @BeforeEach
    public void beforeEach() throws IOException {
        kvserver = new KVServer();
        kvserver.start();
        manager = new HttpTaskManager(FileBackedTasksManager.file, "DEBUG");
    }

    @AfterEach
    public void afterEach() {
        kvserver.stop();
    }

    @Test
    void save() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(epic1);
        manager.addTask(subtask1);
        manager.getTask(task1.getId());
        manager.getTask(subtask1.getId());
        HttpTaskManager newManager = new HttpTaskManager(FileBackedTasksManager.file, "DEBUG");
        newManager.loadFromServer();
        assertEquals(manager.getAllTasks().size(), newManager.getAllTasks().size(),
                "Не правильно сохраняются все задачи.");
        assertEquals(manager.getTasks().size(), newManager.getTasks().size(),
                "Не правильно сохраняются Task.");
        assertEquals(manager.getSubTasks().size(), newManager.getSubTasks().size(),
                "Не правильно сохраняются subtask.");
        assertEquals(manager.getEpicTasks().size(), newManager.getEpicTasks().size(),
                "Не правильно сохраняются epic.");
        assertEquals(manager.getPrioritizedTasks().size(), newManager.getPrioritizedTasks().size(),
                "Не правильно сохраняются осортированные задачи.");
        assertEquals(manager.getHistory().size(), newManager.getHistory().size(),
                "Не правильно сохраняется история.");
    }

    @Test
    void loadFromServer() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(epic1);
        manager.addTask(subtask1);
        manager.getTask(task1.getId());
        manager.getTask(subtask1.getId());
        HttpTaskManager newManager = new HttpTaskManager(FileBackedTasksManager.file, "DEBUG");
        newManager.loadFromServer();
        assertEquals(manager.getAllTasks().size(), newManager.getAllTasks().size(),
                "Не правильно загружаются все задачи.");
        assertEquals(manager.getTasks().size(), newManager.getTasks().size(),
                "Не правильно загружаются Task.");
        assertEquals(manager.getSubTasks().size(), newManager.getSubTasks().size(),
                "Не правильно загружаются subtask.");
        assertEquals(manager.getEpicTasks().size(), newManager.getEpicTasks().size(),
                "Не правильно загружаются epic.");
        assertEquals(manager.getPrioritizedTasks().size(), newManager.getPrioritizedTasks().size(),
                "Не правильно загружаются осортированные задачи.");
        assertEquals(manager.getHistory().size(), newManager.getHistory().size(),
                "Не правильно загружается история.");
    }
}