package HttpTaskServer;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.Task;

import KVServer.KVServer;

import Manager.Managers;
import Manager.TaskManager;
import MyException.IdRepitException;
import MyException.NoCorrectEpicId;
import MyException.SubWithoutEpicId;
import MyException.TimeException;
import TypeAdapter.LocalDateTimeAdapter;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        System.out.println("Останавливаем сервер на порту " + PORT);
        server.stop(1);
    }

    public HttpTaskServer() throws IOException, InterruptedException, URISyntaxException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new PostsHandler());
    }

    public static class PostsHandler implements HttpHandler {

        TaskManager taskManager = Managers.getDefault(new File("http://localhost:"
                + KVServer.PORT + "/tasks"));
        private final Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        PostsHandler() throws IOException, InterruptedException, URISyntaxException {
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange, exchange.getRequestURI().getPath(), exchange.getRequestMethod());
            switch (endpoint) {
                case GET_TASKS: {
                    handleGetTask(exchange);
                    break;
                }
                case GET_SUBTASKS: {
                    handleGetSubtask(exchange);
                    break;
                }
                case GET_EPICS: {
                    handleGetEpic(exchange);
                    break;
                }
                case GET_TASK: {
                    try {
                        handleGetTaskOnId(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_SUBTASK: {
                    try {
                        handleGetSubTaskOnId(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_EPIC: {
                    try {
                        handleGetEpicOnId(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case POST_TASK: {
                    try {
                        handlePostTask(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case POST_SUBTASK: {
                    try {
                        handlePostSubtask(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case POST_EPIC: {
                    try {
                        handlePostEpic(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_SORTTASK: {
                    handleGetSortTasks(exchange);
                    break;
                }
                case GET_EPICIDINSUB: {
                    handleGetEpicIdInSub(exchange);
                    break;
                }
                case GET_HISTORY: {
                    handleGetHistory(exchange);
                    break;
                }
                case DELETE_TASKID: {
                    try {
                        handleDeleteTaskOnId(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_SUBTASKID: {
                    try {
                        handleDeleteSubTaskOnId(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_EPICID: {
                    try {
                        handleDeleteEpicOnId(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_TASKS: {
                    try {
                        handleDeleteAllTasks(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_EPICS: {
                    try {
                        handleDeleteAllEpics(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_SUBTASKS: {
                    try {
                        handleDeleteAllSubTasks(exchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private void handleDeleteAllEpics(HttpExchange exchange) throws IOException, InterruptedException {
            if (taskManager.deleteAllEpics()) {
                writeResponse(exchange, "Все Epic удалены.", 200);
            } else {
                writeResponse(exchange, "Список задач уже пуст.", 200);
            }
        }

        private void handleDeleteAllSubTasks(HttpExchange exchange) throws IOException, InterruptedException {
            if (taskManager.deleteAllSubTasks()) {
                writeResponse(exchange, "Все Sub удалены.", 200);
            } else {
                writeResponse(exchange, "Список задач уже пуст.", 200);
            }
        }

        private void handleDeleteEpicOnId(HttpExchange exchange) throws IOException, InterruptedException {
            Optional<Integer> idOpt = getId(exchange);
            if (idOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи для удаления.", 400);
                return;
            }
            int taskId = idOpt.get();
            if (taskManager.deleteTask(taskId)) {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " удалена.", 200);
            } else {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " не найдена.", 404);
            }
        }

        private void handleDeleteSubTaskOnId(HttpExchange exchange) throws IOException, InterruptedException {
            Optional<Integer> idOpt = getId(exchange);
            if (idOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи для удаления.", 400);
                return;
            }
            int taskId = idOpt.get();
            if (taskManager.deleteTask(taskId)) {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " удалена.", 200);
            } else {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " не найдена.", 404);
            }
        }

        private void handleGetEpicOnId(HttpExchange exchange) throws IOException, InterruptedException {
            Optional<Integer> idOpt = getId(exchange);
            if (idOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            int taskId = idOpt.get();
            if (taskManager.getAllTasks().containsKey(taskId)) {
                if (taskManager.getEpicTasks().containsKey(taskId)) {
                    writeResponse(exchange, gson.toJson(taskManager.getTask(taskId)), 200);
                } else {
                    writeResponse(exchange, "Задача с идентификатором " + taskId
                            + " не является Epic.", 400);
                }
            } else {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " не найдена.", 404);
            }
        }

        private void handleGetSubTaskOnId(HttpExchange exchange) throws IOException, InterruptedException {
            Optional<Integer> idOpt = getId(exchange);
            if (idOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            int taskId = idOpt.get();
            if (taskManager.getAllTasks().containsKey(taskId)) {
                if (taskManager.getSubTasks().containsKey(taskId)) {
                    writeResponse(exchange, gson.toJson(taskManager.getTask(taskId)), 200);
                } else {
                    writeResponse(exchange, "Задача с идентификатором " + taskId
                            + " не является SubTask.", 400);
                }
            } else {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " не найдена.", 404);
            }
        }

        private void handlePostEpic(HttpExchange exchange) throws IOException, InterruptedException {
            try {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                Epic epic = new Epic(gson.fromJson(body, Epic.class));
                if (epic.getTitle() == null || (epic.getDescription() == null)) {
                    writeResponse(exchange, "Поля Epic не могут быть пустыми", 400);
                }
                if (taskManager.getAllTasks().containsKey(epic.getId())) {
                    taskManager.updateTask(epic);
                    writeResponse(exchange, "Epic обновлен.", 200);
                } else {
                    int id = taskManager.addTask(epic);
                    writeResponse(exchange, "Epic c id " + id
                            + " добавлен.", 201);
                }
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            } catch (IOException e) {
                writeResponse(exchange, "Ошибка сервера.", 500);
            } catch (IdRepitException e) {
                writeResponse(exchange, "Задача с таким id уже существует.", 400);
            } catch (TimeException e) {
                writeResponse(exchange, "Задача пересекается с другой по времени.", 400);
            }
        }

        private void handlePostSubtask(HttpExchange exchange) throws IOException, InterruptedException {
            try {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                SubTask subTask = new SubTask(gson.fromJson(body, SubTask.class));
                if (subTask.getTitle() == null || (subTask.getDescription() == null)) {
                    writeResponse(exchange, "Поля SubTask не могут быть пустыми", 400);
                }
                if (taskManager.getAllTasks().containsKey(subTask.getId())) {
                    taskManager.updateTask(subTask);
                    writeResponse(exchange, "SubTask обновлен.", 200);
                } else {
                    int id = taskManager.addTask(subTask);
                    writeResponse(exchange, "SubTask c id " + id
                            + " добавлен", 201);
                }
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            } catch (IOException e) {
                writeResponse(exchange, "Ошибка сервера.", 500);
            } catch (IdRepitException e) {
                writeResponse(exchange, "Задача с таким id уже существует.", 400);
            } catch (TimeException e) {
                writeResponse(exchange, "Задача пересекается с другой по времени.", 400);
            } catch (SubWithoutEpicId e) {
                writeResponse(exchange, "Sub без Epic.", 400);
            } catch (NoCorrectEpicId e) {
                writeResponse(exchange, "Задача с таким Id не является Epic, либо отсутствует.", 400);
            }
        }

        private void handleDeleteAllTasks(HttpExchange exchange) throws IOException, InterruptedException {
            if (taskManager.deleteAllTasks()) {
                writeResponse(exchange, "Все Task удалены.", 200);
            } else {
                writeResponse(exchange, "Список задач уже пуст.", 200);
            }
        }

        private void handleDeleteTaskOnId(HttpExchange exchange) throws IOException, InterruptedException {
            Optional<Integer> idOpt = getId(exchange);
            if (idOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи для удаления.", 400);
                return;
            }
            int taskId = idOpt.get();
            if (taskManager.deleteTask(taskId)) {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " удалена.", 200);
            } else {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " не найдена.", 404);
            }
        }

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            if (taskManager.getHistory().size() == 0) {
                writeResponse(exchange, "История задач пуста.", 404);
            } else {
                writeResponse(exchange, gson.toJson(taskManager.getHistory()), 200);
            }
        }

        private void handleGetEpicIdInSub(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getId(exchange);
            if (idOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            int taskId = idOpt.get();
            if (taskManager.getEpicTasks().containsKey(taskId)) {
                Epic epic = (Epic) taskManager.getEpicTasks().get(taskId);
                if (epic.getIdSubTasks().size() != 0) {
                    writeResponse(exchange, gson.toJson(epic.getIdSubTasks()), 200);
                } else {
                    writeResponse(exchange, "У Epic c id " + taskId
                            + " нет Sub.", 400);
                }
            } else {
                writeResponse(exchange, "Epic с идентификатором " + taskId
                        + " не найдена.", 404);
            }
        }

        private void handleGetSortTasks(HttpExchange exchange) throws IOException {
            if (taskManager.getPrioritizedTasks().size() == 0) {
                writeResponse(exchange, "Список отсортированных задач пуст.", 404);
            } else {
                writeResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
            }
        }

        private void handlePostTask(HttpExchange exchange) throws IOException, InterruptedException {
            try {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                Task task = new Task(gson.fromJson(body, Task.class));
                if (task.getTitle() == null || (task.getDescription() == null)) {
                    writeResponse(exchange, "Поля Task не могут быть пустыми", 400);
                }
                if (taskManager.getAllTasks().containsKey(task.getId())) {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Task обновлен.", 200);
                } else {
                    int id = taskManager.addTask(task);
                    writeResponse(exchange, "Task c id " + id
                            + " добавлен", 201);
                }
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            } catch (IOException e) {
                writeResponse(exchange, "Ошибка сервера.", 500);
            } catch (IdRepitException e) {
                writeResponse(exchange, "Задача с таким id уже существует.", 400);
            } catch (TimeException e) {
                writeResponse(exchange, "Задача пересекается с другой по времени.", 400);
            }
        }

        private void handleGetTaskOnId(HttpExchange exchange) throws IOException, InterruptedException {
            Optional<Integer> idOpt = getId(exchange);
            if (idOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            int taskId = idOpt.get();
            if (taskManager.getAllTasks().containsKey(taskId)) {
                if (taskManager.getTasks().containsKey(taskId)) {
                    writeResponse(exchange, gson.toJson(taskManager.getTask(taskId)), 200);
                } else {
                    writeResponse(exchange, "Задача с идентификатором " + taskId
                            + " не является Task.", 400);
                }
            } else {
                writeResponse(exchange, "Задача с идентификатором " + taskId
                        + " не найдена.", 404);
            }
        }

        private void handleGetEpic(HttpExchange exchange) throws IOException {
            if (taskManager.getEpicTasks().size() == 0) {
                writeResponse(exchange, "Список Epic задач пуст.", 404);
            } else {
                writeResponse(exchange, gson.toJson(taskManager.getEpicTasks()), 200);
            }
        }

        private void handleGetSubtask(HttpExchange exchange) throws IOException {
            if (taskManager.getSubTasks().size() == 0) {
                writeResponse(exchange, "Список Subtask задач пуст.", 404);
            } else {
                writeResponse(exchange, gson.toJson(taskManager.getSubTasks()), 200);
            }
        }

        private void handleGetTask(HttpExchange exchange) throws IOException {
            if (taskManager.getTasks().size() == 0) {
                writeResponse(exchange, "Список Task задач пуст.", 404);
            } else {
                writeResponse(exchange, gson.toJson(taskManager.getTasks()), 200);
            }
        }

        private Optional<Integer> getId(HttpExchange exchange) {
            String[] pathParts = exchange.getRequestURI().getQuery().split("=");
            try {
                return Optional.of(Integer.parseInt(pathParts[1]));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }

        }

        private Endpoint getEndpoint(HttpExchange exchange, String requestPath, String requestMethod) {
            String[] pathParts = requestPath.split("/");
            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                return Endpoint.GET_SORTTASK;
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && pathParts[2].equals("history")) {
                return Endpoint.GET_HISTORY;
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") &&
                    (pathParts[2].equals("task") || pathParts[2].equals("subtask") || pathParts[2].equals("epic"))
                    && exchange.getRequestURI().getQuery() != null
                    && exchange.getRequestURI().getQuery().contains("id=")) {
                if (requestMethod.equals("GET")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.GET_TASK;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.GET_SUBTASK;
                    }
                    if (pathParts[2].equals("epic")) {
                        return Endpoint.GET_EPIC;
                    }
                }
                if (requestMethod.equals("DELETE")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.DELETE_TASKID;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.DELETE_SUBTASKID;
                    }
                    if (pathParts[2].equals("epic")) {
                        return Endpoint.DELETE_EPICID;
                    }
                }
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") &&
                    (pathParts[2].equals("task") || pathParts[2].equals("subtask") || pathParts[2].equals("epic"))) {

                if (requestMethod.equals("POST") && pathParts[2].equals("task")) {
                    return Endpoint.POST_TASK;
                }
                if (requestMethod.equals("POST") && pathParts[2].equals("subtask")) {
                    return Endpoint.POST_SUBTASK;
                }
                if (requestMethod.equals("POST")) {
                    return Endpoint.POST_EPIC;
                }
                if (requestMethod.equals("GET") && pathParts[2].equals("task")) {
                    return Endpoint.GET_TASKS;
                }
                if (requestMethod.equals("GET") && pathParts[2].equals("subtask")) {
                    return Endpoint.GET_SUBTASKS;
                }
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_EPICS;
                }
                if (requestMethod.equals("DELETE")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.DELETE_TASKS;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.DELETE_SUBTASKS;
                    }
                    return Endpoint.DELETE_EPICS;
                }
            }
            if (pathParts.length == 4 && pathParts[1].equals("tasks") &&
                    pathParts[2].equals("subtask") && pathParts[3].equals("epic")) {
                return Endpoint.GET_EPICIDINSUB;
            }
            return Endpoint.UNKNOWN;
        }

        private static void writeResponse(HttpExchange exchange,
                                          String responseString,
                                          int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }


        enum Endpoint {
            GET_TASKS, GET_SUBTASKS, GET_EPICS, GET_TASK, GET_SUBTASK, GET_EPIC, GET_EPICIDINSUB, GET_SORTTASK,
            GET_HISTORY, POST_TASK, POST_SUBTASK, POST_EPIC, DELETE_TASKID, DELETE_SUBTASKID, DELETE_EPICID,
            DELETE_TASKS, DELETE_SUBTASKS, DELETE_EPICS, UNKNOWN
        }
    }
}