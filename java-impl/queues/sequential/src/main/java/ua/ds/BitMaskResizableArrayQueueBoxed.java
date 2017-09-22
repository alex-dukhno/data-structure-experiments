package ua.ds;

public class BitMaskResizableArrayQueueBoxed implements SequentialQueueBoxed {
  private Integer[] items;
  private int head;
  private int tail;
  private int size;

  public BitMaskResizableArrayQueueBoxed() {
    this(16);
  }

  public BitMaskResizableArrayQueueBoxed(int capacity) {
    capacity = SequentialQueueBoxed.nextPowerOfTwo(capacity);
    items = new Integer[capacity];
    size = 0;
    head = 0;
    tail = 0;
  }

  @Override
  public Integer deque() {
    if (isEmpty()) return null;
    int item = items[head];
    size--;
    head = (head + 1) & (items.length - 1);
    if (size > MIN_CAPACITY && size == items.length >> 2) resize(items.length >> 1);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void enqueue(Integer item) {
    if (size == items.length) resize(items.length << 1);
    items[tail] = item;
    tail = (tail + 1) & (items.length - 1);
    size++;
  }

  private void resize(int capacity) {
    Integer[] temp = new Integer[capacity];
    for (int i = 0; i < size; i++) {
      temp[i] = items[(head + i) & (items.length - 1)];
    }
    items = temp;
    head = 0;
    tail = size;
  }
}
