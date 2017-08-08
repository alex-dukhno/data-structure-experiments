package ua.ds.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ArrayBlockingQueue {
  private static final int MIN_CAPACITY = 16;
  private static final int MAX_CAPACITY = Integer.MIN_VALUE >>> 1;

  private final Lock mutex;
  private final Condition empty;
  private final Condition full;
  private Integer[] items;
  private int mask;
  private int head;
  private int tail;
  private int capacity;

  public ArrayBlockingQueue() {
    this(16);
  }

  public ArrayBlockingQueue(int capacity) {
    mutex = new ReentrantLock();
    empty = mutex.newCondition();
    full = mutex.newCondition();
    items = new Integer[capacity];
    head = 0;
    tail = capacity - 1;
    mask = capacity - 1;
    this.capacity = capacity;
  }

  public int deque() throws InterruptedException {
    mutex.lock();
    try {
      while (isEmpty()) {
        empty.await();
      }
      nextTailIndex();
      int item = items[tail];
      if (isFullByQuarter()) shrink();
      return item;
    } finally {
      full.signalAll();
      mutex.unlock();
    }
  }

  private boolean isEmpty() {
    return (tail - head & mask) == mask;
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

  public void enqueue(int item) throws InterruptedException {
    mutex.lock();
    try {
      while (isCompletelyFull()) {
        full.await();
      }
      if (isFull() && !isCompletelyFull()) resize();
      int index = head;
      nextHeadIndex();
      items[index] = item;
    } finally {
      empty.signalAll();
      mutex.unlock();
    }
  }

  private boolean isCompletelyFull() {
    return size() == MAX_CAPACITY;
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

  private void nextHeadIndex() {
    head = nextIndex(head, mask);
  }

  private int nextIndex(int index, int mask) {
    return (index + 1) & mask;
  }

  private void updateCursor(int newMask) {
    tail = (newMask - mask + tail) & newMask;
    head = head & newMask;
    mask = newMask;
    capacity = newMask + 1;
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

  private int beginningOfItems() {
    return nextIndex(tail, mask);
  }
}
