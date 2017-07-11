package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

//    Array Queue layout
//    ua.ds.ArrayQueue object internals:
//     OFFSET  SIZE    TYPE DESCRIPTION       VALUE
//          0    12         (object header)   N/A
//         12     4     int ArrayQueue.head   N/A
//         16     4     int ArrayQueue.tail   N/A
//         20     4     int ArrayQueue.mask   N/A
//         24     4   int[] ArrayQueue.items  N/A
//         28     4         (loss due to the next object alignment)
//    Instance size: 32 bytes
//    Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
//
//    Intel(R) Core(TM) i5-5257U CPU @ 2.70 GHz, 2 Core(s), 4 Logical Processor(s)
//          cache sizes                 queue sizes
//    L1D   32KB     32768      4096      8192     16384
//    L2   256KB    262144     32768     65536    131072
//    L3     3MB   3145728    524288   1048576   2097152
//
//    Intel(R) Core(TM) i7-3770 CPU @ 3.40GHz, 4 Core(s), 8 Logical Processor(s)
//          cache sizes                 queue sizes
//    L1D   32KB       32768     4096      8192     16384
//    L2   256KB      262144    32768     65536    131072
//    L3     8MB     8388608  1048576   2097152   4194304
public class ArrayQueueBenchmark {

  @State(Scope.Benchmark)
  public static class Enqueueing {

    @Benchmark
    @OperationsPerInvocation(4096)
    public ArrayQueue enqueue_LessThanL1Size() {
      return enqueue(4096);
    }

    @Benchmark
    @OperationsPerInvocation(8192)
    public ArrayQueue enqueue__ExactAsL1Size() {
      return enqueue(8192);
    }

    @Benchmark
    @OperationsPerInvocation(16384)
    public ArrayQueue enqueue___MoreThanL1Size() {
      return enqueue(16384);
    }

    @Benchmark
    @OperationsPerInvocation(32768)
    public ArrayQueue enqueue__LessThanL2Size() {
      return enqueue(32768);
    }

    @Benchmark
    @OperationsPerInvocation(65536)
    public ArrayQueue enqueue__ExactAsL2Size() {
      return enqueue(65536);
    }

    @Benchmark
    @OperationsPerInvocation(131072)
    public ArrayQueue enqueue___MoreThanL2Size() {
      return enqueue(131072);
    }

    @Benchmark
    @OperationsPerInvocation(1048576)
    public ArrayQueue enqueue_LessThanL3Size() {
      return enqueue(1048576);
    }

    @Benchmark
    @OperationsPerInvocation(2097152)
    public ArrayQueue enqueue__ExactAsL3Size() {
      return enqueue(2097152);
    }

    @Benchmark
    @OperationsPerInvocation(4194304)
    public ArrayQueue enqueue___MoreThanL3Size() {
      return enqueue(4194304);
    }

    private ArrayQueue enqueue(int items) {
      ArrayQueue queue = new ArrayQueue(items);
      for (int i = 0; i < items - 1; i++) {
        queue.enqueue(i);
      }
      return queue;
    }
  }

  @State(Scope.Benchmark)
  public static class EnqueueingDequeueing {

    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "1048576", "2097152", "4194304"
    })
    private int capacity;

    private ArrayQueue queue;

    @Setup
    public void createQueue() {
      queue = new ArrayQueue(capacity);
      for (int i = 0; i < capacity - 1; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @OperationsPerInvocation(64)
    public int enqueueDeque0064() {
      return enqueueDeque(64);
    }

    @Benchmark
    @OperationsPerInvocation(256)
    public int enqueueDeque0256() {
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

    private int enqueueDeque(int iterations) {
      int sum = 0;
      for (int i = 0; i < iterations; i++) {
        sum += queue.deque();
        queue.enqueue(i);
      }
      return sum;
    }
  }
}
