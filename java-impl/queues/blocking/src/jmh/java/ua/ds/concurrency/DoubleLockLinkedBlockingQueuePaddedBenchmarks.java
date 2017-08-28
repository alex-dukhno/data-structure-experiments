package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class DoubleLockLinkedBlockingQueuePaddedBenchmarks {
  static final int ITEM = 10;
  static final int SIZE = 32_768;

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueuePaddedSWSRSamePace {
    private DoubleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new DoubleLockLinkedBlockingQueuePadded();
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads()
    public void enqueue() throws InterruptedException {
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
  public static class DoubleLockLinkedBlockingQueuePaddedSWSRReaderLegBehind {
    private DoubleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new DoubleLockLinkedBlockingQueuePadded();
      for (int i = 0; i < SIZE; i++) {
        queue.enqueue(ITEM);
      }
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads()
    public void enqueue() throws InterruptedException {
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
  public static class DoubleLockLinkedBlockingQueuePaddedMWMRReaderSamePace {
    private DoubleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() {
      queue = new DoubleLockLinkedBlockingQueuePadded();
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    public void twoWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    public void fourWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueuePaddedMWMRReadersLegBehind {
    private DoubleLockLinkedBlockingQueuePadded queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new DoubleLockLinkedBlockingQueuePadded();
      for (int i = 0; i < SIZE; i++) {
        queue.enqueue(ITEM);
      }
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    public void twoWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    public int twoReadersDeque() throws InterruptedException {
      return queue.deque();
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    public void fourWritersEnqueue() throws InterruptedException {
      queue.enqueue(ITEM);
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    public int fourReadersDeque() throws InterruptedException {
      return queue.deque();
    }
  }
}
