package DataTask;

public class Task {

    private final int id;
    private final String title;
    private final String description;
    private Status status;
    private final TypeTask type;

    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeTask.TASK;
    }

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        status = Status.NEW;
        type = TypeTask.TASK;
    }

    public int getId() {

        return id;
    }

    public Status getStatus() {

        return status;
    }

    public TypeTask getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    @Override
    public String toString() {
        return id + ",," + type + ",," + title + ",," + status + ",,"
                + description + ",," + "\n";
    }
}
