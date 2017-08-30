package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class DoubleLockLinkedBlockingQueuePaddedBenchmarks {
  static final int ITEM = 10;
  static final int SIZE = Integer.MIN_VALUE >>> 2;

  @State(Scope.Group)
  public static class SamePace {
    private DoubleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new DoubleLockLinkedBlockingQueuePadded();
    }

    @Benchmark
    @Group("1W1R")
    @GroupThreads()
    public void enqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("1W1R")
    @GroupThreads()
    public int deque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("2W2R")
    @GroupThreads(2)
    public void twoWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("2W2R")
    @GroupThreads(2)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("4W4R")
    @GroupThreads(4)
    public void fourWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("4W4R")
    @GroupThreads(4)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class ReaderLegBehind {
    private DoubleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new DoubleLockLinkedBlockingQueuePadded();
      for (int i = 0; i < SIZE; i++) {
        queue.enqueue(ITEM);
      }
    }

    @Benchmark
    @Group("1W1R")
    @GroupThreads()
    public void enqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("1W1R")
    @GroupThreads()
    public int deque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("2W2R")
    @GroupThreads(2)
    public void twoWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("2W2R")
    @GroupThreads(2)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("4W4R")
    @GroupThreads(4)
    public void fourWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("4W4R")
    @GroupThreads(4)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }
}
