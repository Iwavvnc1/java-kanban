package Manager;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;

import java.util.ArrayList;
import java.util.HashMap;
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

    ArrayList<Integer> getAllTask();

    ArrayList<Integer> getAllSubTaskInEpic(Epic epic);

    ArrayList<Integer> getAllTaskId();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Task> getSubTasks();

    HashMap<Integer, Task> getEpicTasks();
}