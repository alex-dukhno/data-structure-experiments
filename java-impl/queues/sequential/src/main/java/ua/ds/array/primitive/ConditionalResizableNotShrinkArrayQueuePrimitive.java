package ua.ds.array.primitive;

import ua.ds.SequentialQueue;
import ua.ds.array.ArrayQueue;

public class ConditionalResizableNotShrinkArrayQueuePrimitive implements SequentialQueue {
  private int[] items;
  private int size;
  private int head;
  private int tail;

  public ConditionalResizableNotShrinkArrayQueuePrimitive() {
    this(16);
  }

  public ConditionalResizableNotShrinkArrayQueuePrimitive(int capacity) {
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
    if (head == items.length) head = 0;
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void enqueue(int item) {
    if (size == items.length) resize(2 * items.length);
    items[tail++] = item;
    if (tail == items.length) tail = 0;
    size++;
  }

  private void resize(int capacity) {
    int[] temp = new int[capacity];
    for (int i = 0; i < size; i++) {
      temp[i] = items[(head + i) % items.length];
    }
    items = temp;
    head = 0;
    tail = size;
  }
}
