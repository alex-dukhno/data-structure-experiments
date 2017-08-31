package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

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
public class LinkedQueueBenchmark {

  @Param({"1024", "8192", "262144", "1048576"})
  private int size;

  @Benchmark
  public LinkedQueue enqueue() {
    LinkedQueue queue = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      queue.enqueue(i);
    }
    return queue;
  }

  private LinkedQueue queue;

  @Setup
  public void createQueue() {
    queue = enqueue();
  }

  @Benchmark
  public void deque(Blackhole blackhole) {
    for (int i = 0; i < size; i++) {
      blackhole.consume(queue.deque());
    }
  }
}
