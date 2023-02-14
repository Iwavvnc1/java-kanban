package Main;

import DataTask.Epic;

import DataTask.SubTask;
import DataTask.Task;
import Manager.FileBackedTasksManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager taskManager = new FileBackedTasksManager();
        Task task1 = new Task(FileBackedTasksManager.giveId(), "таск 1", "описание 1",
                LocalDateTime.of(2002,10,13,2,0), Duration.ofMinutes(11));
        taskManager.addTask(task1);
        Task task2 = new Task(FileBackedTasksManager.giveId(), "таск 2", "описание 2",
                LocalDateTime.of(2002,10,13,2,12), Duration.ofMinutes(12));
        taskManager.addTask(task2);
        Epic epic1 = new Epic(FileBackedTasksManager.giveId(), "эпик1", "описание эпик1");
        taskManager.addTask(epic1);
        SubTask subTask1 = new SubTask(FileBackedTasksManager.giveId(), "саб1-1",
                "описание саб1-1", epic1.getId(), LocalDateTime.of
                (2002,10,13,2,26), Duration.ofMinutes(16));
        taskManager.addTask(subTask1);
        SubTask subTask2 = new SubTask(FileBackedTasksManager.giveId(), "саб2-1",
                "описание саб2-1", epic1.getId(),  LocalDateTime.of
                (2002,10,13,2,43), Duration.ofMinutes(10));
        taskManager.addTask(subTask2);
        SubTask subTask3 = new SubTask(FileBackedTasksManager.giveId(), "саб3-1",
                "описание саб3-1", epic1.getId());
        taskManager.addTask(subTask3);
        Epic epic2 = new Epic(FileBackedTasksManager.giveId(), "эпик2", "описание эпик2",
                LocalDateTime.of(2002,10,13,2,54), Duration.ofMinutes(10));
        taskManager.addTask(epic2);

        taskManager.getTask(epic1.getId());
        taskManager.getTask(subTask1.getId());
        taskManager.getTask(subTask2.getId());
        taskManager.getTask(subTask3.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(epic2.getId());
        System.out.println(taskManager.getPrioritizedTasks());
        /*taskManager.getTask(subTask2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(epic1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(epic2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println(taskManager.getHistory());*/
    }
}