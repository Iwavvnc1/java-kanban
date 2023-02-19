package Test;

import DataTask.Epic;
import DataTask.SubTask;
import DataTask.TypeTask;

import Manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
   private Epic epic;
   private SubTask sub1;
   private SubTask sub2;
    @BeforeEach
    public void beforeEach() {
        epic = new Epic( "name", "description");
        sub1 = new SubTask( "name", "description", epic.getId());
        sub2 = new SubTask( "name", "description", epic.getId());
    }
    @Test
    void getType() {
        assertEquals(TypeTask.EPIC, epic.getType(), "Не верно выводится TYPE");
    }

    @Test
    void getIdSubTasks() {
        epic.addSubTasksOnEpic(sub2);
        assertEquals(sub2, epic.getIdSubTasks().get(0), "Не верно выводится idSub из Epic");
    }

    @Test
    void addSubTasksOnEpic() {
        epic.addSubTasksOnEpic(sub2);
        assertEquals(sub2, epic.getIdSubTasks().get(0), "Не верно добавляется idSub в Epic");
    }

    @Test
    void removeTasksOnEpic() {
        epic.addSubTasksOnEpic(sub1);
        epic.addSubTasksOnEpic(sub2);
        epic.removeTasksOnEpic(sub1);
        assertEquals(sub2, epic.getIdSubTasks().get(0), "Не верно удаляется idSub в Epic");
    }
}