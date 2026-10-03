package structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    @Test
    void testInsertAndPeekMin() {
        MinHeap heap = new MinHeap();

        heap.insert(20);
        heap.insert(10);
        heap.insert(30);
        heap.insert(5);

        assertEquals(5, heap.peekMin());
        assertEquals(4, heap.size());
    }

    @Test
    void testExtractMin() {
        MinHeap heap = new MinHeap();

        heap.insert(20);
        heap.insert(10);
        heap.insert(30);
        heap.insert(5);

        assertEquals(5, heap.extractMin());
        assertEquals(10, heap.peekMin());
        assertEquals(3, heap.size());
    }

    @Test
    void testOneElement() {
        MinHeap heap = new MinHeap();

        heap.insert(50);

        assertEquals(50, heap.peekMin());
        assertEquals(50, heap.extractMin());
        assertEquals(0, heap.size());
    }

    @Test
    void testDuplicates() {
        MinHeap heap = new MinHeap();

        heap.insert(10);
        heap.insert(5);
        heap.insert(5);
        heap.insert(20);

        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
    }

    @Test
    void testEmptyHeap() {
        MinHeap heap = new MinHeap();

        assertThrows(IllegalStateException.class,
                () -> heap.peekMin());

        assertThrows(IllegalStateException.class,
                () -> heap.extractMin());
    }

    @Test
    void testSortedOutput() {
        MinHeap heap = new MinHeap();

        heap.insert(30);
        heap.insert(10);
        heap.insert(50);
        heap.insert(5);
        heap.insert(20);

        int previous = heap.extractMin();

        while (heap.size() > 0) {
            int current = heap.extractMin();

            assertTrue(previous <= current);

            previous = current;
        }
    }

    @Test
    void testHeapProperty() {
        MinHeap heap = new MinHeap();

        int[] values = {30, 10, 50, 5, 20, 3, 40};

        for (int value : values) {
            heap.insert(value);
            assertTrue(heap.isValidHeap());
        }

        while (heap.size() > 0) {
            heap.extractMin();
            assertTrue(heap.isValidHeap());
        }
    }

    @Test
    void testRandomData() {
        MinHeap heap = new MinHeap();
        java.util.PriorityQueue<Integer> expected =
                new java.util.PriorityQueue<>();

        java.util.Random random = new java.util.Random(42);

        for (int i = 0; i < 100; i++) {
            int value = random.nextInt(1000);

            heap.insert(value);
            expected.add(value);
        }

        while (!expected.isEmpty()) {
            assertEquals(expected.remove(), heap.extractMin());
        }

        assertEquals(0, heap.size());
    }

}