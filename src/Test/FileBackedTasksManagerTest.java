package Test;

import Manager.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    @Test
    public void save() throws IOException, InterruptedException {
        manager.addTask(task1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager,newManager,"Не правильно работает save(),менеджеры не совпадают.");
    }

    @Test
    public void saveEmpty() throws IOException, InterruptedException, InterruptedException {
        manager.deleteAllTasks();
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager.getAllTasks().size(),newManager.getAllTasks().size(),"Не правильно работает " +
                "save()," +
                "менеджеры не совпадают с пустым списком.");
    }

    @Test
    public void saveWithEpicWithoutSub() throws IOException, InterruptedException, InterruptedException {
        manager.addTask(epic1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager,newManager,"Не правильно работает save(), " +
                "менеджеры не совпадают при неправильном Id.");
    }

    @Test
    public void loadFromFile() throws IOException, InterruptedException, InterruptedException {
        manager.addTask(task1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager,newManager,"Не правильно работает loadFromFile(),менеджеры не совпадают.");
    }

    @Test
    public void loadFromFileEmpty() throws IOException, InterruptedException, InterruptedException {
        manager.deleteAllTasks();
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager,newManager,"Не правильно работает " +
                "loadFromFile()," +
                "менеджеры не совпадают с пустым списком.");
    }

    @Test
    public void loadFromFileWithEpicWithoutSub() throws IOException, InterruptedException, InterruptedException {
        manager.addTask(epic1);
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(FileBackedTasksManager.file);
        assertEquals(manager,newManager,"Не правильно работает loadFromFile(), " +
                "менеджеры не совпадают при неправильном Id.");
    }
}