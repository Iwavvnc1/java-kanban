package Manager;

import DataTask.Epic;
import DataTask.Status;
import DataTask.SubTask;
import DataTask.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    static private int id = 0;
    private int epicId;
    private Task searchTask;
    private final List<Integer> allTaskId = new ArrayList<>();
    private final List<Integer> allSubTaskIdInEpic = new ArrayList<>();
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Task> subTasks = new HashMap<>();
    private final Map<Integer, Task> epicTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public int giveId() {
        id++;
        return id;
    }

    @Override
    public int addTask(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        for (int idEpicTask : epicTasks.keySet()) {
            if (subTask.getEpicId() == idEpicTask) {
                Epic updateTask = (Epic) epicTasks.get(idEpicTask);
                updateTask.addSubTasksOnEpic(subTask.getId());
                epicTasks.put(idEpicTask, updateTask);
            }
        }
        return subTask.getId();
    }

    @Override
    public int addEpicTask(Epic epic) {
        epicId = id;
        epicTasks.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) { // Блок методов обновления задач
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        updateStatusEpic(subTask.getEpicId());
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void updateEpicTask(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    private void updateStatusEpic(int id) {
        int countSubTaskDONE = 0;
        Epic epicTask = (Epic) epicTasks.get(id);
        for (int idSub : epicTask.getIdSubTasks()) {
            for (int idSubTasks : subTasks.keySet()) {
                if (idSubTasks == idSub) {
                    SubTask subTask = (SubTask) subTasks.get(idSubTasks);
                    if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                        epicTask.setStatus(Status.IN_PROGRESS);
                        break;
                    }
                    if (subTask.getStatus().equals(Status.DONE)) {
                        countSubTaskDONE++;
                        epicTask.setStatus(Status.IN_PROGRESS);
                    }
                    if (countSubTaskDONE == epicTask.getIdSubTasks().size()) {
                        epicTask.setStatus(Status.DONE);
                    }
                }
            }
        }
        updateEpicTask(epicTask);
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        for (int idTask : tasks.keySet()) {
            if (id == idTask) {
                historyManager.remove(id);
                tasks.remove(idTask);
                return;
            }
        }
        for (int idSubTask : subTasks.keySet()) {
            if (id == idSubTask) {
                SubTask subTask = (SubTask) subTasks.get(idSubTask);
                Epic epicTask = (Epic) epicTasks.get(subTask.getEpicId());
                historyManager.remove(id);
                epicTask.removeTasksOnEpic(idSubTask);
                subTasks.remove(idSubTask);
                return;
            }
        }
        for (int idEpicTask : epicTasks.keySet()) {
            if (id == idEpicTask) {
                Epic epicTask = (Epic) epicTasks.get(idEpicTask);
                for (Integer idSub : getAllSubTaskInEpic(epicTask)) {
                    subTasks.remove(idSub);
                    historyManager.remove(idSub);
                }
                historyManager.remove(idEpicTask);
                epicTasks.remove(idEpicTask);

                return;
            }
        }
    }

    @Override
    public Task getTask(int id) {
        searchTask = tasks.get(id);
        historyManager.add(searchTask);
        return searchTask;
    }

    @Override
    public Task getSubTask(int id) {
        searchTask = subTasks.get(id);
        historyManager.add(searchTask);
        return searchTask;
    }

    @Override
    public Task getEpicTask(int id) {
        searchTask = epicTasks.get(id);
        historyManager.add(searchTask);
        return searchTask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Integer> getAllTask() {
        allTaskId.addAll(tasks.keySet());
        allTaskId.addAll(subTasks.keySet());
        allTaskId.addAll(epicTasks.keySet());
        return allTaskId;
    }

    @Override
    public List<Integer> getAllSubTaskInEpic(Epic epic) {
        allSubTaskIdInEpic.clear();
        for (int idSub : epic.getIdSubTasks()) {
            for (int idTasks : subTasks.keySet()) {
                if (idTasks == idSub) {
                    allSubTaskIdInEpic.add(idTasks);
                }
            }
        }
        return allSubTaskIdInEpic;
    }

    public List<Integer> getAllTaskId() {
        return allTaskId;
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
