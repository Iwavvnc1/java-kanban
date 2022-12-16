public class FatherTask {
    private int id;
    private String title;
    Status status;

    public FatherTask(String title) {

        this.title = title;
        status = Status.NEW;
    }

    public int getId() {

        return id;
    }

    public Status getStatus() {

        return status;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setStatus(Status status) {

        this.status = status;
    }
}

