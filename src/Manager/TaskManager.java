package Manager;

import DataTask.Epic;
import DataTask.Task;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    List<Task> getHistory();

    int addTask(Task task);

    boolean updateTask(Task task);

    boolean deleteAllTasks();

    boolean deleteAllSubTasks();

    boolean deleteAllEpics();

    boolean deleteTask(int id);

    Task getTask(int id);

    Map<Integer, Task> getAllTasks();

    List<Task> getAllSubTaskInEpic(Epic epic);

    Map<Integer, Task> getTasks();

    Map<Integer, Task> getSubTasks();

    Map<Integer, Task> getEpicTasks();

    void updateStatusEpic(int id);

    TreeSet<Task> getPrioritizedTasks();

    LocalDateTime getTimeNow();

    boolean isHasIntersection(Task task);
}