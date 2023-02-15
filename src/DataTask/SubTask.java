package DataTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;
    private final TypeTask type;

    public int getEpicId() {

        return epicId;
    }

    public SubTask(int id, String title, String description, int epicId) {
        super(id, title, description);
        this.epicId = epicId;
        this.type = TypeTask.SUBTASK;
    }
    public SubTask(int id, String title, String description, int epicId, Status status) {
        super(id, title, description,status);
        this.epicId = epicId;
        this.type = TypeTask.SUBTASK;
    }

    public SubTask(int id, String title, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(id, title, description, startTime, duration);
        this.epicId = epicId;
        this.type = TypeTask.SUBTASK;
    }
    public SubTask(int id, String title, String description, int epicId, Status status,
                   LocalDateTime startTime, Duration duration) {
        super(id, title, description,status, startTime, duration);
        this.epicId = epicId;
        this.type = TypeTask.SUBTASK;
    }

    @Override
    public TypeTask getType() {
        return type;
    }

    @Override
    public String toString() {
        return getId() + ",," + type + ",," + getTitle() + ",," + getStatus() + ",,"
                + getDescription() + ",,";
    }
}
