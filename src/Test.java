public class Test {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Переезд", "привезти");
        taskManager.addTask(task);
        Epic epic = new Epic("Разгрузка");
        taskManager.addEpicTask(epic);
        SubTask subTask = new SubTask("Выгрузить", epic.getId(), "принять вещи в транспортной компании");
        taskManager.addSubTask(subTask);
        SubTask subTask1 = new SubTask("Привезти", epic.getId(), "Привезти вези из тк");
        taskManager.addSubTask(subTask1);
        Epic epic1 = new Epic("Обустройство");
        taskManager.addEpicTask(epic1);
        SubTask subTask2 = new SubTask("улучшение придомовой территории", epic1.getId(),
                "убрать весь мусор");
        taskManager.addSubTask(subTask2);

        taskManager.getAllTask();
        System.out.println(epic.getIdSubTasks());
        System.out.println(epic1.getIdSubTasks());
        System.out.println(epic.getStatus());
        subTask.setStatus(Status.DONE);
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
