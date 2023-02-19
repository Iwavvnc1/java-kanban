package Manager;

import DataTask.*;
import MyException.ManagerSaveException;
import MyException.ThisFileNotFound;
import MyException.ThisNullPointer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static final File file = new File("resources", "Tasks_and_history.csv");
    private final String dir = "resources";
    private final String fileName = "Tasks_and_history.csv";

    private void save() {

        try {
            if (!Files.exists(Paths.get(dir))) {
                Files.createDirectory(Paths.get(dir));
            }
            if (!Files.exists(Paths.get(dir, fileName))) {
                Files.createFile(Paths.get(dir, fileName));
            }
            FileWriter writer = new FileWriter(dir + "/" + fileName);
            FileReader reader = new FileReader(dir + "/" + fileName);
            BufferedReader br = new BufferedReader(reader);
            if (br.readLine() == null) {
                writer.write("id,,type,,name,,status,,description,,starTime,,duration,,epic,," + "\n");
            }
            for (Task task : getAllTasks().values()) {
                writer.write(task.toString());
                if(task.getStartTime() != null) {
                    writer.write(task.getStartTime().format(formatter) + ",,");
                } else {
                    writer.write("null,,");
                }
                if(task.getDuration() != null) {
                    writer.write(Integer.parseInt(String.valueOf(task.getDuration().toSeconds())) + ",,");
                } else {
                    writer.write("null,,");
                }
                if(task.getType() == TypeTask.SUBTASK) {
                    writer.write("" + task.getEpicId());
                }
                writer.write("\n");
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
            if (id < Integer.parseInt(lineTask[0])) {
                id = Integer.parseInt(lineTask[0]);
            }
            if(lineTask[5].equals("null")) {
                switch (lineTask[1]) {
                    case "TASK": {
                        return new Task(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                                Status.valueOf(lineTask[3]));
                    }
                    case "EPIC": {
                        return new Epic(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                                Status.valueOf(lineTask[3]));
                    }
                    case "SUBTASK": {
                        return new SubTask(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                                Integer.parseInt(lineTask[7]), Status.valueOf(lineTask[3]));
                    }
                }
            }
            switch (lineTask[1]) {
                case "TASK": {
                    return new Task(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                            Status.valueOf(lineTask[3]), LocalDateTime.parse(lineTask[5],formatter),
                            Duration.ofSeconds(Integer.parseInt(lineTask[6])));
                }
                case "EPIC": {
                    return new Epic(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                            Status.valueOf(lineTask[3]), LocalDateTime.parse(lineTask[5],formatter),
                            Duration.ofSeconds(Integer.parseInt(lineTask[6])));
                }
                case "SUBTASK": {
                    return new SubTask(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                            Integer.parseInt(lineTask[7]), Status.valueOf(lineTask[3]),
                            LocalDateTime.parse(lineTask[5],formatter),
                            Duration.ofSeconds(Integer.parseInt(lineTask[6])));
                }
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

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {
            boolean isHistory = false;
            while (br.ready()) {
                String newLine = br.readLine();
                if (newLine.equals("id,,type,,name,,status,,description,,starTime,,duration,,epic,,")) {
                    continue;
                }
                if (isHistory) {
                    for (Integer id : historyFromString(newLine)) {
                        manager.historyManager.add(manager.getTask(id));
                    }
                    return manager;
                }
                if (newLine.equals("")) {
                    isHistory = true;
                    continue;
                }
                Task task = manager.fromString(newLine);
                if (task == null) continue;
                manager.addTask(task);
            }
            return manager;
        } catch (FileNotFoundException e) {
            throw new ThisFileNotFound("Запрашиваемый файл не найден");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода/вывода");
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
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


    public static void main(String[] args) {
        try {
            TaskManager taskManager = new FileBackedTasksManager();

            Task task1 = new Task( "таск 1", "описание 1");
            taskManager.addTask(task1);
            Task task2 = new Task( "таск 2", "описание 2");
            taskManager.addTask(task2);
            Epic epic1 = new Epic( "эпик1", "описание эпик1");
            taskManager.addTask(epic1);
            SubTask subTask1 = new SubTask( "саб1-1",
                    "описание саб1-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,12), Duration.ofMinutes(10));
            taskManager.addTask(subTask1);
            SubTask subTask2 = new SubTask( "саб2-1",
                    "описание саб2-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,0), Duration.ofMinutes(11));
            taskManager.addTask(subTask2);
            SubTask subTask3 = new SubTask( "саб3-1",
                    "описание саб3-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,30), Duration.ofMinutes(12));
            taskManager.addTask(subTask3);
            Epic epic2 = new Epic( "эпик2", "описание эпик2");
            taskManager.addTask(epic2);
            SubTask subTask4 = new SubTask( "саб4-1",
                    "описание саб4-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,24), Duration.ofMinutes(5));
            taskManager.addTask(subTask4);
            taskManager.getAllTasks();
            taskManager.getTask(epic1.getId());
            taskManager.getTask(subTask1.getId());
            taskManager.getTask(subTask2.getId());
            taskManager.getTask(subTask3.getId());
            TaskManager taskManager2 = loadFromFile(file);
            for (Integer s : taskManager.getAllTasks().keySet()) {
                System.out.println(s+ " " + taskManager.getAllTasks().get(s));
            }
            /*System.out.println("История 1 менеджера");
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
            System.out.println(taskManager2.getEpicTasks());*/
            //TaskManager taskManager2 = loadFromFile(file);
            Task task3 = new Task( "таск 3", "описание 3");
            taskManager2.addTask(task3);
            for (Task prioritizedTask : taskManager.getPrioritizedTasks()) {
                System.out.print(prioritizedTask.getTitle() + " - ");
                System.out.println(prioritizedTask.getStartTime());
            }
           // System.out.println(taskManager2.getTasks());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода/вывода");
        } catch (NullPointerException e) {
            throw new ThisNullPointer("Ошибка проведения операции с null/0",e);
        }
    }
}