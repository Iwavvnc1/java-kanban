package Manager;


import KVServer.KVServer;
import KVTaskClient.KVTaskClient;

import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient kvClient = new KVTaskClient(new URL("http://localhost:" + KVServer.PORT + "/register"));

   protected URL url;
   Gson gson = new Gson();
    public HttpTaskManager(File newFile) throws IOException, InterruptedException {
        super(newFile);
        this.url = newFile.toURI().toURL();
        //loadFromServer();
    }

    @Override
    public void save() throws IOException, InterruptedException {
        kvClient.put("task",gson.toJson(this.getTasks()));
        kvClient.put("allTask",gson.toJson(this.getAllTasks()));
        kvClient.put("subtask",gson.toJson(this.getSubTasks()));
        kvClient.put("epic",gson.toJson(this.getEpicTasks()));
        kvClient.put("sort",gson.toJson(this.getPrioritizedTasks()));
        kvClient.put("history",gson.toJson(this.historyManager));
    }

    public void loadFromServer() throws IOException, InterruptedException {
        this.historyManager = gson.fromJson(kvClient.load("history"),historyManager.getClass());
        this.getAllTasks().putAll(gson.fromJson(kvClient.load("allTask"), HashMap.class));
        this.getTasks().putAll(gson.fromJson(kvClient.load("task"), HashMap.class));
        this.getEpicTasks().putAll(gson.fromJson(kvClient.load("allTask"), HashMap.class));
        this.getSubTasks().putAll(gson.fromJson(kvClient.load("allTask"), HashMap.class));
        this.getPrioritizedTasks().addAll(gson.fromJson(kvClient.load("sort"), TreeSet.class));

    }
}
