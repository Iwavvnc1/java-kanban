package Manager;

import DataTask.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> temporaryHistory = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    private Node linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, null, oldTail);
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
            historyTracker.add((Task) node.data);
            node = node.next;
        }
        return historyTracker;
    }

    private void removeNode(Node node) {
        if (node.equals(head) && node.equals(tail)) {
            head = null;
            tail = null;
            return;
        }
        if (node.equals(head)) {
            if (head.next.equals(tail)) {
                head.data = tail.data;
                head.next = null;
                tail = head;
                temporaryHistory.put(head.data.getId(),head);
            } else {
                head.data = head.next.data;
                head.next = head.next.next;
                head.next.prev = head;
            }

            return;
        } else {
            node.prev.next = node.next;
        }
        if (node.equals(tail)) {
            if (tail.prev.equals(head)) {
                tail.data = head.data;
                tail.prev = null;
                head = tail;
                temporaryHistory.put(tail.data.getId(),tail);
            } else {
                tail.data = tail.prev.data;
                tail.prev = tail.prev.prev;
                tail.prev.next = tail;
            }

        } else {
            node.next.prev = node.prev;
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

    private class Node<Task> {

        private Task data;
        private Node<Task> next;
        private Node<Task> prev;

        private Node(Task data, Node<Task> next, Node<Task> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
