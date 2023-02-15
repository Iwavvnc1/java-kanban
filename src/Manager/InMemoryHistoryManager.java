package Manager;

import DataTask.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> temporaryHistory = new HashMap<>();
    private Node head;
    private Node tail;

    private Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(task, null, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> historyTracker = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyTracker.add(node.data);
            node = node.next;
        }
        return historyTracker;
    }

    private void removeNode(Node node) {
        if (node != null) {
            temporaryHistory.remove(node.data.getId());
            if (node == head && node == tail) {
                head = null;
                tail = null;
                return;
            }
            if (node == head) {
                if (head.next == tail) {
                    tail.prev = null;
                    head = tail;
                } else {
                    head = head.next;
                    head.prev = null;
                }
                return;
            } else {
                node.prev.next = node.next;
            }
            if (node == tail) {
                if (tail.prev == head) {
                    head.next = null;
                    tail = head;
                } else {
                    tail = tail.prev;
                    tail.next = null;
                }
            } else {
                node.next.prev = node.prev;
            }
        }
    }

    @Override
    public void add(Task task) {
        if (temporaryHistory.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node newNode = linkLast(task);
        temporaryHistory.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(temporaryHistory.remove(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(temporaryHistory, that.temporaryHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temporaryHistory);
    }

    private class Node {

        private Task data;
        private Node next;
        private Node prev;

        private Node(Task data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
