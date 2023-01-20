package Manager;

import DataTask.Epic;
import DataTask.Status;
import DataTask.SubTask;
import DataTask.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    static private int id = 0;
    int epicId;
    Task searchTask;
    private ArrayList<Integer> allTaskId = new ArrayList<>();
    private ArrayList<Integer> allSubTaskIdInEpic = new ArrayList<>();
    private HashMap<Integer, Object> tasks = new HashMap<>();
    private HashMap<Integer, Object> subTasks = new HashMap<>();
    private HashMap<Integer, Object> epicTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public int giveId() {
        id++;
        return id;
    }

    @Override
    public int addTask(Task task) { // Блок методов добавления и сохранения задач
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
        for (int idEpicTask : epicTasks.keySet()) {
            if (id == idEpicTask) {
                Epic epicTask = (Epic) epicTasks.get(idEpicTask);
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
        }
    }

    @Override
    public void deleteAll() { // Блок методов удаления задач
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
    public Task getTask(int id) { // метод получения задачи по id
        searchTask = null;
        for (int idTask : tasks.keySet()) {
            if (id == idTask) {
                searchTask = (Task) tasks.get(idTask);
                break;
            }
        }
        historyManager.add(searchTask);
        return searchTask;
    }

    @Override
    public Task getSubTask(int id) {
        searchTask = null;
        for (int idSubTask : subTasks.keySet()) {
            if (id == idSubTask) {
                searchTask = (Task) subTasks.get(idSubTask);
                break;
            }
        }
        historyManager.add(searchTask);
        return searchTask;
    }

    @Override
    public Task getEpicTask(int id) {
        searchTask = null;
        for (int idEpicTask : epicTasks.keySet()) {
            if (id == idEpicTask) {
                searchTask = (Task) epicTasks.get(idEpicTask);
                break;
            }
        }
        historyManager.add(searchTask);
        return searchTask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Integer> getAllTask() { // метод получения списка всех задач
        for (int idTask : tasks.keySet()) {
            allTaskId.add(idTask);
        }
        for (int idSubTask : subTasks.keySet()) {
            allTaskId.add(idSubTask);
        }
        for (int idEpicTask : epicTasks.keySet()) {
            allTaskId.add(idEpicTask);
        }
        return allTaskId;
    }

    @Override
    public ArrayList<Integer> getAllSubTaskInEpic(Epic epic) {
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

    public ArrayList<Integer> getAllTaskId() {
        return allTaskId;
    }

    public HashMap<Integer, Object> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Object> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Object> getEpicTasks() {
        return epicTasks;
    }

}
