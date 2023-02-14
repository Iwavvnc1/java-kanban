package Test;

import DataTask.SubTask;
import DataTask.TypeTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void getEpicId() {
        SubTask sub = new SubTask(0, "name", "description", 1);
        assertEquals(1, sub.getEpicId(), "Не верно выводится EpicId в Sub");
    }

    @Test
    void getType() {
        SubTask sub = new SubTask(0, "name", "description", 1);
        assertEquals(TypeTask.SUBTASK, sub.getType(), "Не верно выводится TYPE");
    }

}