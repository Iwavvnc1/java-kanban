

public class Task extends FatherTask {

    private String description;

    public Task(int id, String title, Status status, String description) {
        super(id, title, status);
        this.description = description;
        status = Status.NEW;
    }
}
