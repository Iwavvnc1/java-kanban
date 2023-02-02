package DataTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Epic extends Task {

    private List<Integer> idSubTasks = new ArrayList<>();
    private TypeTask type;

    public Epic(int id, String title, String description) {
        super(id, title, description);
        type = TypeTask.EPIC;
    }

    @Override
    public TypeTask getType() {
        return type;
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        type = TypeTask.EPIC;
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
    @Override
    public String toString() {
        return getId() + ",," + type + ",," + getTitle() + ",," + getStatus() + ",,"
                + getDescription() + ",," + "\n";
    }
}
