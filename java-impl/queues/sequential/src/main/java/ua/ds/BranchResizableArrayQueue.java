package ua.ds;

public class BranchResizableArrayQueue implements SequentialQueue {
  private int[] items;
  private int size;
  private int head;
  private int tail;

  public BranchResizableArrayQueue() {
    this(16);
  }

  public BranchResizableArrayQueue(int capacity) {
    capacity = SequentialQueue.nextPowerOfTwo(capacity);
    items = new int[capacity];
    size = 0;
    head = 0;
    tail = 0;
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    int item = items[head];
    size--;
    head++;
    if (head == items.length) head = 0;
    if (size > MIN_CAPACITY && size == items.length / 4) resize(items.length / 2);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void enqueue(int item) {
    if (size == items.length) resize(2 * items.length);
    items[tail++] = item;
    if (tail == items.length) tail = 0;
    size++;
  }

  private void resize(int capacity) {
    int[] temp = new int[capacity];
    for (int i = 0; i < size; i++) {
      temp[i] = items[(head + i) % items.length];
    }
    items = temp;
    head = 0;
    tail = size;
  }
}
