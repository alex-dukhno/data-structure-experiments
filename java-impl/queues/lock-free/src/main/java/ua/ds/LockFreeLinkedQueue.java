package ua.ds;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeLinkedQueue {
  //using this static variable leads to strange results ...
//  private static final Node SENTINEL = new Node(-1);

  private final AtomicReference<Node> head
//    = new AtomicReference<>(SENTINEL)
    ;
  private final AtomicReference<Node> tail
//    = new AtomicReference<>(SENTINEL)
    ;

  public LockFreeLinkedQueue() {
    Node node = new Node(-1);
    head = new AtomicReference<>(node);
    tail = new AtomicReference<>(node);
  }

  public int deque() {
    while (true) {
      final Node first = head.get();
      final Node last = tail.get();
      final Node next = first.next.get();
      if (first == head.get()) {
        if (first == last) {
          if (next == null) {
            return -1;
          }
          tail.compareAndSet(last, next);
        } else {
          int item = next.item;
          if (head.compareAndSet(first, next)) {
            return item;
          }
        }
      }
    }
  }

  public void enqueue(int item) {
    final Node node = new Node(item);
    for (;;) {
      final Node last = tail.get();
      final Node next = last.next.get();
      if (last == tail.get()) {
        if (next == null) {
          if (last.next.compareAndSet(next, node)) {
            tail.compareAndSet(last, node);
            return;
          }
        } else {
          tail.compareAndSet(last, next);
        }
      }
    }
  }

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(LockFreeLinkedQueue.class).toPrintable());
    System.out.println(ClassLayout.parseClass(Node.class).toPrintable());
  }

  static class Node {
    final int item;
    final AtomicReference<Node> next;

    Node(int item) {
      this.item = item;
      this.next = new AtomicReference<>(null);
    }
  }
}
