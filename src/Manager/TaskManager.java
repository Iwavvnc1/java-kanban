package Manager;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;

public interface TaskManager {

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

    void getAllTask();

    void getAllSubTaskInEpic();
}