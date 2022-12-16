import java.util.ArrayList;
import java.util.Iterator;

public class Epic extends FatherTask {

    private ArrayList<Integer> idSubTasks = new ArrayList<>();

    public Epic(String title) {

        super(title);
    }

    public ArrayList<Integer> getIdSubTasks() {

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
