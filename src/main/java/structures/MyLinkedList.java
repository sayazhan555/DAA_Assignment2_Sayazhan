package structures;

import metrics.Metrics;

public class MyLinkedList {

    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    private Node head;
    private int size;
    private Metrics metrics;

    public MyLinkedList() {
        head = null;
        size = 0;
        metrics = new Metrics();
    }

    public void add(int value) {
        Node newNode = new Node(value);

        if (head == null) {
            head = newNode;
            metrics.addMove();
        } else {
            Node current = head;

            while (current.next != null) {
                current = current.next;
                metrics.addStep();
            }

            current.next = newNode;
            metrics.addMove();
        }

        size++;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
            metrics.addStep();
        }

        return current.value;
    }

    public boolean contains(int value) {
        Node current = head;

        while (current != null) {
            metrics.addComparison();

            if (current.value == value) {
                return true;
            }

            current = current.next;
            metrics.addStep();
        }

        return false;
    }

    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node newNode = new Node(value);

        if (index == 0) {
            newNode.next = head;
            head = newNode;
            metrics.addMove();
            metrics.addMove();
        } else {
            Node current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.next;
                metrics.addStep();
            }

            newNode.next = current.next;
            current.next = newNode;
            metrics.addMove();
            metrics.addMove();
        }

        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        int removedValue;

        if (index == 0) {
            removedValue = head.value;
            head = head.next;
            metrics.addMove();
        } else {
            Node current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.next;
                metrics.addStep();
            }

            removedValue = current.next.value;
            current.next = current.next.next;
            metrics.addMove();
        }

        size--;

        return removedValue;
    }

    public int size() {
        return size;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void resetMetrics() {
        metrics.reset();
    }

}