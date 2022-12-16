public class SubTask extends FatherTask {
   private int epicId;
   private String description;
   Status status;

    public int getEpicId() {

        return epicId;
    }

    public SubTask(String title, int epicId, String description) {

        super(title);
        this.epicId = epicId;
        this.description = description;
    }
}
