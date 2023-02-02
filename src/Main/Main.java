package Main;

import DataTask.Epic;

import DataTask.SubTask;
import DataTask.Task;
import Manager.FileBackedTasksManager;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager taskManager = new FileBackedTasksManager();
        Task task1 = new Task(taskManager.giveId(), "таск 1", "описание 1");
        taskManager.addTask(task1);
        Task task2 = new Task(taskManager.giveId(), "таск 2", "описание 2");
        taskManager.addTask(task2);
        Epic epic1 = new Epic(taskManager.giveId(), "эпик1", "описание эпик1");
        taskManager.addEpicTask(epic1);
        SubTask subTask1 = new SubTask(taskManager.giveId(), "саб1-1",
                "описание саб1-1", epic1.getId());
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(taskManager.giveId(), "саб2-1",
                "описание саб2-1", epic1.getId());
        taskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask(taskManager.giveId(), "саб3-1",
                "описание саб3-1", epic1.getId());
        taskManager.addSubTask(subTask3);
        Epic epic2 = new Epic(taskManager.giveId(), "эпик2", "описание эпик2");
        taskManager.addEpicTask(epic2);
        taskManager.getAllTask();
        taskManager.getEpicTask(epic1.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask3.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpicTask(epic2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getSubTask(subTask2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpicTask(epic1.getId());

        System.out.println(taskManager.getHistory());
        taskManager.getTask(task2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getEpicTask(epic2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println(taskManager.getHistory());


    }
}