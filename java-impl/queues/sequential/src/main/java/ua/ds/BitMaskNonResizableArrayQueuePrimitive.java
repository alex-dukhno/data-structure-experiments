package ua.ds;

public class BitMaskNonResizableArrayQueuePrimitive implements SequentialQueue {
  private final int[] items;
  private int head;
  private int tail;
  private int size;

  public BitMaskNonResizableArrayQueuePrimitive() {
    this(16);
  }

  public BitMaskNonResizableArrayQueuePrimitive(int capacity) {
    capacity = SequentialQueue.nextPowerOfTwo(capacity);
    items = new int[capacity];
    head = 0;
    tail = 0;
    size = 0;
  }

  @Override
  public void enqueue(int item) {
    items[tail] = item;
    tail = (tail + 1) & (items.length - 1);
    size++;
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    int item = items[head];
    size--;
    head = (head + 1) & (items.length - 1);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }
}
