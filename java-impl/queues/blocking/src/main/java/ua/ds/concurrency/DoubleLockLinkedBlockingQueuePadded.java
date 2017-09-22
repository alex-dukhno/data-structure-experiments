package ua.ds.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class DoubleLockHead {

  final Lock deque = new ReentrantLock();
  final Condition empty = deque.newCondition();
  Node head;
}

abstract class DoubleLockHeadPadding extends DoubleLockHead {

  long p01, p02, p03, p04, p05, p06, p07;
  long p10, p11, p12, p13, p14, p15, p16, p17;
}

abstract class DoubleLockTail extends DoubleLockHeadPadding {

  final Lock enqueue = new ReentrantLock();
  final Condition full = enqueue.newCondition();
  Node tail;
}

abstract class DoubleLockTailPadding extends DoubleLockTail {

  long p01, p02, p03, p04, p05, p06, p07;
  long p10, p11, p12, p13, p14, p15, p16, p17;
}

public class DoubleLockLinkedBlockingQueuePadded extends DoubleLockTailPadding {

  private static final int MAX_CAPACITY = Integer.MIN_VALUE >>> 1;
  //size must be atomic to guarantee that 'Reader' will see changes made by 'Writer'
  //because 'Reader's and 'Writer's use different locks and different references
  private final AtomicInteger size;

  public DoubleLockLinkedBlockingQueuePadded() {
    ;
    head = tail = new Node(Integer.MIN_VALUE);
    size = new AtomicInteger();
  }

  public void enqueue(int item) throws InterruptedException {
    int count;
    Node node = new Node(item);
    enqueue.lock();
    try {
      while (size.get() == MAX_CAPACITY) {
        full.await();
      }
      tail = tail.next = node;
      count = size.getAndIncrement();
      if (count + 1 < MAX_CAPACITY) {
        full.signalAll();
      }
    } finally {
      enqueue.unlock();
    }
    if (count == 0) {
      deque.lock();
      try {
        empty.signalAll();
      } finally {
        deque.unlock();
      }
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
    if (count == MAX_CAPACITY) {
      enqueue.lock();
      try {
        full.signalAll();
      } finally {
        enqueue.unlock();
      }
    }
    return result;
  }
}
