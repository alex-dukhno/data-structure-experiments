package ua.ds.array.boxed;

import ua.ds.array.ArrayQueue;
import ua.ds.SequentialQueueBoxed;

public class BitMaskResizableNotShrinkArrayQueueBoxed implements SequentialQueueBoxed {
  private Integer[] items;
  private int head;
  private int tail;
  private int size;

  public BitMaskResizableNotShrinkArrayQueueBoxed() {
    this(16);
  }

  public BitMaskResizableNotShrinkArrayQueueBoxed(int capacity) {
    capacity = ArrayQueue.nextPowerOfTwo(capacity);
    items = new Integer[capacity];
    size = 0;
    head = 0;
    tail = 0;
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

  @Override
  public void enqueue(Integer item) {
    if (size == items.length) resize(items.length << 1);
    items[tail] = item;
    tail = (tail + 1) & (items.length - 1);
    size++;
  }

  private void resize(int capacity) {
    Integer[] temp = new Integer[capacity];
    for (int i = 0; i < size; i++) {
      temp[i] = items[(head + i) & (items.length - 1)];
    }
    items = temp;
    head = 0;
    tail = size;
  }
}
