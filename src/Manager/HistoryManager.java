package Manager;

import DataTask.Task;

import java.util.LinkedList;


public interface HistoryManager {

    public void updateHistory(Task task);

    public LinkedList<Task> getHistory();
}
