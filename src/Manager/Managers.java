package Manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Managers {
    public static TaskManager getDefault(File file) throws IOException, InterruptedException {
        return new HttpTaskManager(file);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedTasksManager(File file) {
        return new FileBackedTasksManager(file);
    }
}
