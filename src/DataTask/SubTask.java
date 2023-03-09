package DataTask;

import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public int getEpicId() {

        return epicId;
    }

    public SubTask(String title, String description, int epicId) {
        super(title, description, TypeTask.SUBTASK);
        this.epicId = epicId;

    }

    public SubTask(int id, String title, String description, int epicId) {
        super(id, title, description, TypeTask.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId, Status status) {
        super(title, description, TypeTask.SUBTASK, status);
        this.epicId = epicId;
    }

    public SubTask(int id, String title, String description, int epicId, Status status) {
        super(id, title, description, TypeTask.SUBTASK, status);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId, LocalDateTime startTime, Integer duration) {
        super(title, description, TypeTask.SUBTASK, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId, Status status,
                   LocalDateTime startTime, Integer duration) {
        super(title, description, TypeTask.SUBTASK, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(int id, String title, String description, int epicId, Status status,
                   LocalDateTime startTime, Integer duration) {
        super(id, title, description, TypeTask.SUBTASK, status, startTime, duration);
        this.epicId = epicId;
    }
    public SubTask(SubTask sub, TypeTask type) {
        super(sub,type);
        this.epicId = sub.getEpicId();
    }
    /*public <T> SubTask(T fromJson) {
        super(fromJson);
            this.endTime = ((SubTask) fromJson).getEndTime();
            this.epicId = ((SubTask) fromJson).epicId;
        }*/

    @Override
    public String toString() {
        return getId() + ",," + getType() + ",," + getTitle() + ",," + getStatus() + ",,"
                + getDescription() + ",,";
    }
}
