package ua.ds;

import org.openjdk.jol.info.ClassLayout;

public class BitAndResizableArrayQueue {
  private static final int MIN_CAPACITY = 16;
  private static final int MAX_CAPACITY = Integer.MIN_VALUE >>> 1;

  private int[] items;
  private int head;
  private int tail;
  private int size;

  public BitAndResizableArrayQueue() {
    this(16);
  }

  public BitAndResizableArrayQueue(int capacity) {
    items = new int[capacity];
    size = 0;
    head = 0;
    tail = 0;
  }

  public int deque() {
    if (isEmpty()) return -1;
    int item = items[head];
    size--;
    head = (head + 1) & (items.length - 1);
    if (size > MIN_CAPACITY && size == items.length >> 2) resize(items.length >> 1);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  public void enqueue(int item) {
    if (size == items.length) resize(items.length << 1);
    items[tail] = item;
    tail = (tail + 1) & (items.length - 1);
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

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(BitAndResizableArrayQueue.class).toPrintable());
  }
}
