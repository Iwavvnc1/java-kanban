package DataTask;

import Manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;


public class Task implements Comparable<Task> {

    private int id;
    private String title;
    private String description;
    private Status status;
    private TypeTask type;
    protected Integer duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;


    public Task(String title, String description, Status status, LocalDateTime startTime, Integer duration) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeTask.TASK;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(int id, String title, String description, Status status, LocalDateTime startTime, Integer duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeTask.TASK;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(String title, String description, LocalDateTime startTime, Integer duration) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        status = Status.NEW;
        type = TypeTask.TASK;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
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

    public Task(String title, String description, TypeTask type, Status status, LocalDateTime startTime, Integer duration) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String title, String description, TypeTask type, Status status, LocalDateTime startTime, Integer duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(String title, String description, TypeTask type, LocalDateTime startTime, Integer duration) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        status = Status.NEW;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task(String title, String description, TypeTask type, Status status) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(int id, String title, String description, TypeTask type, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(String title, String description, TypeTask type) {
        id = InMemoryTaskManager.giveId();
        this.title = title;
        this.description = description;
        status = Status.NEW;
        this.type = type;
    }

    public Task(int id, String title, String description, TypeTask type) {
        this.id = id;
        this.title = title;
        this.description = description;
        status = Status.NEW;
        this.type = type;
    }

    public Task(Task task, TypeTask type) {
        if (task.getId() == 0) {
            id = InMemoryTaskManager.giveId();
        } else {
            this.id = task.getId();
        }
        this.title = task.getTitle();
        this.description = task.getDescription();
        if (task.getStatus() == null) {
            status = Status.NEW;
        } else {
            this.status = task.getStatus();
        }
        if(task.getType() == null) {
            this.type = type;
        } else {
            this.type = task.getType();
        }
        if (task.getStartTime() != null) {
            this.startTime = task.getStartTime();
        }
        if (task.getDuration() != null) {
            this.duration = task.getDuration();
        }
        if (task.getEndTime() != null) {
            this.endTime = task.getEndTime();
        }
    }


/*    public <T> Task(T fromJson) {
        if (fromJson.getClass().equals(Task.class)) {
            if(((Task) fromJson).getId() == 0) {
                this.id = InMemoryTaskManager.giveId();
            } else {
                this.id = ((Task) fromJson).getId();
            }
            this.title = ((Task) fromJson).getTitle();
            this.description = ((Task) fromJson).getDescription();
            if (((Task) fromJson).getStatus() == null) {
                this.status = Status.NEW;
            } else {
                this.status = ((Task) fromJson).getStatus();
            }
            this.type = TypeTask.TASK;
            if(((Task) fromJson).getStartTime() != null) {
                this.startTime = ((Task) fromJson).getStartTime();
            }
            if(((Task) fromJson).getDuration() != null) {
                this.duration = ((Task) fromJson).getDuration();
            }
            if (((Task) fromJson).endTime != null) {
                this.endTime = ((Task) fromJson).getEndTime();
            }
        } else if ((fromJson.getClass().equals(SubTask.class))) {
            if(((SubTask) fromJson).getId() == 0) {
                this.id = InMemoryTaskManager.giveId();
            } else {
                this.id = ((SubTask) fromJson).getId();
            }
            this.title = ((SubTask) fromJson).getTitle();
            this.description = ((SubTask) fromJson).getDescription();
            if (((SubTask) fromJson).getStatus() == null) {
                this.status = Status.NEW;
            } else {
                this.status = ((SubTask) fromJson).getStatus();
            }
            this.type = TypeTask.SUBTASK;
            if(((SubTask) fromJson).getStartTime() != null) {
                this.startTime = ((SubTask) fromJson).getStartTime();
            }
            if(((SubTask) fromJson).getDuration() != null) {
                this.duration = ((SubTask) fromJson).getDuration();
            }
        } else if ((fromJson.getClass().equals(Epic.class))) {
            if(((Epic) fromJson).getId() == 0) {
                this.id = InMemoryTaskManager.giveId();
            } else {
                this.id = ((Epic) fromJson).getId();
            }
            this.title = ((Epic) fromJson).getTitle();
            this.description = ((Epic) fromJson).getDescription();
            if (((Epic) fromJson).getStatus() == null) {
                this.status = Status.NEW;
            } else {
                this.status = ((Epic) fromJson).getStatus();
            }
            this.type = TypeTask.EPIC;
        }
    }*/


    public int getId() {

        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if(startTime == null) {
            return null;
        }
        endTime = startTime.plus(Duration.ofMinutes(getDuration()));
        return endTime;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime.adjustInto(startTime);
    }


    @Override
    public String toString() {
        return id + ",," + type + ",," + title + ",," + status + ",,"
                + description + ",,";
    }

    @Override
    public int compareTo(Task task) {
        if (task.getId() == this.getId()) {
            return 0;
        }
        if (task.getStartTime() == null) {
            return -1;
        }
        if (this.getStartTime() == null) {
            return 1;

        }
        return this.getStartTime().compareTo(task.getStartTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status && type == task.type && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, type, duration, startTime, endTime);
    }
}
