package structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    @Test
    void testAddAndGet() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testAddAtIndex() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(30);
        list.add(1, 20);

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testAddAtHead() {
        MyLinkedList list = new MyLinkedList();

        list.add(20);
        list.add(30);
        list.add(0, 10);

        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testRemove() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        int removed = list.remove(1);

        assertEquals(20, removed);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }

    @Test
    void testRemoveHead() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);

        int removed = list.remove(0);

        assertEquals(10, removed);
        assertEquals(1, list.size());
        assertEquals(20, list.get(0));
    }

    @Test
    void testContains() {
        MyLinkedList list = new MyLinkedList();

        list.add(5);
        list.add(10);
        list.add(10);

        assertTrue(list.contains(5));
        assertTrue(list.contains(10));
        assertFalse(list.contains(100));
    }

    @Test
    void testInvalidIndex() {
        MyLinkedList list = new MyLinkedList();

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(0));

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(-1, 10));

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(0));
    }

    @Test
    void testOneElement() {
        MyLinkedList list = new MyLinkedList();

        list.add(50);

        assertEquals(1, list.size());
        assertEquals(50, list.get(0));
        assertEquals(50, list.remove(0));
        assertEquals(0, list.size());
    }

    @Test
    void testRandomData() {
        MyLinkedList list = new MyLinkedList();
        java.util.LinkedList<Integer> expected = new java.util.LinkedList<>();
        java.util.Random random = new java.util.Random(42);

        for (int i = 0; i < 100; i++) {
            int value = random.nextInt(1000);

            list.add(value);
            expected.add(value);
        }

        assertEquals(expected.size(), list.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), list.get(i));
        }
    }

}