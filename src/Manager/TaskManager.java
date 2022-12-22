package Manager;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;

public interface TaskManager {


    int addTask(Task task);
    int addSubTask(SubTask subTask);
    int addEpicTask(Epic epic);
    void updateTask(Task task);
    void updateSubTask(SubTask subTask);
    void updateEpicTask(Epic epic);
    void deleteAll();
    void deleteTask(int id);
    Object getTask(int id);
    Object getSubTask(int id);
    Object getEpicTask(int id);
    void getAllTask();
    void getAllSubTaskInEpic();
}