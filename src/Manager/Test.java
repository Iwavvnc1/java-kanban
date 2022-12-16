package Manager;

import DataTask.Epic;
import DataTask.Status;
import DataTask.SubTask;
import DataTask.Task;

public class Test {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task(taskManager.getId(),"Переезд", "привезти",Status.NEW);
        taskManager.addTask(task);
        Epic epic = new Epic(taskManager.getId(), "Разгрузка","описание");
        taskManager.addEpicTask(epic);
        SubTask subTask = new SubTask(taskManager.getId(), "Выгрузить",
                "принять вещи в транспортной компании", epic.getId());
        taskManager.addSubTask(subTask);
        SubTask subTask1 = new SubTask(taskManager.getId(), "Привезти",
                "Привезти вези из тк", epic.getId());
        taskManager.addSubTask(subTask1);
        Epic epic1 = new Epic(taskManager.getId(), "Обустройство", "object");
        taskManager.addEpicTask(epic1);
        SubTask subTask2 = new SubTask(taskManager.getId(), "улучшение придомовой территории",
                "убрать весь мусор",epic1.getId());
        taskManager.addSubTask(subTask2);

        taskManager.getAllTask();
        System.out.println(epic.getIdSubTasks());
        System.out.println(epic1.getIdSubTasks());
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
} /* Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
И, наконец, попробуйте удалить одну из задач и один из эпиков. */
