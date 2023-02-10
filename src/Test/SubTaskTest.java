package Test;

import DataTask.SubTask;
import DataTask.TypeTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void getEpicId() {
        SubTask sub = new SubTask(0, "name", "description", 1);
        assertEquals(1,sub.getEpicId());
    }

    @Test
    void getType() {
        SubTask sub = new SubTask(0,"name","description",1);
        assertEquals(TypeTask.SUBTASK,sub.getType());
    }

    @Test
    void testToString() {
        SubTask sub = new SubTask(0,"name","description",1);
        assertEquals("0,,SUBTASK,,name,,NEW,,description,,1" + "\n",sub.toString());
    }
}