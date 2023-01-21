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
        TaskManager taskManager = managers.getDefault();
        Task task1 = new Task(taskManager.giveId(), "sdasd", "sdasdasds0");
        taskManager.addTask(task1);
        Task task2 = new Task(taskManager.giveId(), "sdas23d", "sdasd23123asds0");
        taskManager.addTask(task2);
        Epic epic1 = new Epic(taskManager.giveId(), "Разгрузка","описание");
        taskManager.addEpicTask(epic1);
        SubTask subTask1 = new SubTask(taskManager.giveId(), "Выгрузить",
                "принять вещи в транспортной компании", epic1.getId());
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(taskManager.giveId(), "Привезти",
                "Привезти вези из тк", epic1.getId());
        taskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask(taskManager.giveId(), "Приdtи",
                "123Привезти вkkkи из тк", epic1.getId());
        taskManager.addSubTask(subTask3);
        Epic epic2 = new Epic(taskManager.giveId(), "Обустройство", "object");
        taskManager.addEpicTask(epic2);
        taskManager.getEpicTask(epic1.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask3.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpicTask(epic2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getTask(task2.getId());
        taskManager.deleteTask(epic1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpicTask(epic2.getId());
        taskManager.deleteTask(task2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpicTask(epic2.getId());
        taskManager.getTask(task1.getId());
        System.out.println(taskManager.getHistory());
    }
}