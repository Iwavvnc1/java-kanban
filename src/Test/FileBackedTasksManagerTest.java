package Test;

import Manager.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Test
    public void save() {
        manager.addTask(task1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager, newManager, "Не правильно работает save(),менеджеры не совпадают.");
    }

    @Test
    public void saveEmpty() {
        manager.deleteAllTasks();
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager.getAllTasks().size(), newManager.getAllTasks().size(), "Не правильно работает " +
                "save()," +
                "менеджеры не совпадают с пустым списком.");
    }

    @Test
    public void saveWithEpicWithoutSub() {
        manager.addTask(epic1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager, newManager, "Не правильно работает save(), " +
                "менеджеры не совпадают при неправильном Id.");
    }

    @Test
    public void loadFromFile() {
        manager.addTask(task1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager, newManager, "Не правильно работает loadFromFile(),менеджеры не совпадают.");
    }

    @Test
    public void loadFromFileEmpty() {
        manager.deleteAllTasks();
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager, newManager, "Не правильно работает " +
                "loadFromFile()," +
                "менеджеры не совпадают с пустым списком.");
    }

    @Test
    public void loadFromFileWithEpicWithoutSub() {
        manager.addTask(epic1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager, newManager, "Не правильно работает loadFromFile(), " +
                "менеджеры не совпадают при неправильном Id.");
    }
}