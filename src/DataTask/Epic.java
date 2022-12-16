package DataTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Epic extends Task {

    private List<Integer> idSubTasks = new ArrayList<>(); /*List<SubTask> мы это еще не изучали, и на QA сказали тут id
    хранить а не задачи*/

    public Epic(int id, String title, String description) {
        super(id, title, description,Status.NEW);
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
