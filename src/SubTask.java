public class SubTask extends FatherTask {
   private int epicId;
   private String description;

    public int getEpicId() {
        return epicId;
    }

    public SubTask(int id, String title, Status status, int epicId, String description) {
        super(id, title, status);
        this.epicId = epicId;
        this.description = description;
        status = Status.NEW;
    }
}
