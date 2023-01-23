package DataTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Epic extends Task {

    private List<Integer> idSubTasks = new ArrayList<>();

    public Epic(int id, String title, String description) {
        super(id, title, description);
    }

    public List<Integer> getIdSubTasks() {

        return idSubTasks;
    }

    public void addSubTasksOnEpic(int idSubtask) {

        idSubTasks.add(idSubtask);
    }

    public void removeTasksOnEpic(int idSubtask) {

        Iterator<Integer> idIterator = idSubTasks.iterator();
        while (idIterator.hasNext()) {
            Integer nextId = idIterator.next();
            if (nextId == idSubtask) {
                idIterator.remove();
            }
        }
    }
}
