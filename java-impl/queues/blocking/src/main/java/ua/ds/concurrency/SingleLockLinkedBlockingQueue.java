package ua.ds.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

  public int deque() throws InterruptedException {
    lock.lock();
    try {
      while (head == null) {
        empty.await();
      }
      Node node = head;
      head = head.next;
      if (head == null) {
        tail = null;
      }
      return node.item;
    } finally {
      lock.unlock();
    }
  }
}
