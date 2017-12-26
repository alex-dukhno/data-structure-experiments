package ua.ds.linked.primitive;

import ua.ds.SequentialQueue;

public abstract class AbstractLinkedQueuePrimitive implements SequentialQueue {
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
