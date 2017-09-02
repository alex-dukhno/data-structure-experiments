package ua.ds;

public class LinkedArrayQueue implements SequentialQueue {
  private final int segmentCapacity;
  private Segment head;
  private Segment tail;

  public LinkedArrayQueue() {
    this(16);
  }

  public LinkedArrayQueue(int segmentCapacity) {
    this.segmentCapacity = segmentCapacity;
  }

  @Override
  public int deque() {
    if (head == null || (head == tail && head.first == head.last)) return -1;
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
  public void enqueue(int item) {
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
    final int[] items;
    Segment next;
    int first;
    int last;

    Segment(int capacity) {
      items = new int[capacity];
      first = -1;
      last = -1;
    }
  }
}
