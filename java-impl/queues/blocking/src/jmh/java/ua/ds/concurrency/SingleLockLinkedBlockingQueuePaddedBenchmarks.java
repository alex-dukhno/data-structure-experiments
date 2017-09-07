package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class SingleLockLinkedBlockingQueuePaddedBenchmarks {
  static final int ITEM = 10;
  static final int SIZE = Integer.MIN_VALUE >>> 16;

  @State(Scope.Group)
  public static class SamePace {
    private SingleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueuePadded();
    }

    @Benchmark
    @Group("_1W1R")
    @GroupThreads()
    public void enqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("_1W1R")
    @GroupThreads()
    public int deque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("_2W2R")
    @GroupThreads(2)
    public void twoWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("_2W2R")
    @GroupThreads(2)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("_4W4R")
    @GroupThreads(4)
    public void fourWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("_4W4R")
    @GroupThreads(4)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class ReaderLegBehind {
    private SingleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueuePadded();
      for (int i = 0; i < SIZE; i++) {
        queue.enqueue(ITEM);
      }
    }

    @Benchmark
    @Group("_1W1R")
    @GroupThreads()
    public void enqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("_1W1R")
    @GroupThreads()
    public int deque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("_2W2R")
    @GroupThreads(2)
    public void twoWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("_2W2R")
    @GroupThreads(2)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("_4W4R")
    @GroupThreads(4)
    public void fourWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("_4W4R")
    @GroupThreads(4)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }
}
