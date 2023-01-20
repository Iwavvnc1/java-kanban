package Manager;

import DataTask.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> temporaryHistory = new HashMap<>();
    public Node<Task> head;
    public Node<Task> tail;

    public Node linkLast(Task task) {
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

    public List<Task> getTasks() {
        List<Task> historyTracker = new ArrayList<>();
        historyTracker.clear();
        for (Node task : temporaryHistory.values()) {
            historyTracker.add((Task) task.data);
        }
        return historyTracker;
    }

    public void removeNode(Node node) {
        Node nodePrev = node.prev;
        Node nodeNext = node.next;

        if (node.equals(head) && node.equals(tail)) {
            return;
        }

        if (node.equals(head)) {
            head = new Node(nodeNext.data, nodeNext.next, null);
        } else {
            nodePrev.next = nodeNext;
        }

        if (node.equals(tail)) {
            tail = new Node(nodePrev.data, null, nodePrev.prev);
        } else {
            nodeNext.prev = node.prev;
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

    class Node<Task> {

        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Task data, Node<Task> next, Node<Task> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
