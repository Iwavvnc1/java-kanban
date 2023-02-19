package DataTask;

import Manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Task implements Comparable<Task> {

    private final int id;
    private final String title;
    private final String description;
    private Status status;
    private final TypeTask type;
    private Duration duration;
    private LocalDateTime startTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");


    public Task(String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeTask.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }
    public Task(int id,String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeTask.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, LocalDateTime startTime, Duration duration) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        status = Status.NEW;
        type = TypeTask.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }
    public Task(String title, String description, Status status) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeTask.TASK;
    }
    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeTask.TASK;
    }

    public Task(String title, String description) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        status = Status.NEW;
        type = TypeTask.TASK;
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
    public int getEpicId() {
        return 0;
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime.adjustInto(startTime);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String toString() {
        return id + ",," + type + ",," + title + ",," + status + ",,"
                + description + ",,";
    }

    @Override
    public int compareTo(Task task) {
        if (task.getStartTime() == null) {
            return -1;
        }
        if(this.getStartTime() == null) {
            return 1;

        }
        return this.getStartTime().compareTo(task.getStartTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
