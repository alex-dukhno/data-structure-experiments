package ua.ds;

public interface SequentialQueue {
  int MIN_CAPACITY = 1 << 4;
  int MAX_CAPACITY = 1 << 30;

  static int nextPowerOfTwo(int n) {
    n = n - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? MIN_CAPACITY : (n >= MAX_CAPACITY) ? MAX_CAPACITY : n + 1;
  }

  void enqueue(int item);

  int deque();
}
