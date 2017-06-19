package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class BranchResizableArrayQueueBenchmark {
  @State(Scope.Benchmark)
  public static class EnqueueingNoPreallocate {
    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "524288", "1048576", "2097152"
    })
    private int preallocateSize;

    @Benchmark
    public BranchResizableArrayQueue enqueue() {
      BranchResizableArrayQueue queue = new BranchResizableArrayQueue();
      for (int i = 0; i < preallocateSize - 1; i++) {
        queue.enqueue(i);
      }
      return queue;
    }
  }

  @State(Scope.Benchmark)
  public static class EnqueueingPreallocate {
    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "524288", "1048576", "2097152"
    })
    private int preallocateSize;

    @Benchmark
    public BranchResizableArrayQueue enqueue() {
      BranchResizableArrayQueue queue = new BranchResizableArrayQueue(preallocateSize);
      for (int i = 0; i < preallocateSize - 1; i++) {
        queue.enqueue(i);
      }
      return queue;
    }
  }

  @State(Scope.Benchmark)
  public static class EnqueueingDequeing {
    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "524288", "1048576", "2097152"
    })
    private int preallocateSize;

    private BranchResizableArrayQueue queue;

    @Setup
    public void createQueue() {
      queue = new BranchResizableArrayQueue(preallocateSize);
      for (int i = 0; i < preallocateSize - 1; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    public int enqueue() {
      int sum = 0;
      for (int i = 0; i < preallocateSize - 1; i++) {
        sum += queue.deque();
        queue.enqueue(i);
      }
      return sum;
    }
  }
}
