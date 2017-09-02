package ua.ds;

public class NonResizableArrayQueuePrimitive implements SequentialQueue {
  private final int[] items;
  private int head;
  private int tail;
  private final int mask;

  public NonResizableArrayQueuePrimitive() {
    this(16);
  }

  public NonResizableArrayQueuePrimitive(int capacity) {
    capacity = SequentialQueue.nextPowerOfTwo(capacity);
    items = new int[capacity];
    head = 0;
    tail = capacity - 1;
    mask = capacity - 1;
  }

  @Override
  public void enqueue(int item) {
    int index = head;
    head = (head + 1) & mask;
    items[index] = item;
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    tail = (tail + 1) & mask;
    return items[tail];
  }

  private boolean isEmpty() {
    return (tail - head & mask) == mask;
  }
}
