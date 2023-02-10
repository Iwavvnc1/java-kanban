package Manager;

import DataTask.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    static protected int id = 0;
    protected int epicId;
    protected final Map<Integer, Task> allTask = new HashMap<>();
    protected List<Integer> allSubTaskIdInEpic = new ArrayList<>();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Task> subTasks = new HashMap<>();
    protected final Map<Integer, Task> epicTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public static int giveId() {
        id++;
        return id;
    }

    @Override
    public void addTask(Task task) {
        allTask.put(task.getId(), task);
        switch (task.getType()) {
            case TASK: {
                tasks.put(task.getId(), task);
                break;
            }
            case EPIC: {
                epicId = id;
                epicTasks.put(task.getId(), task);
                break;
            }
            case SUBTASK: {
                subTasks.put(task.getId(), task);
                Epic updateTask = (Epic) epicTasks.get(task.getEpicId());
                updateTask.addSubTasksOnEpic(task.getId());
                epicTasks.put(updateTask.getId(), updateTask);
                allTask.put(updateTask.getId(), updateTask);
                break;
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        switch (task.getType()) {
            case TASK: {
                tasks.put(task.getId(), task);
                break;
            }
            case EPIC: {
                epicTasks.put(task.getId(), task);
                break;
            }
            case SUBTASK: {
                updateStatusEpic(task.getEpicId());
                subTasks.put(task.getId(), task);
                break;
            }
        }
    }

    public void updateStatusEpic(int id) {
        int countSubTaskDONE = 0;
        Epic epicTask = (Epic) epicTasks.get(id);
        for (int idSub : epicTask.getIdSubTasks()) {
            switch (subTasks.get(idSub).getStatus()) {
                case IN_PROGRESS: {
                    epicTask.setStatus(Status.IN_PROGRESS);
                    break;
                }
                case DONE: {
                    countSubTaskDONE++;
                    epicTask.setStatus(Status.IN_PROGRESS);
                    break;
                }
            }
            if (countSubTaskDONE == epicTask.getIdSubTasks().size()) {
                epicTask.setStatus(Status.DONE);
            }
        }
        updateTask(epicTask);
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
        allTask.clear();
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        switch (allTask.get(id).getType()) {
            case TASK: {
                tasks.remove(id);
                break;
            }
            case EPIC: {
                for (Integer idSub : getAllSubTaskInEpic((Epic) epicTasks.get(id))) {
                    subTasks.remove(idSub);
                    historyManager.remove(idSub);
                }
                epicTasks.remove(id);
                break;
            }
            case SUBTASK: {
                Epic epicTask = (Epic) epicTasks.get(subTasks.get(id).getEpicId());
                epicTask.removeTasksOnEpic(id);
                subTasks.remove(id);
                break;
            }
        }
        allTask.remove(id);
    }

    @Override
    public Task getTask(int id) {
        switch (allTask.get(id).getType()) {
            case TASK: {
                historyManager.add(tasks.get(id));
                return tasks.get(id);
            }
            case EPIC: {
                historyManager.add(epicTasks.get(id));
                return epicTasks.get(id);
            }
            case SUBTASK: {
                historyManager.add(subTasks.get(id));
                return subTasks.get(id);
            }
            default:
                return null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Map<Integer, Task> getAllTask() {
        return allTask;
    }

    @Override
    public List<Integer> getAllSubTaskInEpic(Epic epic) {
        allSubTaskIdInEpic.clear();
        allSubTaskIdInEpic = epic.getIdSubTasks();
        return allSubTaskIdInEpic;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Task> getSubTasks() {
        return subTasks;
    }

    public Map<Integer, Task> getEpicTasks() {
        return epicTasks;
    }
}
