package Manager;

public class Managers {
    public static TaskManager getDefault() { //меня зовут Иван, Игорь это случайное имя в аккаунте

        return new InMemoryTaskManager();
    }
    public static InMemoryHistoryManager  getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}
