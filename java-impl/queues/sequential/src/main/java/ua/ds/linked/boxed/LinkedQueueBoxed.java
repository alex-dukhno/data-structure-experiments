package ua.ds.linked.boxed;

import ua.ds.SequentialQueueBoxed;

public class LinkedQueueBoxed implements SequentialQueueBoxed {
  private Node head;
  private Node tail;

  @Override
  public Integer deque() {
    Node first = head;
    if (first == null) return null;
    int item = first.item;
    if (first == tail) {
      tail = null;
    }
    head = head.next;
    first.next = null;
    return item;
  }

  @Override
  public void enqueue(Integer item) {
    Node node = new Node(item);
    if (head == null) {
      head = node;
    } else {
      tail.next = node;
    }
    tail = node;
  }

  private static class Node {
    final Integer item;
    Node next;

    Node(int item) {
      this.item = item;
    }
  }
}
