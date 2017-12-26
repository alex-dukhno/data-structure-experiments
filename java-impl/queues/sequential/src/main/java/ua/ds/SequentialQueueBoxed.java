package ua.ds;

import ua.ds.array.ArrayQueue;

public interface SequentialQueueBoxed extends ArrayQueue {

  void enqueue(Integer item);

  Integer deque();
}
