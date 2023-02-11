package Manager;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;

import java.io.IOException;
import java.util.Map;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    void addTask(Task task) throws IOException;

    void updateTask(Task task);

    void deleteAll();

    void deleteTask(int id);

    Task getTask(int id);

    Map<Integer, Task> getAllTasks();

    List<Integer> getAllSubTaskInEpic(Epic epic);

    Map<Integer, Task> getTasks();

    Map<Integer, Task> getSubTasks();

    Map<Integer, Task> getEpicTasks();

    public void updateStatusEpic(int id);
}