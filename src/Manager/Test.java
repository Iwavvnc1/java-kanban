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
        taskManager.getEpicTask(epic2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.deleteTask(epic1.getId());
        System.out.println(taskManager.getHistory());
    }
}