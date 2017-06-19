package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

//ua.ds.ArrayQueue object internals:
//  OFFSET  SIZE    TYPE DESCRIPTION                               VALUE
//       0    12         (object header)                           N/A
//      12     4     int ArrayQueue.head                           N/A
//      16     4     int ArrayQueue.tail                           N/A
//      20     4     int ArrayQueue.mask                           N/A
//      24     4   int[] ArrayQueue.items                          N/A
//      28     4         (loss due to the next object alignment)
//  Instance size: 32 bytes
//  Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
//[I object internals:
// OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
//      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
//      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
//      8     4        (object header)                           6d 01 00 f8 (01101101 00000001 00000000 11111000) (-134217363)
//     12     4        (object header)                           20 00 00 00 (00100000 00000000 00000000 00000000) (32)
//     16   128    int [I.<elements>                             N/A
//  Instance size: 144 bytes
//  Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
//
//    L1D 32KB  32768      4096      8192     16384
//    L2  256KB 262144    32768     65536    131072
//    L3  3MB   3145728  524288   1048576   2097152
public class ArrayQueueBenchmark {

  @State(Scope.Benchmark)
  public static class Enqueueing {

    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "524288", "1048576", "2097152"
    })
    private int capacity;

    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "524288", "1048576", "2097152"
    })
    private int iterations;

    @Benchmark
    public ArrayQueue enqueue() {
      ArrayQueue queue = new ArrayQueue(capacity);
      for (int i = 0; i < iterations - 1; i++) {
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
        "524288", "1048576", "2097152"
    })
    private int capacity;

    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "524288", "1048576", "2097152"
    })
    private int iterations;

    private ArrayQueue queue;

    @Setup
    public void createQueue() {
      queue = new ArrayQueue(capacity);
      for (int i = 0; i < iterations - 1; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    public int enqueueDeque() {
      int sum = 0;
      for (int i = 0; i < iterations; i++) {
        sum += queue.deque();
        queue.enqueue(i);
      }
      return sum;
    }
  }
}
