package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
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
public class BitAndResizableArrayQueueBenchmark {

  @State(Scope.Benchmark)
  public static class BitAndResizableArrayQueueEnqueue {

    @Benchmark
    public BitAndResizableArrayQueue _00000512() {
      return enqueue(512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00001024() {
      return enqueue(2 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00002048() {
      return enqueue(4 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00004096() {
      return enqueue(8 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00008192() {
      return enqueue(16 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00016384() {
      return enqueue(32 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00032768() {
      return enqueue(64 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00065536() {
      return enqueue(128 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00131072() {
      return enqueue(256 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00262144() {
      return enqueue(512 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _00524288() {
      return enqueue(1024 * 512);
    }

    @Benchmark
    public BitAndResizableArrayQueue _01048576() {
      return enqueue(2048 * 512);
    }
  }

  @State(Scope.Benchmark)
  public static class BitAndResizableArrayQueueEnqueueDeque {

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

  private static BitAndResizableArrayQueue enqueue(int iterations) {
    BitAndResizableArrayQueue queue = new BitAndResizableArrayQueue();
    for (int i = 0; i < iterations; i++) {
      queue.enqueue(i);
    }
    return queue;
  }

  private static int deque(BitAndResizableArrayQueue queue, int iterations) {
    int sum = 0;
    for (int i = 0; i < iterations; i++) {
      sum += queue.deque();
    }
    return sum;
  }
}
