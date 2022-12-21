package Manager;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    ArrayList<Integer> allTaskId = new ArrayList<>();
    ArrayList<Integer> allSubTaskIdInEpic = new ArrayList<>();
    HashMap<Integer, Object> tasks = new HashMap<>();
    HashMap<Integer, Object> subTasks = new HashMap<>();
    HashMap<Integer, Object> epicTasks = new HashMap<>();
    public int getId();
    public int addTask(Task task);
    public int addSubTask(SubTask subTask);
    public int addEpicTask(Epic epic);
    public void updateTask(Task task);
    public void updateSubTask(SubTask subTask);
    public void updateEpicTask(Epic epic);
    public void deleteAll();
    public void deleteTask(int id);
    public Object getTask(int id);
    public Object getSubTask(int id);
    public Object getEpicTask(int id);
    public void getAllTask();
    public void getAllSubTaskInEpic();
}