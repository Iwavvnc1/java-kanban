package Manager;

public class Managers {
    public static TaskManager getDefault() { /*меня зовут Иван, Игорь это случайное имя в аккаунте,
  и с наступающим новым годом)*/
        return new InMemoryTaskManager();
    }
    public static InMemoryHistoryManager  getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}
