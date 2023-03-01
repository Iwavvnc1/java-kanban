package Manager;

import DataTask.*;
import MyException.ManagerSaveException;
import MyException.ThisFileNotFound;
import MyException.ThisNullPointer;
import TypeAdapter.LocalDateTimeDeserializer;
import TypeAdapter.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static File newFile;
    public static final File file = new File("resources"+ File.separator + "Tasks_and_history.csv");

    public FileBackedTasksManager(File newFile) {
        FileBackedTasksManager.newFile = newFile;
    }


    public void save() throws IOException, InterruptedException {
        try {
            FileWriter writer = new FileWriter(newFile);
            FileReader reader = new FileReader(newFile);
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
                    writer.write(task.getDuration() + ",,");
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
                            Integer.parseInt(lineTask[6]));
                }
                case "EPIC": {
                    return new Epic(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                            Status.valueOf(lineTask[3]), LocalDateTime.parse(lineTask[5],formatter),
                            Integer.parseInt(lineTask[6]));
                }
                case "SUBTASK": {
                    return new SubTask(Integer.parseInt(lineTask[0]), lineTask[2], lineTask[4],
                            Integer.parseInt(lineTask[7]), Status.valueOf(lineTask[3]),
                            LocalDateTime.parse(lineTask[5],formatter),
                            Integer.parseInt(lineTask[6]));
                }
            }
        }
        return null;
    }

    protected static String historyToString(HistoryManager manager) throws IOException {
        StringBuilder historyManager = new StringBuilder();
        for (Task task : manager.getHistory()) {
            historyManager.append(task.getId()).append(",");
        }
        return historyManager.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> idHistory = new ArrayList<>();
        if(!value.equals("")) {
            String[] id = value.split(",");
            for (String idInHistory : id) {
                idHistory.add(Integer.parseInt(idInHistory));
            }
            return idHistory;
        }
        return idHistory;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws InterruptedException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
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
                        manager.getTask(id);
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
    public int addTask(Task task) throws IOException, InterruptedException {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public boolean updateTask(Task task) throws IOException, InterruptedException {
        if (super.updateTask(task)) {
            save();
            return true;
        }
        save();
        return false;
    }


    @Override
    public boolean deleteAllTasks() throws IOException, InterruptedException {
        if(super.deleteAllTasks()) {
            save();
            return true;
        }
        save();
        return false;
    }

    @Override
    public boolean deleteTask(int id) throws IOException, InterruptedException {
        if(super.deleteTask(id)) {
            save();
            return true;
        }
        save();
        return false;
    }

    @Override
    public Task getTask(int id) throws IOException, InterruptedException {
        Task searchTask = super.getTask(id);
        save();
        return searchTask;
    }


    public static void main(String[] args) throws InterruptedException {
        try {
            TaskManager taskManager = Managers.getDefaultFileBackedTasksManager(file);

            Task task1 = new Task( "таск 1", "описание 1");
            taskManager.addTask(task1);
            Task task2 = new Task( "таск 2", "описание 2");
            taskManager.addTask(task2);
            Epic epic1 = new Epic( "эпик1", "описание эпик1");
            taskManager.addTask(epic1);
            SubTask subTask1 = new SubTask( "саб1-1",
                    "описание саб1-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,12), 10);
            taskManager.addTask(subTask1);
            SubTask subTask2 = new SubTask( "саб2-1",
                    "описание саб2-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,0), 11);
            taskManager.addTask(subTask2);
            SubTask subTask3 = new SubTask( "саб3-1",
                    "описание саб3-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,30),12);
            taskManager.addTask(subTask3);
            Epic epic2 = new Epic( "эпик2", "описание эпик2");
            taskManager.addTask(epic2);
            SubTask subTask4 = new SubTask( "саб4-1",
                    "описание саб4-1", epic1.getId(),
                    LocalDateTime.of(2002,10,13,2,24), 5);
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
            Task task3 = new Task( "таск 3", "описание 3");
            taskManager2.addTask(task3);
            for (Task prioritizedTask : taskManager.getPrioritizedTasks()) {
                System.out.print(prioritizedTask.getTitle() + " - ");
                System.out.println(prioritizedTask.getStartTime());
            }
            final Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .registerTypeAdapter(LocalDateTime.class,new LocalDateTimeSerializer())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                    .create();
            String s = gson.toJson(subTask4);
            System.out.println(s);
           SubTask sub = gson.fromJson(s, SubTask.class);
            System.out.println(sub);
            System.out.println(taskManager2.getTasks());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода/вывода");
        } catch (NullPointerException e) {
            throw new ThisNullPointer("Ошибка проведения операции с null/0",e);
        }
    }
}