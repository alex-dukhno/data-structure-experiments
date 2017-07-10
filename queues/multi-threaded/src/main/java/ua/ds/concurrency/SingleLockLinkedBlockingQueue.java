package ua.ds.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.openjdk.jol.info.ClassLayout;

public class SingleLockLinkedBlockingQueue {

  private final Lock lock;
  private final Condition empty;
  private Node head;
  private Node tail;

  public SingleLockLinkedBlockingQueue() {
    lock = new ReentrantLock();
    empty = lock.newCondition();
    head = null;
    tail = null;
  }

  public void enqueue(int item) {
    lock.lock();
    try {
      Node node = new Node(item);
      if (tail != null) {
        tail.next = node;
      } else {
        head = node;
        empty.signalAll();
      }
      tail = node;
    } finally {
      lock.unlock();
    }
  }

  public int deque() {
    lock.lock();
    try {
      while (head == null) {
        try {
          empty.await();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      int item = head.item;
      head = head.next;
      if (head == null) {
        tail = null;
      }
      return item;
    } finally {
      lock.unlock();
    }
  }


  private static class Node {

    private final int item;
    private Node next;

    Node(int item) {
      this.item = item;
    }
  }

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(SingleLockLinkedBlockingQueue.class).toPrintable());
  }
}
