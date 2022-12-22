package Manager;

import DataTask.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static List<Task> historyTracker = new ArrayList<>();

    @Override
    public void updateHistory(Task task) {

        if (historyTracker.size() > 9) {
            historyTracker.remove(9);
        }
        historyTracker.add(0, task);
    }

    @Override

    public List<Task> getHistory() {

        return historyTracker;
    }
}
