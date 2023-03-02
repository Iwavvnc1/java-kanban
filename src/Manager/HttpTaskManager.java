package Manager;


import DataTask.Task;
import KVServer.KVServer;
import KVTaskClient.KVTaskClient;

import TypeAdapter.LocalDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class HttpTaskManager extends FileBackedTasksManager {

    protected URL url;
    KVTaskClient kvClient;

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskManager(File newFile) throws IOException, InterruptedException {
        super(newFile);
        this.url = newFile.toURI().toURL();
        kvClient = new KVTaskClient(new URL("http://localhost:" + KVServer.PORT + "/register"));
    }

    public HttpTaskManager(File newFile, String token) throws IOException {
        super(newFile);
        this.url = newFile.toURI().toURL();
        kvClient = new KVTaskClient(new URL("http://localhost:" + KVServer.PORT + "/register"), token);
    }

    @Override
    public void save() throws IOException, InterruptedException {
        List<Task> taskList = new ArrayList<>(getAllTasks().values());
        kvClient.put("allTask", gson.toJson(taskList));
        kvClient.put("history", gson.toJson(getHistory()));
    }

    public void loadFromServer() throws IOException, InterruptedException {
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        JsonElement jsonElement = JsonParser.parseString(kvClient.load("allTask"));
        String str = jsonElement.getAsString();
        JsonElement jsonElement1 = JsonParser.parseString(str);
        if (jsonElement1.isJsonArray()) {
            List<Task> newTaskList = gson.fromJson(jsonElement1, listType);
            for (Task task : newTaskList) {
                switch (task.getType()) {
                    case TASK: {
                        allTasks.put(task.getId(), task);
                        tasks.put(task.getId(), task);
                        allSortTasks.add(task);
                        break;
                    }
                    case EPIC: {
                        allTasks.put(task.getId(), task);
                        epicTasks.put(task.getId(), task);
                        if (epicId < task.getId()) {
                            epicId = task.getId();
                        }
                        break;
                    }
                    case SUBTASK: {
                        allTasks.put(task.getId(), task);
                        subTasks.put(task.getId(), task);
                        allSortTasks.add(task);
                        break;
                    }
                }
            }
        }

        JsonElement jsonElementH = JsonParser.parseString(kvClient.load("history"));
        String elementH = jsonElementH.getAsString();
        JsonElement jsonElementH1 = JsonParser.parseString(elementH);
        if (jsonElementH1.isJsonArray()) {
            List<Task> newTaskList = gson.fromJson(jsonElementH1, listType);
            for (Task task : newTaskList) {
                historyManager.add(task);
            }
        }
    }
}

