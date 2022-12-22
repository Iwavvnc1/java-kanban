package Manager;

import DataTask.Task;

import java.util.List;


public interface HistoryManager {

    void updateHistory(Task task);

    List<Task> getHistory();
}
