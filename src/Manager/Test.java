package Manager;

import DataTask.Epic;
import DataTask.Status;
import DataTask.SubTask;
import DataTask.Task;

import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Managers managers = new Managers();
        InMemoryTaskManager taskManager = (InMemoryTaskManager) managers.getDefault();
        Task task = new Task(taskManager.giveId(),"Переезд", "привезти");
        taskManager.addTask(task);
        Epic epic = new Epic(taskManager.giveId(), "Разгрузка","описание");
        taskManager.addEpicTask(epic);
        SubTask subTask = new SubTask(taskManager.giveId(), "Выгрузить",
                "принять вещи в транспортной компании", epic.getId());
        taskManager.addSubTask(subTask);
        SubTask subTask1 = new SubTask(taskManager.giveId(), "Привезти",
                "Привезти вези из тк", epic.getId());
        taskManager.addSubTask(subTask1);
        Epic epic1 = new Epic(taskManager.giveId(), "Обустройство", "object");
        taskManager.addEpicTask(epic1);
        SubTask subTask2 = new SubTask(taskManager.giveId(), "улучшение придомовой территории",
                "убрать весь мусор",epic1.getId());
        taskManager.addSubTask(subTask2);
        taskManager.getEpicTask(epic.getId());
        taskManager.getSubTask(subTask.getId());
        taskManager.getTask(task.getId());
        for (Task task1 : taskManager.getHistory()) {
            System.out.println(task1);
            System.out.println(task1.getId());
        }
        taskManager.getAllTask();
        System.out.println(epic.getIdSubTasks());
        System.out.println(epic1.getIdSubTasks());
        System.out.println(subTask1.getStatus());
        System.out.println(subTask.getStatus());
        System.out.println(epic.getStatus());
        subTask.setStatus(Status.IN_PROGRESS);
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask);
        System.out.println(subTask1.getStatus());
        System.out.println(epic.getStatus());
        System.out.println(taskManager.allTaskId);
        taskManager.getAllSubTaskInEpic();
        System.out.println(taskManager.allSubTaskIdInEpic);
        System.out.println(taskManager.subTasks.size());
        taskManager.deleteTask(subTask.getId());
        // taskManager.deleteAll();
        taskManager.deleteTask(subTask2.getId());
        System.out.println(taskManager.subTasks.size());
        System.out.println(taskManager.epicTasks.size());
        taskManager.deleteTask(epic.getId());
        System.out.println(taskManager.epicTasks.size());

    }
}
