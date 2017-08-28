package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class SingleLockLinkedBlockingQueueBenchmarks {
  static final int ITEM = 10;
  static final int SIZE = 32_768;

  @State(Scope.Group)
  public static class SWSRSamePace {
    private SingleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueue();
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
  public static class SWSRReaderLegBehind {
    private SingleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueue();
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
  public static class MWMRReaderSamePace {
    private SingleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueue();
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
  public static class MWMRReadersLegBehind {
    private SingleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new SingleLockLinkedBlockingQueue();
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
