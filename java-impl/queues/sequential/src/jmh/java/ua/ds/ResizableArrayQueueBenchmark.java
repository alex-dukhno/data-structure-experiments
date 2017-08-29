package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

//ua.ds.BitAndResizableArrayQueue object internals:
// OFFSET  SIZE    TYPE DESCRIPTION                               VALUE
//      0    12         (object header)                           N/A
//     12     4     int BitAndResizableArrayQueue.head            N/A
//     16     4     int BitAndResizableArrayQueue.tail            N/A
//     20     4     int BitAndResizableArrayQueue.mask            N/A
//     24     4     int BitAndResizableArrayQueue.capacity        N/A
//     28     4   int[] BitAndResizableArrayQueue.items           N/A
//     Instance size: 32 bytes
//     Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
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
@State(Scope.Benchmark)
public class ResizableArrayQueueBenchmark {

  @Param({"512", "1024", "2048", "4096", "8192", "16384", "32768", "65536", "131072", "262144", "524288", "1048576"})
  private int iterations;

  @Benchmark
  public BitAndResizableArrayQueue enqueue_bit_and() {
    BitAndResizableArrayQueue queue = new BitAndResizableArrayQueue();
    for (int i = 0; i < iterations; i++) {
      queue.enqueue(i);
    }
    return queue;
  }

  @Benchmark
  public int deque_bit_and() {
    BitAndResizableArrayQueue queue = enqueue_bit_and();
    int sum = 0;
    for (int i = 0; i < iterations; i++) {
      sum += queue.deque();
    }
    return sum;
  }

  @Benchmark
  public BranchResizableArrayQueue enqueue_if_branch() {
    BranchResizableArrayQueue queue = new BranchResizableArrayQueue();
    for (int i = 0; i < iterations; i++) {
      queue.enqueue(i);
    }
    return queue;
  }

  @Benchmark
  public int deque_if_branch() {
    BranchResizableArrayQueue queue = enqueue_if_branch();
    int sum = 0;
    for (int i = 0; i < iterations; i++) {
      sum += queue.deque();
    }
    return sum;
  }
}
