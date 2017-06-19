package ua.ds;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class LinkedQueue {
  private Node head;
  private Node tail;

  public Integer deque() {
    if (head == null) return -1;
    int item = head.item;
    if (head == tail) {
      tail = null;
    }
    head = head.next;
    return item;
  }

  public void enqueue(int item) {
    Node node = new Node(item);
    if (head == null) {
      head = node;
    } else {
      tail.next = node;
    }
    tail = node;
  }

  private class Node {
    private final int item;
    private Node next;

    Node(int item) {
      this.item = item;
    }
  }

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(LinkedQueue.class).toPrintable());
    System.out.println(ClassLayout.parseClass(LinkedQueue.Node.class).toPrintable());
  }
}
