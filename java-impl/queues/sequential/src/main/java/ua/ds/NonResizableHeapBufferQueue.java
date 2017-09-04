package ua.ds;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class NonResizableHeapBufferQueue implements SequentialQueue {
  private final IntBuffer items;
  private int head;
  private int tail;
  private final int mask;

  public NonResizableHeapBufferQueue() {
    this(16);
  }

  public NonResizableHeapBufferQueue(int capacity) {
    capacity = SequentialQueue.nextPowerOfTwo(capacity);
    items = ByteBuffer.allocate(4 * capacity).asIntBuffer();
    head = 0;
    tail = capacity - 1;
    mask = capacity - 1;
  }

  @Override
  public void enqueue(int item) {
    int index = head;
    head = (head + 1) & mask;
    items.put(index, item);
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    tail = (tail + 1) & mask;
    return items.get(tail);
  }

  private boolean isEmpty() {
    return (tail - head & mask) == mask;
  }
}
