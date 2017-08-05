package ua.ds;

import org.openjdk.jol.info.ClassLayout;

public class LinkedArrayQueue {
  private final int segmentCapacity;
  private Segment head;
  private Segment tail;

  public LinkedArrayQueue() {
    this(16);
  }

  public LinkedArrayQueue(int segmentCapacity) {
    this.segmentCapacity = segmentCapacity;
  }

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
    private final Integer[] items;
    private Segment next;
    private int first;
    private int last;

    Segment(int capacity) {
      items = new Integer[capacity];
      first = -1;
      last = -1;
    }
  }

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(LinkedArrayQueue.class).toPrintable());
    System.out.println(ClassLayout.parseClass(LinkedArrayQueue.Segment.class).toPrintable());
    System.out.println(ClassLayout.parseInstance(new Integer[16]).toPrintable());
  }
}
