package ua.ds.array.primitive;

import ua.ds.SequentialQueue;
import ua.ds.array.ArrayQueue;

public class ConditionalNonResizableArrayQueuePrimitiveWithIncrement implements SequentialQueue {
  private int[] items;
  private int head;
  private int tail;
  private int size;

  public ConditionalNonResizableArrayQueuePrimitiveWithIncrement() {
    this(16);
  }

  public ConditionalNonResizableArrayQueuePrimitiveWithIncrement(int capacity) {
    capacity = ArrayQueue.nextPowerOfTwo(capacity);
    items = new int[capacity];
    head = 0;
    tail = 0;
    size = 0;
  }

  @Override
  public void enqueue(int item) {
    items[tail++] = item;
    if (tail == items.length) tail = 0;
    size++;
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    int item = items[head++];
    size--;
    if (head == items.length) head = 0;
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }
}
