package ua.ds.concurrency;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class DoubleLockLinkedBlockingQueueBenchmarks {

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueueSWSRSamePace {

    private DoubleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new DoubleLockLinkedBlockingQueue();
    }

    @Benchmark
    @Group("_1R_1W")
    @GroupThreads(1)
    public void writer() throws InterruptedException {
      queue.enqueue(10);
    }

    @Benchmark
    @Group("_1R_1W")
    @GroupThreads(1)
    public int reader() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueueSWSRReaderLegBehind {

    private DoubleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new DoubleLockLinkedBlockingQueue();
      for (int i = 0; i < 256; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @Group("_1R_1W")
    @GroupThreads(1)
    public void writer() throws InterruptedException {
      queue.enqueue(10);
    }

    @Benchmark
    @Group("_1R_1W")
    @GroupThreads(1)
    public int reader() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueue2W2RSamePace {

    private DoubleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new DoubleLockLinkedBlockingQueue();
    }

    @Benchmark
    @Group("_2R_2W")
    @GroupThreads(2)
    public void writer() throws InterruptedException {
      queue.enqueue(10);
    }

    @Benchmark
    @Group("_2R_2W")
    @GroupThreads(2)
    public int reader() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueue4W4RSamePace {

    private DoubleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() {
      queue = new DoubleLockLinkedBlockingQueue();
    }

    @Benchmark
    @Group("_4R_4W")
    @GroupThreads(4)
    public void writer() throws InterruptedException {
      queue.enqueue(10);
    }

    @Benchmark
    @Group("_4R_4W")
    @GroupThreads(4)
    public int reader() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueue2W2RReadersLegBehind {

    private DoubleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new DoubleLockLinkedBlockingQueue();
      for (int i = 0; i < 2 * 256; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @Group("_2R_2W")
    @GroupThreads(2)
    public void writer() throws InterruptedException {
      queue.enqueue(10);
    }

    @Benchmark
    @Group("_2R_2W")
    @GroupThreads(2)
    public int reader() throws InterruptedException {
      return queue.deque();
    }
  }

  @State(Scope.Group)
  public static class DoubleLockLinkedBlockingQueue4W4RReadersLegBehind {

    private DoubleLockLinkedBlockingQueue queue;

    @Setup
    public void createQueue() throws InterruptedException {
      queue = new DoubleLockLinkedBlockingQueue();
      for (int i = 0; i < 4 * 256; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @Group("_4R_4W")
    @GroupThreads(4)
    public void writer() throws InterruptedException {
      queue.enqueue(10);
    }

    @Benchmark
    @Group("_4R_4W")
    @GroupThreads(4)
    public int reader() throws InterruptedException {
        return queue.deque();
    }
  }
}
