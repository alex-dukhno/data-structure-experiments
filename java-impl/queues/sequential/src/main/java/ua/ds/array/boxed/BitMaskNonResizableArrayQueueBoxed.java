package ua.ds.array.boxed;

import ua.ds.array.ArrayQueue;
import ua.ds.SequentialQueueBoxed;

public class BitMaskNonResizableArrayQueueBoxed implements SequentialQueueBoxed {
  private final Integer[] items;
  private int head;
  private int tail;
  private int size;

  public BitMaskNonResizableArrayQueueBoxed() {
    this(16);
  }

  public BitMaskNonResizableArrayQueueBoxed(int capacity) {
    capacity = ArrayQueue.nextPowerOfTwo(capacity);
    items = new Integer[capacity];
    head = 0;
    tail = 0;
    size = 0;
  }

  @Override
  public void enqueue(Integer item) {
    items[tail] = item;
    tail = (tail + 1) & (items.length - 1);
    size++;
  }

  @Override
  public Integer deque() {
    if (isEmpty()) return null;
    Integer item = items[head];
    items[head] = null;
    size--;
    head = (head + 1) & (items.length - 1);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }
}
