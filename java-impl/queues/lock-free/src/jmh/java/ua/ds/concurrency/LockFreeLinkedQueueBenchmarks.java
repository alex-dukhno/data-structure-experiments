package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import ua.ds.LockFreeLinkedQueue;

public class LockFreeLinkedQueueBenchmarks {

  @State(Scope.Group)
  public static class SingleWriterSingleReaderSamePace {

    private LockFreeLinkedQueue queue;

    @Setup
    public void createQueue() {
      queue = new LockFreeLinkedQueue();
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public LockFreeLinkedQueue enqueue() {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public int deque() {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }

  @State(Scope.Group)
  public static class SingleWriterSingleReaderReaderLegBehind {

    private LockFreeLinkedQueue queue;

    @Setup
    public void createQueue() {
      queue = new LockFreeLinkedQueue();
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public LockFreeLinkedQueue enqueue() {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public int deque() {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }

  @State(Scope.Group)
  public static class MultipleWriterMultipleReaderSamePace {

    private LockFreeLinkedQueue queue;

    @Setup
    public void createQueue() {
      queue = new LockFreeLinkedQueue();
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public LockFreeLinkedQueue twoWritersEnqueue() {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public int twoReadersDeque() {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }


    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public LockFreeLinkedQueue fourWritersEnqueue() {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public int fourReadersDeque() {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }

  @State(Scope.Group)
  public static class MultipleWriterMultipleReaderReadersLegBehind {

    private LockFreeLinkedQueue queue;

    @Setup
    public void createQueue() {
      queue = new LockFreeLinkedQueue();
      for (int i = 0; i < 512; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public LockFreeLinkedQueue twoWritersEnqueue() {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public int twoReadersDeque() {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }


    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public LockFreeLinkedQueue fourWritersEnqueue() {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public int fourReadersDeque() {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }
}
