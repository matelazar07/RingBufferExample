package at.jku.swtesting;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RingBufferTest {

	@Test
	public void testCapacity() {
		RingBuffer<Integer> buffer = new RingBuffer<>(5);
		assertEquals(5, buffer.capacity(), "Capacity should match the provided value.");
	}

	@Test
	public void testIsEmptyOnNewBuffer() {
		RingBuffer<Integer> buffer = new RingBuffer<>(5);
		assertTrue(buffer.isEmpty(), "Newly created buffer should be empty.");
	}

	@Test
	public void testEnqueueAndSize() {
		RingBuffer<Integer> buffer = new RingBuffer<>(3);
		buffer.enqueue(1);
		buffer.enqueue(2);
		assertEquals(2, buffer.size(), "Size should be updated after enqueue operations.");
	}

	@Test
	public void testDequeue() {
		RingBuffer<Integer> buffer = new RingBuffer<>(3);
		buffer.enqueue(1);
		buffer.enqueue(2);
		assertEquals(1, buffer.dequeue(), "Dequeue should return the first enqueued element.");
		assertEquals(1, buffer.size(), "Size should decrease after dequeue operation.");
	}

	@Test
	public void testPeek() {
		RingBuffer<Integer> buffer = new RingBuffer<>(3);
		buffer.enqueue(10);
		assertEquals(10, buffer.peek(), "Peek should return the first element without removing it.");
		assertEquals(1, buffer.size(), "Size should not change after peek operation.");
	}

	@Test
	public void testIsFull() {
		RingBuffer<Integer> buffer = new RingBuffer<>(2);
		buffer.enqueue(1);
		buffer.enqueue(2);
		assertTrue(buffer.isFull(), "Buffer should be full after adding elements equal to its capacity.");
	}

	@Test
	public void testOverwriteOldestElement() {
		RingBuffer<Integer> buffer = new RingBuffer<>(2);
		buffer.enqueue(1);
		buffer.enqueue(2);
		buffer.enqueue(3); // Overwrites the oldest element (1)
		assertEquals(2, buffer.dequeue(), "Oldest element should have been overwritten.");
	}

	@Test
	public void testMultipleOverwrites() {
		RingBuffer<Integer> buffer = new RingBuffer<>(2);
		buffer.enqueue(1);
		buffer.enqueue(2);
		buffer.enqueue(3); // Overwrites 1
		buffer.enqueue(4); // Overwrites 2

		assertEquals(3, buffer.dequeue());
		assertEquals(4, buffer.dequeue());
		assertTrue(buffer.isEmpty());
	}

	@Test
	public void testDequeueFromEmptyThrowsException() {
		RingBuffer<Integer> buffer = new RingBuffer<>(3);
		assertThrows(RuntimeException.class, buffer::dequeue, "Dequeue from an empty buffer should throw an exception.");
	}

	@Test
	public void testPeekFromEmptyThrowsException() {
		RingBuffer<Integer> buffer = new RingBuffer<>(3);
		assertThrows(RuntimeException.class, buffer::peek, "Peeking an empty buffer should throw an exception.");
	}

	@Test
	public void testInvalidCapacityThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new RingBuffer<>(0), "Creating buffer with capacity < 1 should throw IllegalArgumentException.");
	}

	@Test
	public void testIterator() {
		RingBuffer<Integer> buffer = new RingBuffer<>(3);
		buffer.enqueue(1);
		buffer.enqueue(2);
		buffer.enqueue(3);

		Iterator<Integer> iterator = buffer.iterator();
		assertTrue(iterator.hasNext(), "Iterator should have elements.");
		assertEquals(1, iterator.next(), "Iterator should return elements in correct order.");
		assertEquals(2, iterator.next());
		assertEquals(3, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorWithWrapAround() {
		RingBuffer<String> buffer = new RingBuffer<>(3);
		buffer.enqueue("a");  // buffer = [a]
		buffer.enqueue("b");  // buffer = [a, b]
		buffer.enqueue("c");  // buffer = [a, b, c]
		buffer.dequeue();     // removes "a", buffer = [b, c]
		buffer.enqueue("d");  // wraps around, buffer = [b, c, d] (in circular logic)

		Iterator<String> it = buffer.iterator();
		assertEquals("b", it.next());
		assertEquals("c", it.next());
		assertEquals("d", it.next());
		assertFalse(it.hasNext());
	}

	@Test
	public void testIteratorThrowsNoSuchElementException() {
		RingBuffer<Integer> buffer = new RingBuffer<>(3);
		Iterator<Integer> iterator = buffer.iterator();
		assertThrows(NoSuchElementException.class, iterator::next, "Calling next() on empty iterator should throw an exception.");
	}

	@Test
	public void testIteratorRemoveUnsupported() {
		RingBuffer<String> buffer = new RingBuffer<>(2);
		buffer.enqueue("a");
		Iterator<String> it = buffer.iterator();
		assertThrows(UnsupportedOperationException.class, it::remove, "Calling remove() on iterator should throw UnsupportedOperationException.");
	}
}
