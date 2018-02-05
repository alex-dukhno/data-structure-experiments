package ua.ds.array.primitive;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

import ua.ds.SequentialQueue;
import ua.ds.array.ArrayQueue;

public class UnsafeArrayQueuePrimitive implements SequentialQueue {
  private static final Unsafe UNSAFE;

  static {
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      UNSAFE = (Unsafe) field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      throw new InternalError("Unsafe.theUnsafe field not available", ex);
    }
  }

  private long items;
  private int capacity;
  private int head;
  private int tail;
  private int size;

  public UnsafeArrayQueuePrimitive() {
    this(16);
  }

  public UnsafeArrayQueuePrimitive(int capacity) {
    this.capacity = ArrayQueue.nextPowerOfTwo(capacity);
    this.items = UNSAFE.allocateMemory(this.capacity << 2);
    this.size = 0;
    this.head = 0;
    this.tail = 0;
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    int item = UNSAFE.getInt(items + (head << 2));
    size--;
    head = (head + 1) & (capacity - 1);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void enqueue(int item) {
    if (size == capacity) resize();
    UNSAFE.putInt(items + (tail << 2), item);
    tail = (tail + 1) & (capacity - 1);
    size++;
  }

  private void resize() {
    capacity <<= 1;
    long temp = UNSAFE.allocateMemory(capacity << 2);
    for (int i = 0; i < size; i++) {
      int item = UNSAFE.getInt(items + (((head + i) & (capacity - 1)) << 2));
      UNSAFE.putInt(temp + (i << 2), item);
    }
    UNSAFE.freeMemory(items);
    items = temp;
    head = 0;
    tail = size;
  }

  public void deallocateItems() {
    UNSAFE.freeMemory(items);
  }
}
