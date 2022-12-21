package Manager;

public class Managers {
    public static InMemoryTaskManager getDefault() {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }
    public static InMemoryHistoryManager  getDefaultHistory() {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
