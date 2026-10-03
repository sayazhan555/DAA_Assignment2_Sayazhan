package structures;

import metrics.Metrics;

public class MinHeap {

    private int[] data;
    private int size;
    private Metrics metrics;

    public MinHeap() {
        data = new int[10];
        size = 0;
        metrics = new Metrics();
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

    public void insert(int value) {
        if (size == data.length) {
            grow();
        }

        data[size] = value;
        int current = size;
        size++;

        while (current > 0) {
            int parent = (current - 1) / 2;

            metrics.addComparison();

            if (data[parent] <= data[current]) {
                break;
            }

            int temp = data[parent];
            data[parent] = data[current];
            data[current] = temp;

            metrics.addStep();
            metrics.addStep();
            metrics.addMove();
            metrics.addMove();

            current = parent;
        }

    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        metrics.addStep();
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        metrics.addStep();
        int min = data[0];

        metrics.addStep();
        metrics.addMove();
        data[0] = data[size - 1];

        size--;

        int current = 0;

        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int smallest = current;

            if (left < size) {
                metrics.addComparison();

                if (data[left] < data[smallest]) {
                    smallest = left;
                }
            }

            if (right < size) {
                metrics.addComparison();

                if (data[right] < data[smallest]) {
                    smallest = right;
                }
            }

            if (smallest == current) {
                break;
            }

            int temp = data[current];
            data[current] = data[smallest];
            data[smallest] = temp;

            metrics.addStep();
            metrics.addStep();
            metrics.addMove();
            metrics.addMove();

            current = smallest;
        }

        return min;
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

    public boolean isValidHeap() {
        for (int i = 1; i < size; i++) {
            int parent = (i - 1) / 2;

            if (data[parent] > data[i]) {
                return false;
            }
        }

        return true;
    }

    

}