package Manager;

import java.io.File;


public class Managers {
    public static String getLocalHostKvServer () {
        return "http://localhost:8078/";
    }
    public static String getLocalHostHttpServer () {
        return "http://localhost:8080/";
    }
    public static TaskManager getDefault(File file)  {
        return new HttpTaskManager(file);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedTasksManager(File file) {
        return new FileBackedTasksManager(file);
    }
}
