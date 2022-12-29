package Manager;

import DataTask.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    int limitTheHistory = 9;
    List<Task> historyTracker = new LinkedList<>();

    @Override
    public void add(Task task) {

        if (historyTracker.size() > limitTheHistory) {
            historyTracker.remove(0);
        }
        historyTracker.add(task);
    }

    @Override

    public List<Task> getHistory() {

        return historyTracker;
    }
}
