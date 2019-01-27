package ua.ds;

import java.util.OptionalInt;

public class TotalBlockingList {
  private Node head;
  private Node tail;

  public synchronized void add(int item) {
    final Node node = new Node(item);
    if (this.head == null) {
      this.head = node;
    } else {
      tail.next = node;
    }
    tail = node;
  }

  public OptionalInt remove() {
    if (head != null) {
      int item = head.item;
      Node h = head;
      head = head.next;
      if (head == null) {
        tail = null;
      }
      h.next = null;
      return OptionalInt.of(item);
    } else {
      return OptionalInt.empty();
    }
  }

  public synchronized boolean contains(int item) {
    Node node = head;
    while (node != null && node.item != item) {
      node = node.next;
    }
    return node != null && node.item == item;
  }

  public synchronized OptionalInt get(int index) {
    if (index < 0) {
      return OptionalInt.empty();
    } else {
      Node node = head;
      while (node != null && index > 0) {
        node = node.next;
        index -= 1;
      }
      return node != null ? OptionalInt.of(node.item) : OptionalInt.empty();
    }
  }

  private static class Node {
    private int item;
    private Node next;

    Node(int item) {
      this.item = item;
    }
  }
}
