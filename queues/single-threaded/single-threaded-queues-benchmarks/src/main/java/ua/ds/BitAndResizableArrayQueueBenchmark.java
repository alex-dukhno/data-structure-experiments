package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
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
//    L1D 32KB  32768
//    L2  256KB 262144
//    L3  3MB   3145728
//
public class BitAndResizableArrayQueueBenchmark {
  @State(Scope.Benchmark)
  public static class EnqueueingNoPreallocate {
    @Param({
        "4096", "8192", "16384",
        "32768", "65536", "131072",
        "524288", "1048576", "2097152"
    })
    private int initialSize;

    @Benchmark
    public BitAndResizableArrayQueue enqueue() {
      BitAndResizableArrayQueue queue = new BitAndResizableArrayQueue();
      for (int i = 0; i < initialSize - 1; i++) {
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
    public BitAndResizableArrayQueue enqueue() {
      BitAndResizableArrayQueue queue = new BitAndResizableArrayQueue(preallocateSize);
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

    private BitAndResizableArrayQueue queue;

    @Setup
    public void createQueue() {
      queue = new BitAndResizableArrayQueue(preallocateSize);
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
