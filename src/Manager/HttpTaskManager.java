package Manager;


import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;
import KVServer.KVServer;
import KVTaskClient.KVTaskClient;
import MyException.IdRepitException;
import MyException.NoCorrectEpicId;
import MyException.SubWithoutEpicId;
import TypeAdapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    protected URL url;
    KVTaskClient kvClient;

    private final Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

    public HttpTaskManager(File newFile) {
        super(newFile);
        try {
            this.url = newFile.toURI().toURL();
            kvClient = new KVTaskClient(new URL("http://localhost:" + KVServer.PORT + "/register"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    public HttpTaskManager(File newFile, String token) {
        super(newFile);
        try {
            this.url = newFile.toURI().toURL();
            kvClient = new KVTaskClient(new URL("http://localhost:" + KVServer.PORT + "/register"), token);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        kvClient.put("task", gson.toJson(getTasks().values()));
        kvClient.put("subTask", gson.toJson(getSubTasks().values()));
        kvClient.put("epic", gson.toJson(getEpicTasks().values()));
        kvClient.put("history", gson.toJson(getHistory()));

    }

    public void loadFromServer() {
        Type TaskType = new TypeToken<List<Task>>() {
        }.getType();
        Type SubType = new TypeToken<List<SubTask>>() {
        }.getType();
        Type EpicType = new TypeToken<List<Epic>>() {
        }.getType();

        List<Task> newHistoryList = new ArrayList<>();
        JsonElement jsonElementH = JsonParser.parseString(kvClient.load("history"));
        String elementH = jsonElementH.getAsString();
        JsonElement jsonElementH1 = JsonParser.parseString(elementH);
        if (jsonElementH1.isJsonArray()) {
            newHistoryList = gson.fromJson(jsonElementH1, TaskType);
        }

        JsonElement jsonTask = JsonParser.parseString(kvClient.load("task"));
        String strTask = jsonTask.getAsString();
        JsonElement jsonTask1 = JsonParser.parseString(strTask);
        if (jsonTask1.isJsonArray()) {
            List<Task> newTaskList = gson.fromJson(jsonTask1, TaskType);
            newTaskList.forEach(task -> {
                if(!allTasks.containsKey(task.getId())) {
                    allTasks.put(task.getId(), task);
                }
                if (task.getId() == 0) {
                    task.setId(giveId());
                }
                tasks.put(task.getId(), task);
                allSortTasks.add(task);
            });

            JsonElement jsonEpic = JsonParser.parseString(kvClient.load("epic"));
            String strEpic = jsonEpic.getAsString();
            JsonElement jsonEpic1 = JsonParser.parseString(strEpic);
            if (jsonEpic1.isJsonArray()) {
                List<Epic> newEpicList = gson.fromJson(jsonEpic1, EpicType);
                newEpicList.forEach(epic -> {
                    if(!allTasks.containsKey(epic.getId())) {
                        allTasks.put(epic.getId(), epic);
                    } else {
                        throw new IdRepitException("Задача с таким ID уже существует.");
                    }
                    if (epic.getId() == 0) {
                        epic.setId(giveId());
                    }
                    epicId = id;
                    epicTasks.put(epic.getId(), epic);
                });
            }

            JsonElement jsonSub = JsonParser.parseString(kvClient.load("subTask"));
            String strSub = jsonSub.getAsString();
            JsonElement jsonSub1 = JsonParser.parseString(strSub);
            if (jsonSub1.isJsonArray()) {
                List<SubTask> newSubTaskList = gson.fromJson(jsonSub1, SubType);
                newSubTaskList.forEach(subTask -> {
                    if(subTask.getEpicId() == 0) {
                        throw new SubWithoutEpicId("Sub без EpicId.");
                    }
                    if (!epicTasks.containsKey(subTask.getEpicId())) {
                        throw new NoCorrectEpicId("Задача с таким Id не является Epic.");
                    }
                    if (subTask.getId() == 0) {
                        subTask.setId(giveId());
                    }
                    if(!allTasks.containsKey(subTask.getId())) {
                        allTasks.put(subTask.getId(), subTask);
                    } else {
                        throw new IdRepitException("Задача с таким ID уже существует.");
                    }
                    subTasks.put(subTask.getId(), subTask);
                    Epic updateEpic = (Epic) epicTasks.get(subTask.getEpicId());
                    updateEpic.addSubTasksOnEpic(subTask);
                    updateStatusEpic(subTask.getEpicId());
                    epicTasks.put(updateEpic.getId(), updateEpic);
                    allTasks.put(updateEpic.getId(), updateEpic);
                    allSortTasks.add(subTask);
                    updateTimeEpic(subTask);
                });
            }
            newHistoryList.forEach(task -> {
                historyManager.add(allTasks.get(task.getId()));
            });

            /*{
                switch (task.getType()) {
                    case TASK: {
                        allTasks.put(task.getId(), task);
                        tasks.put(task.getId(), task);
                        allSortTasks.add(task);
                        break;
                    }
                    case EPIC: {
                        Epic epic = new Epic((Epic) task, TypeTask.EPIC);
                        allTasks.put(epic.getId(), epic);
                        epicTasks.put(epic.getId(), epic);
                        if (epicId < epic.getId()) {
                            epicId = epic.getId();
                        }
                        break;
                    }
                    case SUBTASK: {
                        SubTask sub = new SubTask((SubTask)task,TypeTask.SUBTASK);
                        allTasks.put(sub.getId(), sub);
                        subTasks.put(sub.getId(), sub);
                        allSortTasks.add(sub);
                        break;
                    }
                }
            });*/

        }
    }
}


