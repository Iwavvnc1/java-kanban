package Manager;

import DataTask.Epic;
import DataTask.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    List<Task> getHistory();

    void addTask(Task task) throws IOException;

    void updateTask(Task task);

    void deleteAll();

    void deleteTask(int id);

    Task getTask(int id);

    Map<Integer, Task> getAllTasks();

    List<Task> getAllSubTaskInEpic(Epic epic);

    Map<Integer, Task> getTasks();

    Map<Integer, Task> getSubTasks();

    Map<Integer, Task> getEpicTasks();

    public void updateStatusEpic(int id);

    public TreeSet<Task> getPrioritizedTasks();
    public LocalDateTime getTimeNow();
    public boolean checkTime(Task task);
}