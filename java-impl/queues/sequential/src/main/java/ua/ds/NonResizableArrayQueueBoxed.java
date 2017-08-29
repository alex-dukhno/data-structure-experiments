package ua.ds;

import org.openjdk.jol.info.GraphLayout;

public class NonResizableArrayQueueBoxed {
  private final Integer[] items;
  private int head;
  private int tail;
  private final int mask;

  public NonResizableArrayQueueBoxed() {
    this(16);
  }

  public NonResizableArrayQueueBoxed(int capacity) {
    items = new Integer[capacity];
    head = 0;
    tail = capacity - 1;
    mask = capacity - 1;
  }

  public void enqueue(int item) {
    int index = head;
    head = (head + 1) & mask;
    items[index] = item;
  }

  public int deque() {
    if (isEmpty()) return -1;
    tail = (tail + 1) & mask;
    return items[tail];
  }

  private boolean isEmpty() {
    return (tail - head & mask) == mask;
  }

  public static void main(String[] args) {
    int capacity = 1048576;
    NonResizableArrayQueueBoxed queueBoxed = new NonResizableArrayQueueBoxed(capacity);
    for (int i = 0; i < capacity; i++) {
      queueBoxed.enqueue(i);
    }
    System.out.println(GraphLayout.parseInstance(queueBoxed).totalSize());
  }
}
