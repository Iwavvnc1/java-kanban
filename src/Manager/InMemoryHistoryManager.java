package Manager;

import DataTask.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    static LinkedList<Task> historyTracker = new LinkedList<>();

    @Override
    public void updateHistory(Task task) {

        if (historyTracker.size() > 9) {
            historyTracker.removeLast();
            historyTracker.addFirst(task);
        } else {
            historyTracker.addFirst(task);
        }
    }

    @Override

    public LinkedList<Task> getHistory() {

        return historyTracker;
    }
}
