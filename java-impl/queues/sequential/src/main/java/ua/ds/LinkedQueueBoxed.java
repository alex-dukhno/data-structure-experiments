package ua.ds;

public class LinkedQueueBoxed implements SequentialQueueBoxed {
  private Node head;
  private Node tail;

  @Override
  public Integer deque() {
    if (head == null) return null;
    Integer item = head.item;
    if (head == tail) {
      tail = null;
    }
    head = head.next;
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
