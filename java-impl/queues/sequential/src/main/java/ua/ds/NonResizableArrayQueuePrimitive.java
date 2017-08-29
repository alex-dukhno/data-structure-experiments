package ua.ds;

import org.openjdk.jol.datamodel.X86_32_DataModel;
import org.openjdk.jol.datamodel.X86_64_COOPS_DataModel;
import org.openjdk.jol.datamodel.X86_64_DataModel;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.layouters.HotSpotLayouter;

public class NonResizableArrayQueuePrimitive {
  private final int[] items;
  private int head;
  private int tail;
  private final int mask;

  public NonResizableArrayQueuePrimitive() {
    this(16);
  }

  public NonResizableArrayQueuePrimitive(int capacity) {
    items = new int[capacity];
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
    NonResizableArrayQueuePrimitive queuePrimitive = new NonResizableArrayQueuePrimitive(capacity);
    for (int i = 0; i < capacity; i++) {
      queuePrimitive.enqueue(i);
    }
    System.out.println(GraphLayout.parseInstance(queuePrimitive).totalSize());
  }
}
