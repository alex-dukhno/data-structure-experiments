package ua.ds.array.primitive;

import ua.ds.SequentialQueue;
import ua.ds.array.ArrayQueue;

public class BitMaskResizableNotShrinkArrayQueuePrimitiveWithIncrement implements SequentialQueue {
  private int[] items;
  private int head;
  private int tail;
  private int size;

  public BitMaskResizableNotShrinkArrayQueuePrimitiveWithIncrement() {
    this(16);
  }

  public BitMaskResizableNotShrinkArrayQueuePrimitiveWithIncrement(int capacity) {
    capacity = ArrayQueue.nextPowerOfTwo(capacity);
    items = new int[capacity];
    size = 0;
    head = 0;
    tail = 0;
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    int item = items[head++];
    size--;
    head = head & (items.length - 1);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void enqueue(int item) {
    if (size == items.length) resize(items.length << 1);
    items[tail++] = item;
    tail = tail & (items.length - 1);
    size++;
  }

  private void resize(int capacity) {
    int[] temp = new int[capacity];
    for (int i = 0; i < size; i++) {
      temp[i] = items[(head + i) & (items.length - 1)];
    }
    items = temp;
    head = 0;
    tail = size;
  }
}
