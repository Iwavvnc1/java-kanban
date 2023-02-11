package Test;

import DataTask.Epic;
import DataTask.TypeTask;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void getType() {
        Epic epic = new Epic(0, "name", "description");
        assertEquals(TypeTask.EPIC, epic.getType(), "Не верно выводится TYPE");
    }

    @Test
    void getIdSubTasks() {
        Epic epic = new Epic(0, "name", "description");
        epic.addSubTasksOnEpic(2);
        assertEquals(2, epic.getIdSubTasks().get(0), "Не верно выводится idSub из Epic");
    }

    @Test
    void addSubTasksOnEpic() {
        Epic epic = new Epic(0, "name", "description");
        epic.addSubTasksOnEpic(2);
        assertEquals(2, epic.getIdSubTasks().get(0), "Не верно добавляется idSub в Epic");
    }

    @Test
    void removeTasksOnEpic() {
        Epic epic = new Epic(0, "name", "description");
        epic.addSubTasksOnEpic(2);
        epic.addSubTasksOnEpic(1);
        epic.removeTasksOnEpic(1);
        assertEquals(2, epic.getIdSubTasks().get(0), "Не верно удаляется idSub в Epic");
    }

    @Test
    void testToString() {
        Epic epic = new Epic(0, "name", "description");
        assertEquals("0,,EPIC,,name,,NEW,,description,," + "\n", epic.toString(), "Не верно работает " + "метод epic.toString ");
    }
}