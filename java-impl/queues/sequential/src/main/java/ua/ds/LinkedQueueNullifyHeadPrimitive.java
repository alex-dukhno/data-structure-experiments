package ua.ds;

public class LinkedQueueNullifyHeadPrimitive implements SequentialQueue {
  private Node head;
  private Node tail;

  @Override
  public int deque() {
    Node first = head;
    if (first == null) return -1;
    int item = first.item;
    if (first == tail) {
      tail = null;
    }
    head = head.next;
    first.next = null;
    return item;
  }

  @Override
  public void enqueue(int item) {
    Node node = new Node(item);
    if (head == null) {
      head = node;
    } else {
      tail.next = node;
    }
    tail = node;
  }

  private static class Node {
    final int item;
    Node next;

    Node(int item) {
      this.item = item;
    }
  }
}
