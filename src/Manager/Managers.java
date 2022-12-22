package Manager;

public class Managers {
    public static TaskManager getDefault() {

        return new InMemoryTaskManager();
    }
    public static InMemoryHistoryManager  getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}
