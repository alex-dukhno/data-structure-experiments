package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class BranchResizableArrayQueueBenchmark {

  @State(Scope.Benchmark)
  public static class BranchResizableArrayQueueEnqueue {

    @Benchmark
    public BranchResizableArrayQueue _00000512() {
      return enqueue(512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00001024() {
      return enqueue(2 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00002048() {
      return enqueue(4 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00004096() {
      return enqueue(8 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00008192() {
      return enqueue(16 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00016384() {
      return enqueue(32 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00032768() {
      return enqueue(64 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00065536() {
      return enqueue(128 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00131072() {
      return enqueue(256 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00262144() {
      return enqueue(512 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _00524288() {
      return enqueue(1024 * 512);
    }

    @Benchmark
    public BranchResizableArrayQueue _01048576() {
      return enqueue(2048 * 512);
    }
  }

  @State(Scope.Benchmark)
  public static class BranchResizableArrayQueueEnqueueDeque {

    @Benchmark
    public int _00000512() {
      return deque(enqueue(512), 512);
    }

    @Benchmark
    public int _00001024() {
      return deque(enqueue(2 * 512), 2 * 512);
    }

    @Benchmark
    public int _00002048() {
      return deque(enqueue(4 * 512), 4 * 512);
    }

    @Benchmark
    public int _00004096() {
      return deque(enqueue(8 * 512), 8 * 512);
    }

    @Benchmark
    public int _00008192() {
      return deque(enqueue(16 * 512), 16 * 512);
    }

    @Benchmark
    public int _00016384() {
      return deque(enqueue(32 * 512), 32 * 512);
    }

    @Benchmark
    public int _00032768() {
      return deque(enqueue(64 * 512), 64 * 512);
    }

    @Benchmark
    public int _00065536() {
      return deque(enqueue(128 * 512), 128 * 512);
    }

    @Benchmark
    public int _00131072() {
      return deque(enqueue(256 * 512), 256 * 512);
    }

    @Benchmark
    public int _00262144() {
      return deque(enqueue(512 * 512), 512 * 512);
    }

    @Benchmark
    public int _00524288() {
      return deque(enqueue(1024 * 512), 1024 * 512);
    }

    @Benchmark
    public int _01048576() {
      return deque(enqueue(2048 * 512), 2048 * 512);
    }
  }

  private static BranchResizableArrayQueue enqueue(int iterations) {
    BranchResizableArrayQueue queue = new BranchResizableArrayQueue();
    for (int i = 0; i < iterations; i++) {
      queue.enqueue(i);
    }
    return queue;
  }

  private static int deque(BranchResizableArrayQueue queue, int iterations) {
    int sum = 0;
    for (int i = 0; i < iterations; i++) {
      sum += queue.deque();
    }
    return sum;
  }
}
