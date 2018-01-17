package ua.ds.linked.primitive;

import ua.ds.SequentialQueue;

public abstract class AbstractLinkedQueuePrimitive implements SequentialQueue {
  private Node head;
  private Node tail;

  @Override
  public int deque() {
    if (head == null) return -1;
    int item = head.item;
    Node first = head;
    head = head.next;
    first.next = null;
    if (head == null) {
      tail = null;
    }
    return item;
  }

  @Override
  public void enqueue(int item) {
    Node node = newNode();
    node.item = item;
    if (head == null) {
      head = node;
    } else {
      tail.next = node;
    }
    tail = node;
  }

  protected abstract Node newNode();
}
