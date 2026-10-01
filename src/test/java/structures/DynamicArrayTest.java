package structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void testAddAndGet() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(30);

        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testAddAtIndex() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(30);
        array.add(1, 20);

        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testRemove() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(30);

        int removed = array.remove(1);

        assertEquals(20, removed);
        assertEquals(2, array.size());
        assertEquals(10, array.get(0));
        assertEquals(30, array.get(1));
    }

    @Test
    void testContains() {
        DynamicArray array = new DynamicArray();

        array.add(5);
        array.add(10);
        array.add(10);

        assertTrue(array.contains(5));
        assertTrue(array.contains(10));
        assertFalse(array.contains(100));
    }

    @Test
    void testInvalidIndex() {
        DynamicArray array = new DynamicArray();

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(0));

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(-1, 10));

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(0));
    }

    @Test
    void testOneElement() {
        DynamicArray array = new DynamicArray();

        array.add(50);

        assertEquals(1, array.size());
        assertEquals(50, array.get(0));
    }

    @Test
    void testFirstAndLastIndex() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(30);

        assertEquals(10, array.get(0));
        assertEquals(30, array.get(2));
    }

    @Test
    void testGrow() {
        DynamicArray array = new DynamicArray();

        for (int i = 0; i < 15; i++) {
            array.add(i);
        }

        assertEquals(15, array.size());

        for (int i = 0; i < 15; i++) {
            assertEquals(i, array.get(i));
        }
    }

}