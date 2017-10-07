package ua.ds;

public class BitMaskResizableNotShrinkArrayQueueBoxedIntrinsic implements SequentialQueueBoxed {
  private Integer[] items;
  private int head;
  private int tail;
  private int size;

  public BitMaskResizableNotShrinkArrayQueueBoxedIntrinsic() {
    this(16);
  }

  public BitMaskResizableNotShrinkArrayQueueBoxedIntrinsic(int capacity) {
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
    int begin = head;
    int length = items.length;
    int right = length - begin;
    System.arraycopy(items, begin, temp, 0, right);
    System.arraycopy(items, 0, temp, right, begin);
    items = temp;
    head = 0;
    tail = size;
  }
}
