package Test;

import DataTask.Epic;
import DataTask.TypeTask;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void getType() {
        Epic epic = new Epic(0, "name", "description");
        assertEquals(TypeTask.EPIC, epic.getType());
    }

    @Test
    void getIdSubTasks() {
        Epic epic = new Epic(0, "name", "description");
        epic.addSubTasksOnEpic(2);
        assertEquals(2, epic.getIdSubTasks().get(0));
    }

    @Test
    void addSubTasksOnEpic() {
        Epic epic = new Epic(0, "name", "description");
        epic.addSubTasksOnEpic(2);
        assertEquals(2, epic.getIdSubTasks().get(0));
    }

    @Test
    void removeTasksOnEpic() {
        Epic epic = new Epic(0, "name", "description");
        epic.addSubTasksOnEpic(2);
        epic.addSubTasksOnEpic(1);
        epic.removeTasksOnEpic(1);
        assertEquals(2, epic.getIdSubTasks().get(0));
    }

    @Test
    void testToString() {
        Epic epic = new Epic(0, "name", "description");
        assertEquals("0,,EPIC,,name,,NEW,,description,," + "\n", epic.toString());
    }
}