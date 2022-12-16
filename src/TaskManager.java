import java.util.HashMap;

public class TaskManager {

    private int id = 1;
    int epicId;
    HashMap<Integer, Object> tasks = new HashMap<>();
    HashMap<Integer, Object> subTasks = new HashMap<>();
    HashMap<Integer, Object> epicTasks = new HashMap<>();
    String gettingTask;

   
    public int newTask(Task task) { // Блок методов добавления и сохранения задач
        
        task.setId(id++);
        tasks.put(task.getId(),task);
        return task.getId();
    }
    
    public int newSubTask(SubTask subTask) {

        subTask.setId(id++);
        subTasks.put(subTask.getId(),subTask);

        for (int idEpicTask : epicTasks.keySet()) {
            if (subTask.getEpicId() == idEpicTask) {
                Epic updateTask = (Epic) epicTasks.get(idEpicTask);
                updateTask.addSubTasksOnEpic(idEpicTask);
                epicTasks.put(idEpicTask, updateTask);
            }
        }
        return  subTask.getId();
    }

    public int newEpicTask(Epic epic) {
        epic.setId(id++);
        epicId = id;
        epicTasks.put(epic.getId(), epic);
        return epic.getId();
    }

    public void updateTask(Task task) { // Блок методов обновления задач
        tasks.remove(task.getId());
        tasks.put(task.getId(), task);
    }
    
    public void updateSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
    }
    
    public void updateEpicTask(Epic epic) {
        epicTasks.remove(epic.getId());
        epicTasks.put(epic.getId(),epic);
    }


    public void deleteAll() { // Блок методов удаления задач

        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
    }

    public void deleteTask(int id) {

        for (int idTask : tasks.keySet()) {
            if (id == idTask) {
                tasks.remove(idTask);
            }
        }
        for (int idSubTask : subTasks.keySet()) {
            if (id == idSubTask) {
                SubTask subTask = (SubTask) subTasks.get(id);
                Epic epicTask = (Epic) epicTasks.get(subTask.getEpicId());
                epicTask.removeTasksOnEpic(id);
                subTasks.remove(idSubTask);
            }
        }
        for (Integer idEpicTask : epicTasks.keySet()) {
            if (id == idEpicTask) {
                Epic epicTask = (Epic) epicTasks.get(id);
                for (int idTasks : subTasks.keySet()) {
                    for (int idSub : epicTask.getIdSubTasks()) {
                        if (idTasks == idSub) {
                            subTasks.remove(idSub);
                        }
                    }
                }
            }
            epicTasks.remove(idEpicTask);
        }
    }

    public String getTask(int id) { // метод получения задачи по id

        for (Integer idTask : tasks.keySet()) {
            if (id == idTask) {
                gettingTask = tasks.get(idTask).toString();
            }
            for (Integer idSubTask : subTasks.keySet()) {
                if (id == idSubTask) {
                    gettingTask = subTasks.get(idSubTask).toString();
                }
                for (Integer idEpicTask : epicTasks.keySet()) {
                    if (id == idEpicTask) {
                        gettingTask = epicTasks.get(idEpicTask).toString();
                    }
                }
            }
        }
    return gettingTask;
    }
}
