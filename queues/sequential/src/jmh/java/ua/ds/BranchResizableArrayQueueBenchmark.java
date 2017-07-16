package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class BranchResizableArrayQueueBenchmark {

  @State(Scope.Benchmark)
  public static class Enqueueing {

    @Benchmark
    @OperationsPerInvocation(4096)
    public BranchResizableArrayQueue enqueue_LessThanL1Size() {
      return enqueue(4096);
    }

    @Benchmark
    @OperationsPerInvocation(8192)
    public BranchResizableArrayQueue enqueue__ExactAsL1Size() {
      return enqueue(8192);
    }

    @Benchmark
    @OperationsPerInvocation(16384)
    public BranchResizableArrayQueue enqueue___MoreThanL1Size() {
      return enqueue(16384);
    }

    @Benchmark
    @OperationsPerInvocation(32768)
    public BranchResizableArrayQueue enqueue_LessThanL2Size() {
      return enqueue(32768);
    }

    @Benchmark
    @OperationsPerInvocation(65536)
    public BranchResizableArrayQueue enqueue__ExactAsL2Size() {
      return enqueue(65536);
    }

    @Benchmark
    @OperationsPerInvocation(131072)
    public BranchResizableArrayQueue enqueue___MoreThanL2Size() {
      return enqueue(131072);
    }

    @Benchmark
    @OperationsPerInvocation(1048576)
    public BranchResizableArrayQueue enqueue_LessThanL3Size() {
      return enqueue(1048576);
    }

    @Benchmark
    @OperationsPerInvocation(2097152)
    public BranchResizableArrayQueue enqueue__ExactAsL3Size() {
      return enqueue(2097152);
    }

    @Benchmark
    @OperationsPerInvocation(4194304)
    public BranchResizableArrayQueue enqueue___MoreThanL3Size() {
      return enqueue(4194304);
    }

    private BranchResizableArrayQueue enqueue(int initialSize) {
      BranchResizableArrayQueue queue = new BranchResizableArrayQueue();
      for (int i = 0; i < initialSize - 1; i++) {
        queue.enqueue(i);
      }
      return queue;
    }

    @Benchmark
    @OperationsPerInvocation(4096)
    public BranchResizableArrayQueue preallocateEnqueue_LessThanL1Size() {
      return preallocateEnqueue(4096);
    }

    @Benchmark
    @OperationsPerInvocation(8192)
    public BranchResizableArrayQueue preallocateEnqueue__ExactAsL1Size() {
      return preallocateEnqueue(8192);
    }

    @Benchmark
    @OperationsPerInvocation(16384)
    public BranchResizableArrayQueue preallocateEnqueue___MoreThanL1Size() {
      return preallocateEnqueue(16384);
    }

    @Benchmark
    @OperationsPerInvocation(32768)
    public BranchResizableArrayQueue preallocateEnqueue_LessThanL2Size() {
      return preallocateEnqueue(32768);
    }

    @Benchmark
    @OperationsPerInvocation(65536)
    public BranchResizableArrayQueue preallocateEnqueue__ExactAsL2Size() {
      return preallocateEnqueue(65536);
    }

    @Benchmark
    @OperationsPerInvocation(131072)
    public BranchResizableArrayQueue preallocateEnqueue___MoreThanL2Size() {
      return preallocateEnqueue(131072);
    }

    @Benchmark
    @OperationsPerInvocation(1048576)
    public BranchResizableArrayQueue preallocateEnqueue_LessThanL3Size() {
      return preallocateEnqueue(1048576);
    }

    @Benchmark
    @OperationsPerInvocation(2097152)
    public BranchResizableArrayQueue preallocateEnqueue__ExactAsL3Size() {
      return preallocateEnqueue(2097152);
    }

    @Benchmark
    @OperationsPerInvocation(4194304)
    public BranchResizableArrayQueue preallocateEnqueue___MoreThanL3Size() {
      return preallocateEnqueue(4194304);
    }

    private BranchResizableArrayQueue preallocateEnqueue(int preallocateSize) {
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
        "1048576", "2097152", "4194304"
    })
    private int capacity;

    private BranchResizableArrayQueue queue;

    @Setup
    public void createQueue() {
      queue = new BranchResizableArrayQueue(capacity);
      for (int i = 0; i < capacity - 1; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @OperationsPerInvocation(64)
    public int enqueueDeque64() {
      return enqueueDeque(64);
    }

    @Benchmark
    @OperationsPerInvocation(256)
    public int enqueueDeque256() {
      return enqueueDeque(256);
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public int enqueueDeque1024() {
      return enqueueDeque(1024);
    }

    @Benchmark
    @OperationsPerInvocation(4096)
    public int enqueueDeque4096() {
      return enqueueDeque(4096);
    }

    int enqueueDeque(int iterations) {
      int sum = 0;
      for (int i = 0; i < iterations - 1; i++) {
        sum += queue.deque();
        queue.enqueue(i);
      }
      return sum;
    }
  }
}
