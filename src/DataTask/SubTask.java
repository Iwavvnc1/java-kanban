package DataTask;

public class SubTask extends Task {
   private final int epicId;

    public int getEpicId() {

        return epicId;
    }

    public SubTask(int id, String title, String description, int epicId) {
        super(id, title, description);
        this.epicId = epicId;
    }
}
