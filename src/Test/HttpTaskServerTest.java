package Test;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;
import HttpTaskServer.HttpTaskServer;
import KVServer.KVServer;
import Manager.InMemoryTaskManager;
import TypeAdapter.LocalDateTimeAdapter;
import TypeAdapter.LocalDateTimeDeserializer;
import TypeAdapter.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    protected HttpTaskServer server;
    protected KVServer kvserver;
    protected InMemoryTaskManager manager;
    protected HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    protected final Gson gson = new GsonBuilder()
            .serializeNulls()
          //  .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
          //  .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    Task task1 = new Task("task1", "task1Description",
            LocalDateTime.of(2012, 3, 11, 10, 0), 10);
    Task task2 = new Task("task2", "task2Description",
            LocalDateTime.of(2012, 3, 11, 10, 15), 10);
    Epic epic1 = new Epic("epic1", "epic1Description");
    Epic epic2 = new Epic("epic2", "epic2Description");
    SubTask subtask1 = new SubTask("subtask1", "subtask1Description", epic1.getId(),
            LocalDateTime.of(2012, 3, 11, 10, 30), 10);
    SubTask subtask2 = new SubTask("subtask1", "subtask1Description", epic1.getId(),
            LocalDateTime.of(2012, 3, 11, 10, 45), 10);
    URI urlT = URI.create("http://localhost:8080/tasks/task/");
    URI urlS = URI.create("http://localhost:8080/tasks/subtask/");
    URI urlE = URI.create("http://localhost:8080/tasks/epic/");
    URI urlGT = URI.create("http://localhost:8080/tasks/task?id=" + task1.getId());
    URI urlGT2 = URI.create("http://localhost:8080/tasks/task?id=" + task2.getId());
    URI urlGS = URI.create("http://localhost:8080/tasks/subtask?id=" + subtask1.getId());
    URI urlGE = URI.create("http://localhost:8080/tasks/epic?id=" + epic1.getId());
    URI urlH = URI.create("http://localhost:8080/tasks/history/");
    URI urlGPT = URI.create("http://localhost:8080/tasks/");
    URI urlGSE = URI.create("http://localhost:8080/tasks/subtask/epic?id=" + epic1.getId());

    HttpTaskServerTest() throws IOException, InterruptedException {
    }

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        manager = new InMemoryTaskManager();
        kvserver = new KVServer();
        kvserver.start();
        server = new HttpTaskServer();
        server.start();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
        kvserver.stop();
    }

    @Test
    void GET_TASKS() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);
        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlT).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json1 = gson.toJson(task2);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request2 = HttpRequest.newBuilder().uri(urlT).POST(body1).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlT).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String task1s = gson.toJson(manager.getTasks());
        assertEquals(task1s, response.body());
        assertEquals(200, response.statusCode());

    }

    @Test
    void GET_SUBTASKS() throws IOException, InterruptedException {
        manager.addTask(epic1);
        manager.addTask(subtask1);
        manager.addTask(subtask2);
        String jsonE = gson.toJson(epic1);
        final HttpRequest.BodyPublisher bodyE = HttpRequest.BodyPublishers.ofString(jsonE);
        HttpRequest requestE = HttpRequest.newBuilder().uri(urlE).POST(bodyE).build();
        HttpResponse<String> responseE = client.send(requestE, HttpResponse.BodyHandlers.ofString());
        String json = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlS).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json1 = gson.toJson(subtask2);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request2 = HttpRequest.newBuilder().uri(urlS).POST(body1).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlS).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtask1s = gson.toJson(manager.getSubTasks());
        assertEquals(subtask1s, response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void GET_EPICS() throws IOException, InterruptedException {
        manager.addTask(epic1);
        manager.addTask(epic2);
        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlE).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json1 = gson.toJson(epic2);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request2 = HttpRequest.newBuilder().uri(urlE).POST(body1).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlE).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epic1s = gson.toJson(manager.getEpicTasks());
        assertEquals(epic1s, response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void GET_TASK() throws IOException, InterruptedException {
        manager.addTask(task1);
        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlT).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlGT).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String task1s = gson.toJson(manager.getTask(task1.getId()));
        assertEquals(task1s, response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void GET_SUBTASK() throws IOException, InterruptedException {
        manager.addTask(epic1);
        manager.addTask(subtask1);
        String jsonE = gson.toJson(epic1);
        final HttpRequest.BodyPublisher bodyE = HttpRequest.BodyPublishers.ofString(jsonE);
        HttpRequest requestE = HttpRequest.newBuilder().uri(urlE).POST(bodyE).build();
        HttpResponse<String> responseE = client.send(requestE, HttpResponse.BodyHandlers.ofString());

        String json = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlS).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request = HttpRequest.newBuilder().uri(urlGS).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtask1s = gson.toJson(manager.getTask(subtask1.getId()));
        assertEquals(subtask1s, response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void GET_EPIC() throws IOException, InterruptedException {
        manager.addTask(epic1);
        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlE).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlGE).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epic1s = gson.toJson(manager.getTask(epic1.getId()));
        assertEquals(epic1s, response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void POST_TASK() throws IOException, InterruptedException {
        manager.addTask(task1);
        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlT).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals("Task c id " + task1.getId()
                + " добавлен", response1.body());
        assertEquals(201, response1.statusCode());
        String jsonUPDT = gson.toJson(task1);
        final HttpRequest.BodyPublisher bodyUPDT = HttpRequest.BodyPublishers.ofString(jsonUPDT);
        HttpRequest requestUPDT = HttpRequest.newBuilder().uri(urlT).POST(bodyUPDT).build();
        HttpResponse<String> responseUPDT = client.send(requestUPDT, HttpResponse.BodyHandlers.ofString());
        assertEquals("Task обновлен.", responseUPDT.body());
        assertEquals(200, responseUPDT.statusCode());
    }

    @Test
    void POST_SUBTASK() throws IOException, InterruptedException {
        manager.addTask(epic1);
        manager.addTask(subtask1);
        String jsonE = gson.toJson(epic1);
        final HttpRequest.BodyPublisher bodyE = HttpRequest.BodyPublishers.ofString(jsonE);
        HttpRequest requestE = HttpRequest.newBuilder().uri(urlE).POST(bodyE).build();
        HttpResponse<String> responseE = client.send(requestE, HttpResponse.BodyHandlers.ofString());
        String json = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlS).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals("SubTask c id " + subtask1.getId()
                + " добавлен", response1.body());
        assertEquals(201, response1.statusCode());
        String jsonUPDT = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher bodyUPDT = HttpRequest.BodyPublishers.ofString(jsonUPDT);
        HttpRequest requestUPDT = HttpRequest.newBuilder().uri(urlS).POST(bodyUPDT).build();
        HttpResponse<String> responseUPDT = client.send(requestUPDT, HttpResponse.BodyHandlers.ofString());
        assertEquals("SubTask обновлен.", responseUPDT.body());
        assertEquals(200, responseUPDT.statusCode());
    }

    @Test
    void POST_EPIC() throws IOException, InterruptedException {
        manager.addTask(epic1);
        String json = gson.toJson(epic1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlE).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals("Epic c id " + epic1.getId()
                + " добавлен.", response1.body());
        assertEquals(201, response1.statusCode());
        String jsonUPDT = gson.toJson(epic1);
        final HttpRequest.BodyPublisher bodyUPDT = HttpRequest.BodyPublishers.ofString(jsonUPDT);
        HttpRequest requestUPDT = HttpRequest.newBuilder().uri(urlE).POST(bodyUPDT).build();
        HttpResponse<String> responseUPDT = client.send(requestUPDT, HttpResponse.BodyHandlers.ofString());
        assertEquals("Epic обновлен.", responseUPDT.body());
        assertEquals(200, responseUPDT.statusCode());
    }

    @Test
    void GET_SORTTASK() throws IOException, InterruptedException {
        manager.addTask(task2);
        manager.addTask(task1);
        String json = gson.toJson(task2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlT).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json1 = gson.toJson(task1);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request2 = HttpRequest.newBuilder().uri(urlT).POST(body1).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlGPT).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String task1s = gson.toJson(manager.getPrioritizedTasks());
        assertEquals(task1s, response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void GET_EPICIDINSUB() throws IOException, InterruptedException {
        manager.addTask(epic1);
        manager.addTask(subtask1);
        manager.addTask(subtask2);
        String jsonE = gson.toJson(epic1);
        final HttpRequest.BodyPublisher bodyE = HttpRequest.BodyPublishers.ofString(jsonE);
        HttpRequest requestE = HttpRequest.newBuilder().uri(urlE).POST(bodyE).build();
        HttpResponse<String> responseE = client.send(requestE, HttpResponse.BodyHandlers.ofString());
        String json = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlS).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        String json1 = gson.toJson(subtask2);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request2 = HttpRequest.newBuilder().uri(urlS).POST(body1).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlGSE).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtask1s = gson.toJson(epic1.getIdSubTasks());
        assertEquals(subtask1s, response.body());
        assertEquals(200, response.statusCode());

    }

    @Test
    void GET_HISTORY() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);
        String jsonT1 = gson.toJson(task1);
        final HttpRequest.BodyPublisher bodyT1 = HttpRequest.BodyPublishers.ofString(jsonT1);
        HttpRequest requestT1 = HttpRequest.newBuilder().uri(urlT).POST(bodyT1).build();
        HttpResponse<String> responseT1 = client.send(requestT1, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestGETT2 = HttpRequest.newBuilder().uri(urlGT2).GET().build();
        HttpResponse<String> responseGETT2 = client.send(requestGETT2, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestH = HttpRequest.newBuilder().uri(urlH).GET().build();
        String jsonT2 = gson.toJson(task2);
        final HttpRequest.BodyPublisher bodyT2 = HttpRequest.BodyPublishers.ofString(jsonT2);
        HttpRequest requestT2 = HttpRequest.newBuilder().uri(urlT).POST(bodyT2).build();
        HttpResponse<String> responseT2 = client.send(requestT2, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestGETT1 = HttpRequest.newBuilder().uri(urlGT).GET().build();
        HttpResponse<String> responseGETT1 = client.send(requestGETT1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> responseH = client.send(requestH, HttpResponse.BodyHandlers.ofString());
        manager.getTask(task1.getId());
        String history = gson.toJson(manager.getHistory());
        assertEquals(history,responseH.body());
        assertEquals(200, responseH.statusCode());
    }

    @Test
    void DELETE_TASKID() throws IOException, InterruptedException {
        String json = gson.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlT).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().uri(urlGT).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("Задача с идентификатором " + task1.getId()
                + " удалена.", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestr = HttpRequest.newBuilder().uri(urlT).GET().build();
        HttpResponse<String> responser = client.send(requestr, HttpResponse.BodyHandlers.ofString());
        assertEquals("Список Task задач пуст.", responser.body());
        assertEquals(404, responser.statusCode());
    }

    @Test
    void DELETE_SUBTASKID() throws IOException, InterruptedException {
        String jsonE = gson.toJson(epic1);
        final HttpRequest.BodyPublisher bodyE = HttpRequest.BodyPublishers.ofString(jsonE);
        HttpRequest requestE = HttpRequest.newBuilder().uri(urlE).POST(bodyE).build();
        HttpResponse<String> responseE = client.send(requestE, HttpResponse.BodyHandlers.ofString());

        String json = gson.toJson(subtask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(urlS).POST(body).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request = HttpRequest.newBuilder().uri(urlGS).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("Задача с идентификатором " + subtask1.getId()
                + " удалена.", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestr = HttpRequest.newBuilder().uri(urlGS).GET().build();
        HttpResponse<String> responser = client.send(requestr, HttpResponse.BodyHandlers.ofString());
        assertEquals("Список SubTask задач пуст.", responser.body());
        assertEquals(404, responser.statusCode());
    }

    @Test
    void DELETE_EPICID() {
    }

    @Test
    void DELETE_TASKS() {
    }

    @Test
    void DELETE_SUBTASKS() {
    }

    @Test
    void DELETE_EPICS() {
    }
}