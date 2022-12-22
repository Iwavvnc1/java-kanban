package Manager;

import DataTask.Task;

import java.util.List;


public interface HistoryManager {

    public void updateHistory(Task task);

    public List<Task> getHistory();
}
