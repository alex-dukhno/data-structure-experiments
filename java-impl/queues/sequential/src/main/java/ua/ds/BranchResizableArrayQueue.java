package ua.ds;

public class BranchResizableArrayQueue implements SequentialQueue {
  private int[] items;
  private int size;
  private int first;
  private int last;

  public BranchResizableArrayQueue() {
    this(16);
  }

  public BranchResizableArrayQueue(int capacity) {
    capacity = SequentialQueue.nextPowerOfTwo(capacity);
    items = new int[capacity];
    size = 0;
    first = 0;
    last = 0;
  }

  @Override
  public int deque() {
    if (isEmpty()) return -1;
    int item = items[first];
    size--;
    first++;
    if (first == items.length) first = 0;
    if (size > MIN_CAPACITY && size == items.length / 4) resize(items.length / 2);
    return item;
  }

  private boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void enqueue(int item) {
    if (size == items.length) resize(2 * items.length);
    items[last++] = item;
    if (last == items.length) last = 0;
    size++;
  }

  private void resize(int capacity) {
    int[] temp = new int[capacity];
    for (int i = 0; i < size; i++) {
      temp[i] = items[(first + i) & (items.length - 1)];
    }
    items = temp;
    first = 0;
    last = size;
  }
}
