package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

//    ua.ds.LinkedQueue object internals:
//    OFFSET  SIZE                     TYPE DESCRIPTION                           VALUE
//         0    12                          (object header)                           N/A
//        12     4   ua.ds.LinkedQueue.Node LinkedQueue.head                          N/A
//        16     4   ua.ds.LinkedQueue.Node LinkedQueue.tail                          N/A
//        20     4                          (loss due to the next object alignment)
//    Instance size: 24 bytes
//    Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
//
//    ua.ds.LinkedQueue$Node object internals:
//    OFFSET  SIZE                     TYPE DESCRIPTION                           VALUE
//         0    12                          (object header)                           N/A
//        12     4                      int Node.item                                 N/A
//        16     4   ua.ds.LinkedQueue.Node Node.next                                 N/A
//        20     4        ua.ds.LinkedQueue Node.this$0                               N/A
//    Instance size: 24 bytes
//    Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
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
public class LinkedQueueBenchmark {

  @State(Scope.Benchmark)
  public static class Enqueueing {

    @Benchmark
    @OperationsPerInvocation(1024)
    public LinkedQueue enqueueLessThanL1Size() {
      return enqueue(1024);
    }

    @Benchmark
    @OperationsPerInvocation(1364)
    public LinkedQueue enqueueExactAsL1Size() {
      return enqueue(1364);
    }

    @Benchmark
    @OperationsPerInvocation(2048)
    public LinkedQueue enqueueMoreThanL1Size() {
      return enqueue(2048);
    }

    @Benchmark
    @OperationsPerInvocation(8192)
    public LinkedQueue enqueueLessThanL2Size() {
      return enqueue(8192);
    }

    @Benchmark
    @OperationsPerInvocation(10921)
    public LinkedQueue enqueueExactAsL2Size() {
      return enqueue(10921);
    }

    @Benchmark
    @OperationsPerInvocation(16384)
    public LinkedQueue enqueueMoreThanL2Size() {
      return enqueue(16384);
    }

    @Benchmark
    @OperationsPerInvocation(131072)
    public LinkedQueue enqueueLessThanL3Size() {
      return enqueue(131072);
    }

    @Benchmark
    @OperationsPerInvocation(349524)
    public LinkedQueue enqueueExactAsL3Size() {
      return enqueue(349524);
    }

    @Benchmark
    @OperationsPerInvocation(524288)
    public LinkedQueue enqueueMoreThanL3Size() {
      return enqueue(524288);
    }

    private LinkedQueue enqueue(int iterations) {
      LinkedQueue queue = new LinkedQueue();
      for (int i = 0; i < iterations; i++) {
        queue.enqueue(i);
      }
      return queue;
    }
  }

  @State(Scope.Benchmark)
  public static class EnqueueingDequeing {

    @Param({
        "1024", "1364", "2048",     //L1D
        "8192", "10921", "16384",   //L2
        "65536", "131071", "262144" //L3
    })
    private int initialSize;

    private LinkedQueue queue;

    @Setup
    public void createQueue() {
      queue = new LinkedQueue();
      for (int i = 0; i < initialSize; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public int enqueueDeque1024() {
      return enqueueDeque(1024);
    }

    @Benchmark
    @OperationsPerInvocation(1364)
    public int enqueueDeque1364() {
      return enqueueDeque(1364);
    }

    @Benchmark
    @OperationsPerInvocation(2048)
    public int enqueueDeque2048() {
      return enqueueDeque(2048);
    }

    @Benchmark
    @OperationsPerInvocation(8192)
    public int enqueueDeque8192() {
      return enqueueDeque(8192);
    }

    @Benchmark
    @OperationsPerInvocation(10921)
    public int enqueueDeque10921() {
      return enqueueDeque(10921);
    }

    @Benchmark
    @OperationsPerInvocation(16384)
    public int enqueueDeque16384() {
      return enqueueDeque(16384);
    }

    @Benchmark
    @OperationsPerInvocation(65536)
    public int enqueueDeque65536() {
      return enqueueDeque(65536);
    }

    @Benchmark
    @OperationsPerInvocation(131071)
    public int enqueueDeque131071() {
      return enqueueDeque(131071);
    }

    @Benchmark
    @OperationsPerInvocation(262144)
    public int enqueueDeque262144() {
      return enqueueDeque(262144);
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
