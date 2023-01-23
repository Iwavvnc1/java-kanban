package DataTask;

public class Task {

    private final int id;
    private final String title;
    private final String description;
    private Status status;

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        status = Status.NEW;
    }

    public int getId() {

        return id;
    }

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    @Override
    public String toString() {
        return "{'" + title + '\'' +
                '}';
    }
}
