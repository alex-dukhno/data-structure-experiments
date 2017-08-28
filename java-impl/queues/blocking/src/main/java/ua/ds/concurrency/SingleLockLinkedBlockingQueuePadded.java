package ua.ds.concurrency;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class SingleLockHead {
  Node head;
}

abstract class SingleLockHeadPadding extends SingleLockHead {
  long p01, p02, p03, p04, p05, p06, p07;
  long p10, p11, p12, p13, p14, p15, p16, p17;
}

abstract class SingleLockTail extends SingleLockHeadPadding {
  Node tail;
}

abstract class SingleLockTailPadding extends SingleLockTail {
  long p01, p02, p03, p04, p05, p06, p07;
  long p10, p11, p12, p13, p14, p15, p16, p17;
}

public class SingleLockLinkedBlockingQueuePadded extends SingleLockTailPadding {
  private final Lock lock;
  private final Condition empty;


  public SingleLockLinkedBlockingQueuePadded() {
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

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(SingleLockLinkedBlockingQueuePadded.class).toPrintable());
  }
}

class Node {
  final int item;
  Node next;

  Node(int item) {
    this.item = item;
  }
}
