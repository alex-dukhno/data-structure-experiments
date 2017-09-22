package ua.ds;

public class LinkedArrayQueueBoxed implements SequentialQueueBoxed {
  private final int segmentCapacity;
  private Segment head;
  private Segment tail;

  public LinkedArrayQueueBoxed() {
    this(16);
  }

  public LinkedArrayQueueBoxed(int segmentCapacity) {
    this.segmentCapacity = SequentialQueueBoxed.nextPowerOfTwo(segmentCapacity);
  }

  @Override
  public Integer deque() {
    if (head == null || (head == tail && head.first == head.last)) return null;
    if (head.first == segmentCapacity - 1) {
      if (head == tail) {
        head = null;
      } else {
        Segment prev = head;
        head = head.next;
        prev.next = null;
      }
    }
    return head.items[++head.first];
  }

  @Override
  public void enqueue(Integer item) {
    if (tail == null) {
      Segment segment = new Segment(segmentCapacity);
      head = segment;
      tail = segment;
    }
    if (tail.last == segmentCapacity - 1) {
      tail.next = new Segment(segmentCapacity);
      tail = tail.next;
    }
    tail.items[++tail.last] = item;
  }

  private static class Segment {
    final Integer[] items;
    Segment next;
    int first;
    int last;

    Segment(int capacity) {
      items = new Integer[capacity];
      first = -1;
      last = -1;
    }
  }
}
