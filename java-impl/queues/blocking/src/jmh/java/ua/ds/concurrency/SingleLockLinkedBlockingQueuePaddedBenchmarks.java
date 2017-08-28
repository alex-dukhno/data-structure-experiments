package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class SingleLockLinkedBlockingQueuePaddedBenchmarks {
  static final int ITEM = 10;
  static final int SIZE = 32_768;

  @State(Scope.Group)
  public static class SingleLockLinkedBlockingQueuePaddedSWSRSamePace {
    private SingleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueuePadded();
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads()
    public void enqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads()
    public int deque() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class SingleLockLinkedBlockingQueuePaddedSWSRReaderLegBehind {
    private SingleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueuePadded();
      for (int i = 0; i < SIZE; i++) {
        queue.enqueue(ITEM);
      }
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads()
    public void enqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads()
    public int deque() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class SingleLockLinkedBlockingQueuePaddedMWMRReaderSamePace {
    private SingleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueuePadded();
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    public void twoWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public void fourWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class SingleLockLinkedBlockingQueuePaddedMWMRReadersLegBehind {
    private SingleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueuePadded();
      for (int i = 0; i < SIZE; i++) {
        queue.enqueue(ITEM);
      }
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public void twoWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public void fourWritersEnqueue() {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }
}
