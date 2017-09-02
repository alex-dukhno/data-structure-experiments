package ua.ds.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DoubleLockLinkedBlockingQueue {
  private final Lock enqueue;
  private final Condition empty;
  private final Lock deque;
  private final Condition full;
  private final int capacity;
  //size must be atomic to guarantee that 'Reader' will see changes made by 'Writer'
  //because 'Reader's and 'Writer's use different locks and different references
  private final AtomicInteger size;
  private Node head;
  private Node tail;

  public DoubleLockLinkedBlockingQueue() {
    enqueue = new ReentrantLock();
    full = enqueue.newCondition();
    deque = new ReentrantLock();
    empty = deque.newCondition();
    capacity = Integer.MIN_VALUE >>> 1;
    head = tail = new Node(Integer.MIN_VALUE);
    size = new AtomicInteger();
  }

  public void enqueue(int item) throws InterruptedException {
    int count;
    Node node = new Node(item);
    enqueue.lockInterruptibly();
    try {
      while (size.get() == capacity) {
        full.await();
      }
      tail = tail.next = node;
      count = size.getAndIncrement();
      if (count + 1 < capacity) {
        full.signalAll();
      }
    } finally {
      enqueue.unlock();
    }
    if (count == 0) {
      notify(deque, empty);
    }
  }

  private void notify(Lock lock, Condition condition) {
    lock.lock();
    try {
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public int deque() throws InterruptedException {
    int result;
    int count;
    // is it a safe-point ?
    deque.lockInterruptibly();
    try {
      while (size.get() == 0) {
        // is it a safe-point ?
        empty.await();
      }
      Node first = head.next;
      // We need to cycle node to itself to be sure that GC would not follow the 'next' item in the
      // queue those iterating over all other references (again?)
      result = first.item;
      head = first;
      count = size.getAndDecrement();
      //if only 'Reader's have left on processing of the queue each reader who successfully deque item
      // must notify all other 'Reader's in order to avoid program freezing
      if (count > 1) {
        // is it a safe-point ?
        empty.signalAll();
      }
    } finally {
      // is it a safe-point ?
      deque.unlock();
    }
    if (count == capacity) {
      notify(enqueue, full);
    }
    return result;
  }
}
