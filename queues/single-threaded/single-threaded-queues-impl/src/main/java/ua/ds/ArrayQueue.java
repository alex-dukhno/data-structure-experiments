package ua.ds;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class ArrayQueue {
  private final int[] items;
  private int head;
  private int tail;
  private final int mask;

  public ArrayQueue() {
    this(16);
  }

  public ArrayQueue(int capacity) {
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
    System.out.println(ClassLayout.parseClass(ArrayQueue.class).toPrintable());
    System.out.println(ClassLayout.parseClass(int[].class).toPrintable());

    System.out.println(GraphLayout.parseInstance(new ArrayQueue(8192)).totalSize());
  }
}
