package at.jku.swtesting;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The RingBuffer class represents a first-in-first-out (FIFO) circular queue of elements.
 * It has a maximum capacity of elements it can hold. If more elements are added,
 * the last element will overwrite the first one.
 */
public class RingBuffer<Item> implements Iterable<Item> {

	private Item[] a;     // queue elements
	private int N = 0;    // number of elements in queue
	private int first = 0; // index of first element
	private int last = 0;  // index of next available slot

	/**
	 * Creates a new empty ring buffer.
	 * @param capacity number of elements the buffer is able to hold.
	 * @throws IllegalArgumentException if the initial capacity is less than one.
	 */
	@SuppressWarnings("unchecked")
	public RingBuffer(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("Initial capacity is less than one");
		}
		a = (Item[]) new Object[capacity];
	}

	/**
	 * Returns the number of elements the buffer can hold.
	 */
	public int capacity() {
		return a.length;
	}

	/**
	 * Returns the number of elements in the buffer.
	 */
	public int size() {
		return N;
	}

	/**
	 * Returns true if the buffer contains no elements.
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * Returns true if the buffer has reached its capacity.
	 */
	public boolean isFull() {
		return N == a.length;
	}

	/**
	 * Appends the specified element to the end of the buffer.
	 * If the buffer is full, it overwrites the oldest element.
	 * @param item to be added
	 */
	public void enqueue(Item item) {
		a[last] = item;
		last = (last + 1) % a.length;
		if (N < a.length) {
			N++;
		} else {
			first = (first + 1) % a.length; // overwrite oldest
		}
	}

	/**
	 * Removes and returns the first element from the buffer.
	 * @throws RuntimeException if the buffer is empty.
	 */
	public Item dequeue() {
		if (isEmpty()) {
			throw new RuntimeException("Empty ring buffer.");
		}
		Item item = a[first];
		a[first] = null;
		first = (first + 1) % a.length;
		N--;
		return item;
	}

	/**
	 * Returns the first element without removing it.
	 * @throws RuntimeException if the buffer is empty.
	 */
	public Item peek() {
		if (isEmpty()) {
			throw new RuntimeException("Empty ring buffer.");
		}
		return a[first];
	}

	/**
	 * Returns an iterator over the elements in the buffer.
	 */
	public Iterator<Item> iterator() {
		return new RingBufferIterator();
	}

	private class RingBufferIterator implements Iterator<Item> {
		private int count = 0;
		private int current = first;

		public boolean hasNext() {
			return count < N;
		}

		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Item item = a[current];
			current = (current + 1) % a.length;
			count++;
			return item;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
