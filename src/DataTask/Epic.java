package DataTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Epic extends Task {

    private final List<Task> subTasks = new ArrayList<>();
    private TypeTask type;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        type = TypeTask.EPIC;
    }
    public Epic(int id, String title, String description) {
        super(id,title, description);
        type = TypeTask.EPIC;
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        type = TypeTask.EPIC;
    }
    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        type = TypeTask.EPIC;
    }

    public Epic(String title, String description, LocalDateTime startTime, Duration duration) {
        super(title, description,startTime,duration);
        type = TypeTask.EPIC;
    }

    public Epic(String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(title, description, status,startTime,duration);
        type = TypeTask.EPIC;
    }
    public Epic(int id, String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(id, title, description, status,startTime,duration);
        type = TypeTask.EPIC;
    }

    @Override
    public TypeTask getType() {
        return type;
    }

    public List<Task> getIdSubTasks() {

        return subTasks;
    }

    public void addSubTasksOnEpic(Task task) {

        subTasks.add(task);
    }

    public void removeTasksOnEpic(Task task) {

        subTasks.removeIf(nextTask -> nextTask == task);
    }

    @Override
    public LocalDateTime getStartTime(){
       LocalDateTime epicStarTime = null;
        for (Task subTask : subTasks) {
            if(epicStarTime == null) {
                epicStarTime = subTask.getStartTime();
                continue;
            }
            if (epicStarTime.isAfter(subTask.getStartTime())) {
                epicStarTime = subTask.getStartTime();
            }
        }
        return epicStarTime;
    }
    @Override
    public Duration getDuration() {
        Duration epicDuration = null;
        for (Task subTask : subTasks) {
            if(epicDuration == null) {
                epicDuration = subTask.getDuration();
                continue;
            }
            if(subTask.getDuration() == null) {
                continue;
            }
            epicDuration = epicDuration.plus(subTask.getDuration());
        }
        return epicDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTimeEpic = null;
        for (Task subTask : subTasks) {
            if (endTimeEpic == null) {
                endTimeEpic = subTask.getEndTime();
                continue;
            }
            if(endTimeEpic.isBefore(subTask.getEndTime())) {
                endTimeEpic = subTask.getEndTime();
            }
        }
        return endTimeEpic;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return getId() + ",," + type + ",," + getTitle() + ",," + getStatus() + ",,"
                + getDescription() + ",,";
    }
}
