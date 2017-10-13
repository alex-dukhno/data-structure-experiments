package ua.ds;

import org.openjdk.jol.info.ClassLayout;

public class LinkedQueuePrimitivePooled implements SequentialQueue {
  private final Pool nodePool;
  private Node head;
  private Node tail;

  public LinkedQueuePrimitivePooled(Pool nodePool) {
    this.nodePool = nodePool;
  }

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
    Node node = nodePool.getNextNode();
    node.item = item;
    if (head == null) {
      head = node;
    } else {
      tail.next = node;
    }
    tail = node;
  }

  public static class Node {
    int item;
    Node next;
  }

  public interface Pool {
    Node getNextNode();
  }

  public static class SimplePool implements Pool {
    private final Node[] nodes;
    private int index;

    public SimplePool(int size) {
      nodes = new Node[size];
    }

    @Override
    public Node getNextNode() {
      Node node = new Node();
      nodes[index++] = node;
      return node;
    }
  }

  public static class PreInitializedPool implements Pool {
    private final Node[] nodes;
    private int index;

    public PreInitializedPool(int size) {
      nodes = new Node[size];
      for (int i = 0; i < size; i++) {
        nodes[i] = new Node();
      }
    }

    @Override
    public Node getNextNode() {
      return nodes[index++];
    }
  }

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(LinkedQueuePrimitive.class).toPrintable());
  }
}
