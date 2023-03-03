package DataTask;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {

    private transient final HashMap<Integer, Task> subTasks = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description, TypeTask.EPIC);
    }

    public Epic(int id, String title, String description) {
        super(id, title, description, TypeTask.EPIC);
    }

    public Epic(String title, String description, Status status) {
        super(title, description, TypeTask.EPIC, status);
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, TypeTask.EPIC, status);
    }

    public Epic(String title, String description, LocalDateTime startTime, Integer duration) {
        super(title, description, TypeTask.EPIC, startTime, duration);
    }

    public Epic(String title, String description, Status status, LocalDateTime startTime, Integer duration) {
        super(title, description, TypeTask.EPIC, status, startTime, duration);
    }

    public Epic(int id, String title, String description, Status status, LocalDateTime startTime, Integer duration) {
        super(id, title, description, TypeTask.EPIC, status, startTime, duration);
    }

    public Epic(Epic epic) {
        super(epic);
    }

   /* public <T> Epic(T fromJson) {
        super(fromJson);
        if (getIdSubTasks().size() != 0) {
            if (((Epic) fromJson).startTime != null) {
                this.startTime = ((Epic) fromJson).getStartTime();
            }
            if (((Epic) fromJson).duration != null) {
                this.duration = ((Epic) fromJson).getDuration();
            }
            if (((Epic) fromJson).endTime != null) {
                this.endTime = ((Epic) fromJson).getEndTime();
            }
        }
    }*/

    public HashMap<Integer, Task> getIdSubTasks() {

        return subTasks;
    }

    public void addSubTasksOnEpic(Task task) {
        subTasks.put(task.getId(), task);
    }

    public void removeTasksOnEpic(Task task) {
        subTasks.remove(task.getId());
    }

    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime epicStarTime = null;
        if (subTasks == null) {
            return null;
        }
        if (subTasks.size() == 0) {
            return null;
        }
        for (Task subTask : subTasks.values()) {
            if (subTask.getStartTime() != null) {
                if (epicStarTime == null) {
                    epicStarTime = subTask.getStartTime();
                    continue;
                }
                if (epicStarTime.isAfter(subTask.getStartTime())) {
                    epicStarTime = subTask.getStartTime();
                }
            }
        }
        startTime = epicStarTime;
        return epicStarTime;
    }

    @Override
    public Integer getDuration() {
        Integer epicDuration = 0;
        if (subTasks == null) {
            return null;
        }
        if (subTasks.size() == 0) {
            return null;
        }
        for (Task subTask : subTasks.values()) {
            if (subTask.getDuration() != null) {
                epicDuration += subTask.getDuration();
            }
        }
        duration = epicDuration;
        return epicDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTimeEpic = null;
        if (subTasks == null) {
            return null;
        }
        if (subTasks.size() == 0) {
            return null;
        }
        for (Task subTask : subTasks.values()) {
            if (subTask.getEndTime() != null) {
                if (endTimeEpic == null) {
                    endTimeEpic = subTask.getEndTime();
                    continue;
                }
                if (endTimeEpic.isBefore(subTask.getEndTime())) {
                    endTimeEpic = subTask.getEndTime();
                }
            }
        }
        endTime = endTimeEpic;
        return endTimeEpic;
    }

    @Override
    public String toString() {
        return getId() + ",," + getType() + ",," + getTitle() + ",," + getStatus() + ",,"
                + getDescription() + ",,";
    }
}
