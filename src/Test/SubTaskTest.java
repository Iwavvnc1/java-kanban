package Test;

import DataTask.SubTask;
import DataTask.Task;
import DataTask.TypeTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    private SubTask sub;
    @BeforeEach
    public void beforeEach() {
        sub = new SubTask(0, "name", "description", 1);
    }

    @Test
    void getEpicId() {
        assertEquals(1, sub.getEpicId(), "Не верно выводится EpicId в Sub");
    }

    @Test
    void getType() {
        assertEquals(TypeTask.SUBTASK, sub.getType(), "Не верно выводится TYPE");
    }

}