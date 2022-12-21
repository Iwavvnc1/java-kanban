package DataTask;

public class SubTask extends Task {
   private int epicId;

    public int getEpicId() {

        return epicId;
    }

    public SubTask(int id, String title, String description, int epicId) {
        super(id, title, description,Status.NEW);
        this.epicId = epicId;
    }
}