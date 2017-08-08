package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class ArrayBlockingQueueBenchmarks {

  @State(Scope.Group)
  public static class SingleWriterSingleReaderSamePace {

    private ArrayBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new ArrayBlockingQueue();
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public ArrayBlockingQueue enqueue() throws InterruptedException {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public int deque() throws InterruptedException {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }

  @State(Scope.Group)
  public static class SingleWriterSingleReaderReaderLegBehind {

    private ArrayBlockingQueue queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new ArrayBlockingQueue();
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public ArrayBlockingQueue enqueue() throws InterruptedException {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("SingleWriterSingleReader")
    @GroupThreads(1)
    @OperationsPerInvocation(256)
    public int deque() throws InterruptedException {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }

  @State(Scope.Group)
  public static class MultipleWriterMultipleReaderSamePace {

    private ArrayBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new ArrayBlockingQueue();
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public ArrayBlockingQueue twoWritersEnqueue() throws InterruptedException {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public int twoReadersDeque() throws InterruptedException {
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
    public ArrayBlockingQueue fourWritersEnqueue() throws InterruptedException {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public int fourReadersDeque() throws InterruptedException {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }

  @State(Scope.Group)
  public static class MultipleWriterMultipleReaderReadersLegBehind {

    private ArrayBlockingQueue queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new ArrayBlockingQueue();
      for (int i = 0; i < 512; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public ArrayBlockingQueue twoWritersEnqueue() throws InterruptedException {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("TwoWritersTwoReaders")
    @GroupThreads(2)
    @OperationsPerInvocation(256)
    public int twoReadersDeque() throws InterruptedException {
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
    public ArrayBlockingQueue fourWritersEnqueue() throws InterruptedException {
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @Group("FourWritersFourReaders")
    @GroupThreads(4)
    @OperationsPerInvocation(256)
    public int fourReadersDeque() throws InterruptedException {
      int sum = 0;
      for (int i = 0; i < 256; i++) {
        sum += queue.deque();
      }
      return sum;
    }
  }
}
