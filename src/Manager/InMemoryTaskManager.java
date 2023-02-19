package Manager;

import DataTask.*;
import MyException.IdRepitException;
import MyException.TimeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    static protected int id = 0;
    protected int epicId;
    protected final TreeSet<Task> allSortTasks = new TreeSet<>();
    protected final Map<Integer, Task> allTasks = new HashMap<>();
    protected List<Task> allSubTaskIdInEpic = new ArrayList<>();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Task> subTasks = new HashMap<>();
    protected final Map<Integer, Task> epicTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public static int giveId() {
        id++;
        return id;
    }

    @Override
    public void addTask(Task task) {
        if (isHasIntersection(task)) {
            if(allTasks.containsKey(task.getId())) {
                allTasks.put(task.getId(), task);
                switch (task.getType()) {
                    case TASK: {
                        tasks.put(task.getId(), task);
                        allSortTasks.add(task);
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
                        updateTask.addSubTasksOnEpic(task);
                        epicTasks.put(updateTask.getId(), updateTask);
                        allTasks.put(updateTask.getId(), updateTask);
                        allSortTasks.add(task);
                        break;
                    }
                }
            } else {
                throw new IdRepitException("Задача с таким ID уже существует.");
            }
        } else {
            throw new TimeException("Задача пересекается с другой по времени.");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (isHasIntersection(task)) {
            allTasks.put(task.getId(), task);
            switch (task.getType()) {
                case TASK: {
                    tasks.put(task.getId(), task);
                    allSortTasks.remove(task);
                    allSortTasks.add(task);
                    break;
                }
                case EPIC: {
                    epicTasks.put(task.getId(), task);
                    break;
                }
                case SUBTASK: {
                    updateStatusEpic(task.getEpicId());
                    subTasks.put(task.getId(), task);
                    allSortTasks.remove(task);
                    allSortTasks.add(task);
                    break;
                }
            }
        } else {
            throw new TimeException("Задача пересекается с другой по времени.");
        }
    }


    public void updateStatusEpic(int id) {
        int countSubTaskDONE = 0;
        if (allTasks.size() == 0) {
            return;
        }
        Epic epicTask = (Epic) allTasks.get(id);
        if (epicTask.getIdSubTasks().size() == 0) {
            return;
        }
        for (Task subInEpic : epicTask.getIdSubTasks()) {
            switch (subInEpic.getStatus()) {
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
        allTasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        if (allTasks.size() == 0) {
            return;
        }
        switch (allTasks.get(id).getType()) {
            case TASK: {
                tasks.remove(id);
                allSortTasks.remove(allTasks.get(id));
                break;
            }
            case EPIC: {
                for (Task subInEpic : getAllSubTaskInEpic((Epic) epicTasks.get(id))) {
                    subTasks.remove(subInEpic.getId());
                    historyManager.remove(subInEpic.getId());
                }
                epicTasks.remove(id);
                break;
            }
            case SUBTASK: {
                Epic epicTask = (Epic) epicTasks.get(subTasks.get(id).getEpicId());
                epicTask.removeTasksOnEpic(allTasks.get(id));
                subTasks.remove(id);
                allSortTasks.remove(allTasks.get(id));
                break;
            }
        }
        historyManager.remove(id);
        allTasks.remove(id);
    }

    @Override
    public Task getTask(int id) {
        if (allTasks.size() == 0) {
            return null;
        }
        historyManager.add(allTasks.get(id));
        return allTasks.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Map<Integer, Task> getAllTasks() {
        return allTasks;
    }

    @Override
    public List<Task> getAllSubTaskInEpic(Epic epic) {
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

    public TreeSet<Task> getPrioritizedTasks() {
        return allSortTasks;
    }

    @Override
    public LocalDateTime getTimeNow() {
        return LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return Objects.equals(allTasks, that.allTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allTasks);
    }

    @Override
    public boolean isHasIntersection(Task task) {
        int numberOfIterations = 0;
        if (task.getStartTime() == null || task.getEndTime() == null || allSortTasks.size() == 0) {
            return true;
        }
        for (Task taskInMemory : allSortTasks) {
            if (task.getEpicId() == taskInMemory.getId() || task.getId() == taskInMemory.getEpicId()) {
                numberOfIterations++;
                continue;
            }
            if (taskInMemory.getStartTime() == null || taskInMemory.getEndTime() == null) {
                numberOfIterations++;
                continue;
            }
            if ((task.getStartTime().isAfter(taskInMemory.getEndTime())
                    || (task.getStartTime().isBefore(taskInMemory.getStartTime())))
                    && (task.getEndTime().isBefore(taskInMemory.getStartTime())
                    || task.getEndTime().isAfter(taskInMemory.getEndTime()))) {
                numberOfIterations++;
            }
        }
        return allSortTasks.size() == numberOfIterations;
    }
}
