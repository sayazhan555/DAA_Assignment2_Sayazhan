package structures;

import metrics.Metrics;

public class DynamicArray {

    private int[] data;
    private int size;
    private Metrics metrics;


    public DynamicArray() {
        data = new int[10];
        size = 0;
        metrics = new Metrics();
    }

    public void add(int value) {
        if (size == data.length) {
            grow();
        }

        data[size] = value;
        size++;
    }

    private void grow() {
        int[] newData = new int[data.length * 2];

        for (int i = 0; i < data.length; i++) {
            metrics.addStep();
            metrics.addMove();
            newData[i] = data[i];
        }

        data = newData;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        metrics.addStep();
        return data[index];
    }

    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {

            metrics.addStep();
            metrics.addComparison();

            if (data[i] == value) {
                return true;
            }
        }

        return false;
    }

    public void add(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == data.length) {
            grow();
        }

        for (int i = size; i > index; i--) {
            metrics.addStep();
            metrics.addMove();
            data[i] = data[i - 1];
        }

        data[index] = value;
        size++;
    }


    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        metrics.addStep();
        int removedValue = data[index];

        for (int i = index; i < size - 1; i++) {
            metrics.addStep();
            metrics.addMove();
            data[i] = data[i + 1];
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
