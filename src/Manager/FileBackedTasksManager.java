package Manager;

import DataTask.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    static File file = new File("resources", "Tasks_and_history.csv");

    public void save() {

        try {
            if (!Files.exists(Paths.get("resources"))) {
                Files.createDirectory(Paths.get("resources"));
            }
            if (!Files.exists(Paths.get("resources", "Tasks_and_history.csv"))) {
                Files.createFile(Paths.get("resources", "Tasks_and_history.csv"));
            }
            FileWriter writer = new FileWriter("resources/Tasks_and_history.csv");
            FileReader reader = new FileReader("resources/Tasks_and_history.csv");
            BufferedReader br = new BufferedReader(reader);
            if (br.readLine() == null) {
                writer.write("id,,type,,name,,status,,description,,epic" + "\n");
            }
            for (Integer id : getTasks().keySet()) {
                writer.write(getTasks().get(id).toString());
            }
            for (Integer id : getEpicTasks().keySet()) {
                writer.write(getEpicTasks().get(id).toString());
            }
            for (Integer id : getSubTasks().keySet()) {
                writer.write(getSubTasks().get(id).toString());
            }
            writer.write("\n");
            if (getHistoryManager().getHistory().size() != 0) {
                writer.write(historyToString(getHistoryManager()));
            }
            writer.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода/вывода");
        }
    }

    private Task fromString(String value) {
        if (!value.equals("")) {
            String[] lineTask = value.split(",,");
            if (lineTask[1].equals(TypeTask.TASK.toString())) {
                return new Task(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4], Status.valueOf(lineTask[3]));
            } else if (lineTask[1].equals(TypeTask.EPIC.toString())) {
                return new Epic(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4], Status.valueOf(lineTask[3]));
            } else if (lineTask[1].equals(TypeTask.SUBTASK.toString())) {
                return new SubTask(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                        Integer.parseInt(lineTask[5]), Status.valueOf(lineTask[3]));
            }
        }
        return null;
    }

    private static String historyToString(HistoryManager manager) throws IOException {
        StringBuilder historyManager = new StringBuilder();
        for (Task task : manager.getHistory()) {
            historyManager.append(task.getId()).append(",");
        }
        return historyManager.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> idHistory = new ArrayList<>();
        String[] id = value.split(",");
        for (String idInHistory : id) {
            idHistory.add(Integer.parseInt(idInHistory));
        }
        return idHistory;
    }

    private static FileBackedTasksManager loadFromFile(File file) throws IOException, NullPointerException {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        boolean isHistory = false;
        while (br.ready()) {
            String newLine = br.readLine();
            if (newLine.equals("id,,type,,name,,status,,description,,epic")) {
                continue;
            }
            if (isHistory) {
                for (Integer id : historyFromString(newLine)) {
                    if (manager.getTasks().containsKey(id)) {
                        manager.historyManager.add(manager.getTask(id));
                    } else if (manager.getSubTasks().containsKey(id)) {
                        manager.historyManager.add(manager.getSubTask(id));
                    } else if (manager.getEpicTasks().containsKey(id)) {
                        manager.historyManager.add(manager.getEpicTask(id));
                    }
                }
                br.close();
                return manager;
            }
            if (newLine.equals("")) {
                isHistory = true;
                continue;
            }

            Task task = manager.fromString(newLine);
            if (task == null) continue;
            if (task.getType().equals(TypeTask.TASK)) {
                manager.addTask(task);
            } else if (task.getType() == TypeTask.SUBTASK) {
                manager.addSubTask((SubTask) task);
            } else if (task.getType() == TypeTask.EPIC) {
                manager.addEpicTask((Epic) task);
            }
        }
        br.close();
        return null;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpicTask(Epic epic) {
        super.addEpicTask(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task searchTask = super.getTask(id);
        save();
        return searchTask;
    }

    @Override
    public Task getSubTask(int id) {
        Task searchTask = super.getSubTask(id);
        save();
        return searchTask;
    }

    @Override
    public Task getEpicTask(int id) {
        Task searchTask = super.getEpicTask(id);
        save();
        return searchTask;
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = new FileBackedTasksManager();
        Task task1 = new Task(taskManager.giveId(), "таск 1", "описание 1");
        taskManager.addTask(task1);
        Task task2 = new Task(taskManager.giveId(), "таск 2", "описание 2");
        taskManager.addTask(task2);
        Epic epic1 = new Epic(taskManager.giveId(), "эпик1", "описание эпик1, у которого есть сабы");
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
        TaskManager taskManager2 = loadFromFile(file);
        System.out.println("История 1 менеджера");
        System.out.println(taskManager.getHistory());
        System.out.println("История 2 менеджера");
        System.out.println(taskManager2.getHistory());
        System.out.println("таски 1 менеджера");
        System.out.println(taskManager.getTasks());
        System.out.println("таски 2 менеджера");
        System.out.println(taskManager2.getTasks());
        System.out.println("сабтаски 1 менеджера");
        System.out.println(taskManager.getSubTasks());
        System.out.println("сабтаски 2 менеджера");
        System.out.println(taskManager2.getSubTasks());
        System.out.println("эпики 1 менеджера");
        System.out.println(taskManager.getEpicTasks());
        System.out.println("эпики 2 менеджера");
        System.out.println(taskManager2.getEpicTasks());
    }
}