package ua.ds;

public class ConditionalResizableNotShrinkArrayQueueBoxed implements SequentialQueueBoxed {
  private Integer[] items;
  private int size;
  private int head;
  private int tail;

  public ConditionalResizableNotShrinkArrayQueueBoxed() {
    this(16);
  }

  public ConditionalResizableNotShrinkArrayQueueBoxed(int capacity) {
    capacity = SequentialQueueBoxed.nextPowerOfTwo(capacity);
    items = new Integer[capacity];
    size = 0;
    head = 0;
    tail = 0;
  }

  @Override
  public Integer deque() {
    if (isEmpty()) return null;
    int item = items[head++];
    size--;
    if (head == items.length) head = 0;
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void enqueue(Integer item) {
    if (size == items.length) resize(2 * items.length);
    items[tail++] = item;
    if (tail == items.length) tail = 0;
    size++;
  }

  private void resize(int capacity) {
    Integer[] temp = new Integer[capacity];
    for (int i = 0; i < size; i++) {
      temp[i] = items[(head + i) % items.length];
    }
    items = temp;
    head = 0;
    tail = size;
  }
}
