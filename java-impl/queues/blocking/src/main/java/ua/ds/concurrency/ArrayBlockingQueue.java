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
  private int[] items;
  private int head;
  private int tail;
  private int size;

  public ArrayBlockingQueue() {
    this(16);
  }

  public ArrayBlockingQueue(int capacity) {
    mutex = new ReentrantLock();
    empty = mutex.newCondition();
    full = mutex.newCondition();
    items = new int[capacity];
    size = 0;
    head = 0;
    tail = 0;
  }

  public int deque() throws InterruptedException {
    mutex.lock();
    try {
      while (isEmpty()) {
        empty.await();
      }
      int item = items[head];
      size--;
      head = (head + 1) & (items.length - 1);
      if (size > MIN_CAPACITY && size == items.length >> 2) resize(items.length >> 1);
      return item;
    } finally {
      full.signalAll();
      mutex.unlock();
    }
  }

  private boolean isEmpty() {
    return size == 0;
  }

  private boolean isFullByQuarter() {
    return size == quarterOfCapacity();
  }

  private int quarterOfCapacity() {
    return items.length >> 2;
  }

  public void enqueue(int item) throws InterruptedException {
    mutex.lock();
    try {
      while (isCompletelyFull()) {
        full.await();
      }
      if (size == items.length) resize(items.length << 1);
      items[tail] = item;
      tail = (tail + 1) & (items.length - 1);
      size++;
    } finally {
      empty.signalAll();
      mutex.unlock();
    }
  }

  private boolean isCompletelyFull() {
    return size == MAX_CAPACITY;
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
}
