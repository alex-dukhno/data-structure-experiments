package ua.ds;

import java.util.Queue;

abstract class QueueMethods {
  static final int K = 1024;
  static final int M = K * K;

  protected int[] data;

  protected final SequentialQueue enqueueOne(SequentialQueue queue) {
    queue.enqueue(data[0]);
    return queue;
  }

  protected final SequentialQueue enqueueMany(SequentialQueue queue) {
    for (int item : data) {
      queue.enqueue(item);
    }
    return queue;
  }

  protected final SequentialQueueBoxed enqueueOne(SequentialQueueBoxed queue) {
    queue.enqueue(data[0]);
    return queue;
  }

  protected final SequentialQueueBoxed enqueueMany(SequentialQueueBoxed queue) {
    for (int item : data) {
      queue.enqueue(item);
    }
    return queue;
  }

  protected final int dequeMany(SequentialQueue queue) {
    int sum = 0;
    int item;
    while ((item = queue.deque()) != -1) {
      sum += item;
    }
    return sum;
  }

  protected final Integer dequeMany(SequentialQueueBoxed queue) {
    Integer sum = 0;
    Integer item;
    while ((item = queue.deque()) != null) {
      sum += item;
    }
    return sum;
  }

  protected final Queue<Integer> enqueueMany(Queue<Integer> queue) {
    for (int item : data) {
      queue.add(item);
    }
    return queue;
  }

  protected final int dequeMany(Queue<Integer> queue) {
    Integer sum = 0;
    Integer item;
    while ((item = queue.poll()) != null) {
      sum += item;
    }
    return sum;
  }
}
