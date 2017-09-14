package ua.ds;

public class LinkedQueue implements SequentialQueue {
  private Node head;
  private Node tail;

  @Override
  public int deque() {
    if (head == null) return -1;
    int item = head.item;
    if (head == tail) {
      tail = null;
    }
    head = head.next;
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
