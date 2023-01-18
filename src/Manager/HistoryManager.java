package Manager;

import DataTask.Task;

import java.util.HashMap;
import java.util.List;


public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);

}
