package Manager;

import DataTask.Epic;
import DataTask.Status;
import DataTask.SubTask;
import DataTask.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private int id = 0;
    int epicId;
    Object searchTask;
    ArrayList<Integer> allTaskId = new ArrayList<>();
    ArrayList<Integer> allSubTaskIdInEpic = new ArrayList<>();
    HashMap<Integer, Object> tasks = new HashMap<>();
    HashMap<Integer, Object> subTasks = new HashMap<>();
    HashMap<Integer, Object> epicTasks = new HashMap<>();

    public int getId() {
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
                tasks.remove(idTask);
                break;
            }
        }
        for (int idSubTask : subTasks.keySet()) {
            if (id == idSubTask) {
                SubTask subTask = (SubTask) subTasks.get(idSubTask);
                Epic epicTask = (Epic) epicTasks.get(subTask.getEpicId());
                epicTask.removeTasksOnEpic(idSubTask);
                subTasks.remove(idSubTask);
                break;
            }
        }
        for (int idEpicTask : epicTasks.keySet()) {
            if (id == idEpicTask) {
                Epic epicTask = (Epic) epicTasks.get(idEpicTask);
                for (int idSub : epicTask.getIdSubTasks()) {
                    for (int idTasks : subTasks.keySet()) {
                        if (idTasks == idSub) {
                            subTasks.remove(idSub);
                            break;
                        }
                    }
                }
                epicTasks.remove(idEpicTask);
                break;
            }
        }
    }

    @Override
    public Object getTask(int id) { // метод получения задачи по id

        searchTask = null;
        for (int idTask : tasks.keySet()) {
            if (id == idTask) {
                searchTask = tasks.get(idTask);
                break;
            }
        }
        Managers.getDefaultHistory().updateHistory((Task) searchTask);
        return searchTask;
    }

    @Override
    public Object getSubTask(int id) {

        searchTask = null;
        for (int idSubTask : subTasks.keySet()) {
            if (id == idSubTask) {
                searchTask = subTasks.get(idSubTask);
                break;
            }
        }
        Managers.getDefaultHistory().updateHistory((Task) searchTask);
        return searchTask;
    }

    @Override
    public Object getEpicTask(int id) {

        searchTask = null;
        for (int idEpicTask : epicTasks.keySet()) {
            if (id == idEpicTask) {
                searchTask = epicTasks.get(idEpicTask);
                break;
            }
        }
        Managers.getDefaultHistory().updateHistory((Task) searchTask);
        return searchTask;
    }

    @Override
    public void getAllTask() { // метод получения списка всех задач
        for (int idTask : tasks.keySet()) {
            allTaskId.add(idTask);
        }
        for (int idSubTask : subTasks.keySet()) {
            allTaskId.add(idSubTask);
        }
        for (int idEpicTask : epicTasks.keySet()) {
            allTaskId.add(idEpicTask);
        }
    }

    @Override
    public void getAllSubTaskInEpic() {

        for (int idEpicTask : epicTasks.keySet()) {
            Epic epicTask = (Epic) epicTasks.get(idEpicTask);
            for (int idSub : epicTask.getIdSubTasks()) {
                for (int idTasks : subTasks.keySet()) {
                    if (idTasks == idSub) {
                        allSubTaskIdInEpic.add(idTasks);
                    }
                }
            }
        }
    }

}