package ua.ds;

public class ConditionalNonResizableArrayQueueBoxed implements SequentialQueue {
  private final Integer[] items;
  private int head;
  private int tail;
  private int size;

  public ConditionalNonResizableArrayQueueBoxed() {
    this(16);
  }

  public ConditionalNonResizableArrayQueueBoxed(int capacity) {
    capacity = SequentialQueue.nextPowerOfTwo(capacity);
    items = new Integer[capacity];
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
    Integer item = items[head++];
    size--;
    if (head == items.length) head = 0;
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }
}
