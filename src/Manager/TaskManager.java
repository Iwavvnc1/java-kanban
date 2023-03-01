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

    int addTask(Task task) throws IOException, InterruptedException;

    boolean updateTask(Task task) throws IOException, InterruptedException;

    boolean deleteAllTasks() throws IOException, InterruptedException;

    boolean deleteAllSubTasks() throws IOException, InterruptedException;

    boolean deleteAllEpics() throws IOException, InterruptedException;

    boolean deleteTask(int id) throws IOException, InterruptedException;

    Task getTask(int id) throws IOException, InterruptedException;

    Map<Integer, Task> getAllTasks();

    List<Task> getAllSubTaskInEpic(Epic epic);

    Map<Integer, Task> getTasks();

    Map<Integer, Task> getSubTasks();

    Map<Integer, Task> getEpicTasks();

    void updateStatusEpic(int id) throws IOException, InterruptedException;

    TreeSet<Task> getPrioritizedTasks();

    LocalDateTime getTimeNow();

    boolean isHasIntersection(Task task);
}