package Manager;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;

import java.util.Map;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    int giveId();

    int addTask(Task task);

    int addSubTask(SubTask subTask);

    int addEpicTask(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpicTask(Epic epic);

    void deleteAll();

    void deleteTask(int id);

    Task getTask(int id);

    Task getSubTask(int id);

    Task getEpicTask(int id);

    List<Integer> getAllTask();

    List<Integer> getAllSubTaskInEpic(Epic epic);

    List<Integer> getAllTaskId();

    Map<Integer, Task> getTasks();

    Map<Integer, Task> getSubTasks();

    Map<Integer, Task> getEpicTasks();
}