package Manager;

import DataTask.*;
import MyException.*;

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
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public static int giveId() {
        id++;
        return id;
    }

    @Override
    public int addTask(Task task) {
        if (isHasIntersection(task)) {
                switch (task.getType()) {
                    case TASK: {
                        if(!allTasks.containsKey(task.getId())) {
                            allTasks.put(task.getId(), task);
                        } else {
                            throw new IdRepitException("Задача с таким ID уже существует.");
                        }
                        if (task.getId() == 0) {
                            task.setId(giveId());
                        }
                        tasks.put(task.getId(), task);
                        allSortTasks.add(task);
                        break;
                    }
                    case EPIC: {
                        if(!allTasks.containsKey(task.getId())) {
                            allTasks.put(task.getId(), task);
                        } else {
                            throw new IdRepitException("Задача с таким ID уже существует.");
                        }
                        if (task.getId() == 0) {
                            task.setId(giveId());
                        }
                        epicId = id;
                        epicTasks.put(task.getId(), task);
                        break;
                    }
                    case SUBTASK: {
                        if(task.getEpicId() == 0) {
                            throw new SubWithoutEpicId("Sub без EpicId.");
                        }
                        if (!epicTasks.containsKey(task.getEpicId())) {
                           throw new NoCorrectEpicId("Задача с таким Id не является Epic.");
                        }
                        if (task.getId() == 0) {
                            task.setId(giveId());
                        }
                        if(!allTasks.containsKey(task.getId())) {
                            allTasks.put(task.getId(), task);
                        } else {
                            throw new IdRepitException("Задача с таким ID уже существует.");
                        }
                        subTasks.put(task.getId(), task);
                        Epic updateEpic = (Epic) epicTasks.get(task.getEpicId());
                        updateEpic.addSubTasksOnEpic(task);
                        updateStatusEpic(task.getEpicId());
                        epicTasks.put(updateEpic.getId(), updateEpic);
                        allTasks.put(updateEpic.getId(), updateEpic);
                        allSortTasks.add(task);
                        updateTimeEpic(task);
                        break;
                    }
                }
        } else {
            throw new TimeException("Задача пересекается с другой по времени.");
        }
        return task.getId();
    }

    @Override
    public boolean updateTask(Task task) {
        try {
            if (isHasIntersection(task)) {
                switch (task.getType()) {
                    case TASK: {
                        tasks.put(task.getId(), task);
                        allSortTasks.remove(allTasks.get(task.getId()));
                        allSortTasks.add(task);
                        allTasks.put(task.getId(), task);
                        if (task.getStartTime() != null) {
                            updateTime(task);
                        }
                        return true;
                    }
                    case EPIC: {
                        epicTasks.put(task.getId(), task);
                        allTasks.put(task.getId(), task);
                        return true;
                    }
                    case SUBTASK: {
                        subTasks.put(task.getId(), task);
                        allSortTasks.remove(allTasks.get(task.getId()));
                        allSortTasks.add(task);
                        Epic updateEpic = (Epic) epicTasks.get(task.getEpicId());
                        updateEpic.addSubTasksOnEpic(task);
                        updateStatusEpic(task.getEpicId());
                        updateTimeEpic(task);
                        allTasks.put(task.getId(), task);
                        return true;
                    }
                }
                return false;
            } else {
                throw new TimeException("Задача пересекается с другой по времени.");
            }
        } catch (NullPointerException e) {
            throw new ThisNullPointer("Задача не добавлена в менеджер.",e);
        }
    }

    private void updateTimeEpic(Task task) {
        allTasks.get(task.getEpicId()).getStartTime();
        allTasks.get(task.getEpicId()).getDuration();
        allTasks.get(task.getEpicId()).getEndTime();
    }

    private void updateTime(Task task) {
        allTasks.get(task.getId()).getEndTime();
    }

    public void updateStatusEpic(int id) {
        int countSubTaskDONE = 0;
        int countSubTaskNEW = 0;
        if (allTasks.size() == 0) {
            return;
        }
        Epic epicTask = (Epic) allTasks.get(id);
        if (epicTask.getIdSubTasks().size() == 0) {
            return;
        }
        for (Task subInEpic : epicTask.getIdSubTasks().values()) {
            switch (subInEpic.getStatus()) {
                case NEW: {
                    countSubTaskNEW++;
                    break;
                }
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
            if (countSubTaskNEW == epicTask.getIdSubTasks().size()) {
                epicTask.setStatus(Status.NEW);
            }
        }
        updateTask(epicTask);
    }

    @Override
    public boolean deleteAllTasks() {
        for (Task task : tasks.values()) {
            allTasks.remove(task.getId());
            allSortTasks.remove(task);
        }
        tasks.clear();
        return true;
    }

    @Override
    public boolean deleteAllSubTasks() {
        for (Task sub : subTasks.values()) {
            allTasks.remove(sub.getId());
            allSortTasks.remove(sub);
        }
        subTasks.clear();
        return true;
    }

    @Override
    public boolean deleteAllEpics() {
        for (Task epic : epicTasks.values()) {
            allTasks.remove(epic.getId());
            allSortTasks.remove(epic);
        }
        epicTasks.clear();
        return true;
    }

    @Override
    public boolean deleteTask(int id) {
        if (allTasks.size() == 0) {
            return false;
        }
        switch (allTasks.get(id).getType()) {
            case TASK: {
                tasks.remove(id);
                allSortTasks.remove(allTasks.get(id));
                historyManager.remove(id);
                allTasks.remove(id);
                return true;
            }
            case EPIC: {
                for (Task subInEpic : getAllSubTaskInEpic((Epic) epicTasks.get(id))) {
                    subTasks.remove(subInEpic.getId());
                    historyManager.remove(subInEpic.getId());
                }
                epicTasks.remove(id);
                historyManager.remove(id);
                allTasks.remove(id);
                return true;
            }
            case SUBTASK: {
                Epic epicTask = (Epic) epicTasks.get(subTasks.get(id).getEpicId());
                epicTask.removeTasksOnEpic(allTasks.get(id));
                updateStatusEpic(subTasks.get(id).getEpicId());
                subTasks.remove(id);
                allSortTasks.remove(allTasks.get(id));
                historyManager.remove(id);
                allTasks.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public Task getTask(int id)  {
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
        allSubTaskIdInEpic.addAll(epic.getIdSubTasks().values());
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
            if (task.getId() == taskInMemory.getId() && task.getStartTime() != null
            && taskInMemory.getStartTime() != null) {
               if (task.getStartTime().isEqual(taskInMemory.getStartTime())) {
                    return true;
                }
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
