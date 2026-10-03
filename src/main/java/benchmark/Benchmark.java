package benchmark;

import structures.DynamicArray;
import structures.MyLinkedList;
import structures.MinHeap;

import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class Benchmark {

    private static final int[] SIZES = {
            100, 1000, 10000, 100000
    };

    public static void main(String[] args) throws IOException {
        System.out.println("Starting benchmark...");

        File resultsDirectory = new File("results");

        if (!resultsDirectory.exists()) {
            resultsDirectory.mkdir();
        }

        try (FileWriter writer = new FileWriter("results/results.csv")) {

            writer.write(
                    "workload,variant,structure,n,time_ms,steps,moves,comparisons\n"
            );

            for (int n : SIZES) {
                runW1(n, writer);
                runW2(n, writer);
                runW3Head(n, writer);
                runW3Middle(n, writer);
                runW4(n, writer);
            }
        }

        System.out.println("Results saved to results/results.csv");
    }


    private static void runW1(int n, FileWriter writer) throws IOException {
        DynamicArray array = new DynamicArray();
        MyLinkedList list = new MyLinkedList();

        Random dataRandom = new Random(42);

        for (int i = 0; i < n; i++) {
            int value = dataRandom.nextInt();
            array.add(value);
            list.add(value);
        }

        int[] indices = new int[10000];
        Random indexRandom = new Random(42);

        for (int i = 0; i < indices.length; i++) {
            indices[i] = indexRandom.nextInt(n);
        }

        // Warm-up run
        for (int index : indices) {
            array.get(index);
        }

        for (int index : indices) {
            list.get(index);
        }

        double[] arrayTimes = new double[5];
        double[] listTimes = new double[5];

        // 5 measured runs
        for (int run = 0; run < 5; run++) {

            array.resetMetrics();

            long start = System.nanoTime();

            for (int index : indices) {
                array.get(index);
            }

            long end = System.nanoTime();

            arrayTimes[run] = (end - start) / 1_000_000.0;


            list.resetMetrics();

            start = System.nanoTime();

            for (int index : indices) {
                list.get(index);
            }

            end = System.nanoTime();

            listTimes[run] = (end - start) / 1_000_000.0;
        }



        double arrayMedian = median(arrayTimes);
        double listMedian = median(listTimes);

        long arraySteps = array.getMetrics().getSteps();
        long arrayMoves = array.getMetrics().getMoves();
        long arrayComparisons = array.getMetrics().getComparisons();

        long listSteps = list.getMetrics().getSteps();
        long listMoves = list.getMetrics().getMoves();
        long listComparisons = list.getMetrics().getComparisons();

        writeResult(
                writer,
                "W1",
                "-",
                "DynamicArray",
                n,
                arrayMedian,
                arraySteps,
                arrayMoves,
                arrayComparisons
        );

        writeResult(
                writer,
                "W1",
                "-",
                "MyLinkedList",
                n,
                listMedian,
                listSteps,
                listMoves,
                listComparisons
        );


        System.out.println(
                "W1 n=" + n +
                        " DynamicArray: time=" + arrayMedian +
                        " ms, steps=" + arraySteps +
                        ", moves=" + arrayMoves +
                        ", comparisons=" + arrayComparisons
        );

        System.out.println(
                "W1 n=" + n +
                        " MyLinkedList: time=" + listMedian +
                        " ms, steps=" + listSteps +
                        ", moves=" + listMoves +
                        ", comparisons=" + listComparisons
        );
    }

    private static void runW2(int n, FileWriter writer) throws IOException {
        DynamicArray array = new DynamicArray();
        MyLinkedList list = new MyLinkedList();

        Random random = new Random(42);
        int[] data = new int[n];

        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(1_000_000);

            array.add(data[i]);
            list.add(data[i]);
        }

        int[] queries = new int[1000];

        // 500 values that are present
        for (int i = 0; i < 500; i++) {
            queries[i] = data[i % n];
        }

        // 500 values that are not present
        for (int i = 500; i < 1000; i++) {
            queries[i] = -1 - i;
        }

        // Warm-up
        for (int value : queries) {
            array.contains(value);
        }

        for (int value : queries) {
            list.contains(value);
        }

        double[] arrayTimes = new double[5];
        double[] listTimes = new double[5];


        for (int run = 0; run < 5; run++) {
            array.resetMetrics();

            long start = System.nanoTime();

            for (int value : queries) {
                array.contains(value);
            }

            long end = System.nanoTime();

            arrayTimes[run] = (end - start) / 1_000_000.0;


            list.resetMetrics();

            start = System.nanoTime();

            for (int value : queries) {
                list.contains(value);
            }

            end = System.nanoTime();

            listTimes[run] = (end - start) / 1_000_000.0;
        }

        double arrayMedian = median(arrayTimes);
        double listMedian = median(listTimes);

        writeResult(
                writer,
                "W2",
                "-",
                "DynamicArray",
                n,
                arrayMedian,
                array.getMetrics().getSteps(),
                array.getMetrics().getMoves(),
                array.getMetrics().getComparisons()
        );

        writeResult(
                writer,
                "W2",
                "-",
                "MyLinkedList",
                n,
                listMedian,
                list.getMetrics().getSteps(),
                list.getMetrics().getMoves(),
                list.getMetrics().getComparisons()
        );


        System.out.println(
                "W2 n=" + n +
                        " DynamicArray: time=" + arrayMedian +
                        " ms, steps=" + array.getMetrics().getSteps() +
                        ", moves=" + array.getMetrics().getMoves() +
                        ", comparisons=" + array.getMetrics().getComparisons()
        );

        System.out.println(
                "W2 n=" + n +
                        " MyLinkedList: time=" + listMedian +
                        " ms, steps=" + list.getMetrics().getSteps() +
                        ", moves=" + list.getMetrics().getMoves() +
                        ", comparisons=" + list.getMetrics().getComparisons()
        );
    }

    private static void runW3Head(int n, FileWriter writer) throws IOException{
        double[] arrayTimes = new double[5];
        double[] listTimes = new double[5];

        DynamicArray lastArray = null;
        MyLinkedList lastList = null;

        // Warm-up + 5 measured runs
        for (int run = -1; run < 5; run++) {
            DynamicArray array = new DynamicArray();
            MyLinkedList list = new MyLinkedList();

            Random random = new Random(42);

            for (int i = 0; i < n; i++) {
                int value = random.nextInt();
                array.add(value);
                list.add(value);
            }

            array.resetMetrics();

            long start = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                array.add(0, i);
            }

            for (int i = 0; i < 1000; i++) {
                array.remove(0);
            }

            long end = System.nanoTime();

            if (run >= 0) {
                arrayTimes[run] = (end - start) / 1_000_000.0;
            }


            list.resetMetrics();

            start = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                list.add(0, i);
            }

            for (int i = 0; i < 1000; i++) {
                list.remove(0);
            }

            end = System.nanoTime();

            if (run >= 0) {
                listTimes[run] = (end - start) / 1_000_000.0;

                lastArray = array;
                lastList = list;
            }
        }

        double arrayMedian = median(arrayTimes);
        double listMedian = median(listTimes);

        System.out.println(
                "W3 head n=" + n +
                        " DynamicArray: time=" + arrayMedian +
                        " ms, steps=" + lastArray.getMetrics().getSteps() +
                        ", moves=" + lastArray.getMetrics().getMoves() +
                        ", comparisons=" + lastArray.getMetrics().getComparisons()
        );

        System.out.println(
                "W3 head n=" + n +
                        " MyLinkedList: time=" + listMedian +
                        " ms, steps=" + lastList.getMetrics().getSteps() +
                        ", moves=" + lastList.getMetrics().getMoves() +
                        ", comparisons=" + lastList.getMetrics().getComparisons()
        );

        writeResult(
                writer,
                "W3",
                "head",
                "DynamicArray",
                n,
                arrayMedian,
                lastArray.getMetrics().getSteps(),
                lastArray.getMetrics().getMoves(),
                lastArray.getMetrics().getComparisons()
        );

        writeResult(
                writer,
                "W3",
                "head",
                "MyLinkedList",
                n,
                listMedian,
                lastList.getMetrics().getSteps(),
                lastList.getMetrics().getMoves(),
                lastList.getMetrics().getComparisons()
        );

    }

    private static void runW3Middle(int n, FileWriter writer) throws IOException{
        double[] arrayTimes = new double[5];
        double[] listTimes = new double[5];

        DynamicArray lastArray = null;
        MyLinkedList lastList = null;

        for (int run = -1; run < 5; run++) {
            DynamicArray array = new DynamicArray();
            MyLinkedList list = new MyLinkedList();

            Random random = new Random(42);

            for (int i = 0; i < n; i++) {
                int value = random.nextInt();
                array.add(value);
                list.add(value);
            }

            int middle = n / 2;

            array.resetMetrics();

            long start = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                array.add(middle, i);
            }

            for (int i = 0; i < 1000; i++) {
                array.remove(middle);
            }

            long end = System.nanoTime();

            if (run >= 0) {
                arrayTimes[run] = (end - start) / 1_000_000.0;
            }


            list.resetMetrics();

            start = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                list.add(middle, i);
            }

            for (int i = 0; i < 1000; i++) {
                list.remove(middle);
            }

            end = System.nanoTime();

            if (run >= 0) {
                listTimes[run] = (end - start) / 1_000_000.0;

                lastArray = array;
                lastList = list;
            }
        }

        double arrayMedian = median(arrayTimes);
        double listMedian = median(listTimes);

        System.out.println(
                "W3 middle n=" + n +
                        " DynamicArray: time=" + arrayMedian +
                        " ms, steps=" + lastArray.getMetrics().getSteps() +
                        ", moves=" + lastArray.getMetrics().getMoves() +
                        ", comparisons=" + lastArray.getMetrics().getComparisons()
        );

        System.out.println(
                "W3 middle n=" + n +
                        " MyLinkedList: time=" + listMedian +
                        " ms, steps=" + lastList.getMetrics().getSteps() +
                        ", moves=" + lastList.getMetrics().getMoves() +
                        ", comparisons=" + lastList.getMetrics().getComparisons()
        );

        writeResult(
                writer,
                "W3",
                "middle",
                "DynamicArray",
                n,
                arrayMedian,
                lastArray.getMetrics().getSteps(),
                lastArray.getMetrics().getMoves(),
                lastArray.getMetrics().getComparisons()
        );

        writeResult(
                writer,
                "W3",
                "middle",
                "MyLinkedList",
                n,
                listMedian,
                lastList.getMetrics().getSteps(),
                lastList.getMetrics().getMoves(),
                lastList.getMetrics().getComparisons()
        );

    }


    private static void runW4(int n, FileWriter writer) throws IOException {
        double[] times = new double[5];
        MinHeap lastHeap = null;

        for (int run = -1; run < 5; run++) {
            MinHeap heap = new MinHeap();
            Random random = new Random(42);

            int[] values = new int[n];

            for (int i = 0; i < n; i++) {
                values[i] = random.nextInt();
            }

            heap.resetMetrics();

            long start = System.nanoTime();

            for (int value : values) {
                heap.insert(value);
            }

            int previous = heap.extractMin();

            while (heap.size() > 0) {
                int current = heap.extractMin();

                if (current < previous) {
                    throw new IllegalStateException(
                            "Heap output is not sorted"
                    );
                }

                previous = current;
            }

            long end = System.nanoTime();

            if (run >= 0) {
                times[run] = (end - start) / 1_000_000.0;
                lastHeap = heap;
            }
        }

        double heapMedian = median(times);

        writeResult(
                writer,
                "W4",
                "-",
                "MinHeap",
                n,
                heapMedian,
                lastHeap.getMetrics().getSteps(),
                lastHeap.getMetrics().getMoves(),
                lastHeap.getMetrics().getComparisons()
        );

        System.out.println(
                "W4 n=" + n +
                        " MinHeap: time=" + heapMedian +
                        " ms, steps=" + lastHeap.getMetrics().getSteps() +
                        ", moves=" + lastHeap.getMetrics().getMoves() +
                        ", comparisons=" + lastHeap.getMetrics().getComparisons()
        );
    }

    private static void writeResult(
            FileWriter writer,
            String workload,
            String variant,
            String structure,
            int n,
            double time,
            long steps,
            long moves,
            long comparisons) throws IOException {

        writer.write(
                workload + "," +
                        variant + "," +
                        structure + "," +
                        n + "," +
                        time + "," +
                        steps + "," +
                        moves + "," +
                        comparisons + "\n"
        );
    }

    private static double median(double[] values) {
        java.util.Arrays.sort(values);
        return values[values.length / 2];
    }

}