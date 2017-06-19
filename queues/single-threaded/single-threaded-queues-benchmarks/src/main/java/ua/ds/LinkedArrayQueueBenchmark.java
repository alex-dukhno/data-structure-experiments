package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class LinkedArrayQueueBenchmark {
  @State(Scope.Benchmark)
  public static class Enqueueing {
    @Param({
        "16", "32", "64", "128",
        "256", "512", "1024"
    })
    private int segmentCapacity;
    @Param({
        //L1
        "256", "128", "64", "32", "8", "4",
        "292", "186", "108", "58", "15", "7",
        "512", /*"256", "128",*/"64", "32", "16",

        //L2
        "2048", "1024", /*"512", "256", "128", "64", "32",*/
        "2340", "1489", "862", "468", "244", "125", "63",
        "4096", /*"2048", "1024", "512", "256", "128", "64",*/

        //L3
        "16384", /*"16384",*/ "8192", "4096", "2048", "1024", "512",
        "28086", "17873", "10347", "5617", "2932", "1500", "759",
        "32768", /*"32768",*/ "16384", "8192", "4096", "2048", "1024"
    })
    private int numberOfSegments;

    @Benchmark
    public int enqueue() {
      LinkedArrayQueue queue = new LinkedArrayQueue();
      int i;
      for (i = 0; i < numberOfSegments * segmentCapacity; i++) {
        queue.enqueue(i);
      }
      return i + 1;
    }
  }

  @State(Scope.Benchmark)
  public static class EnqueueingDequeing {
    @Param({
        "16", "32", "64", "128",
        "256", "512", "1024"
    })
    private int segmentCapacity;
    @Param({
        //L1
        "256", "128", "64", "32", "8", "4",
        "292", "186", "108", "58", "15", "7",
        "512", /*"256", "128",*/"64", "32", "16",

        //L2
        "2048", "1024", /*"512", "256", "128", "64", "32",*/
        "2340", "1489", "862", "468", "244", "125", "63",
        "4096", /*"2048", "1024", "512", "256", "128", "64",*/

        //L3
        "16384", /*"16384",*/ "8192", "4096", "2048", "1024", "512",
        "28086", "17873", "10347", "5617", "2932", "1500", "759",
        "32768", /*"32768",*/ "16384", "8192", "4096", "2048", "1024"
    })
    private int numberOfSegments;

    private LinkedArrayQueue queue;

    @Setup
    public void createQueue() {
      queue = new LinkedArrayQueue();
      for (int i = 0; i < numberOfSegments * segmentCapacity; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    public int enqueueDeque() {
      int sum = 0;
      for (int i = 0; i < numberOfSegments * segmentCapacity; i++) {
        sum += queue.deque();
        queue.enqueue(i);
      }
      return sum;
    }
  }
}
