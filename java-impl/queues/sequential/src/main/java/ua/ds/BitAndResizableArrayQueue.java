package ua.ds;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class BitAndResizableArrayQueue {
  private static final int MIN_CAPACITY = 16;
  private static final int MAX_CAPACITY = Integer.MIN_VALUE >>> 1;

  private Integer[] items;
  private int head;
  private int tail;
  private int mask;
  private int capacity;

  public BitAndResizableArrayQueue() {
    this(16);
  }

  public BitAndResizableArrayQueue(int capacity) {
    items = new Integer[capacity];
    head = 0;
    tail = capacity - 1;
    mask = capacity - 1;
    this.capacity = capacity;
  }

  public int deque() {
    if (isEmpty()) return -1;
    nextTailIndex();
    int item = items[tail];
    if (isFullByQuarter()) shrink();
    return item;
  }

  private void nextTailIndex() {
    tail = nextIndex(tail, mask);
  }

  private boolean isFullByQuarter() {
    return size() == quarterOfCapacity();
  }

  private int size() {
    return mask - (tail - head & mask);
  }

  private int quarterOfCapacity() {
    return capacity >> 2;
  }

  private void shrink() {
    if (capacity > MIN_CAPACITY) {
      int newCapacity = capacity >> 1;
      items = copyItems(newCapacity);
      updateCursor(newCapacity - 1);
    }
  }

  private Integer[] copyItems(int newCapacity) {
    Integer[] buffer = new Integer[newCapacity];
    int newMask = newCapacity - 1;
    int newItemsIndex = (newMask - mask + tail + 1) & newMask;
    for (int itemsIndex = beginningOfItems(); itemsIndex != head; itemsIndex = nextIndex(itemsIndex, mask)) {
      buffer[newItemsIndex] = items[itemsIndex];
      newItemsIndex = nextIndex(newItemsIndex, newMask);
    }
    return buffer;
  }

  private int nextIndex(int index, int mask) {
    return (index + 1) & mask;
  }

  private int beginningOfItems() {
    return nextIndex(tail, mask);
  }

  private void updateCursor(int newMask) {
    tail = (newMask - mask + tail) & newMask;
    head = head & newMask;
    mask = newMask;
    capacity = newMask + 1;
  }

  public void enqueue(int item) {
    if (isFull()) resize();
    int index = head;
    nextHeadIndex();
    items[index] = item;
  }

  private void nextHeadIndex() {
    head = nextIndex(head, mask);
  }

  private boolean isFull() {
    return head == tail;
  }

  private void resize() {
    int newCapacity = capacity << 1;
    newCapacity = newCapacity > MAX_CAPACITY ? MAX_CAPACITY : newCapacity;
    items = copyItems(newCapacity);
    updateCursor(newCapacity - 1);
  }

  private boolean isEmpty() {
    return (tail - head & mask) == mask;
  }

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(BitAndResizableArrayQueue.class).toPrintable());
  }
}
