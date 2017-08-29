package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class LinkedArrayQueueBenchmark {

  @Param({"512", "1024", "2048", "4096", "8192", "16384", "32768", "65536", "131072", "262144", "524288", "1048576"})
  public int iterations;
  @Param({"16", "32", "64", "128", "256", "512"})
  public int segmentSize;

  @Benchmark
  public LinkedArrayQueue enqueue() {
    LinkedArrayQueue queue = new LinkedArrayQueue(segmentSize);
    for (int i = 0; i < iterations; i++) {
      queue.enqueue(i);
    }
    return queue;
  }

  @Benchmark
  public int deque() {
    LinkedArrayQueue queue = enqueue();
    int sum = 0;
    for (int i = 0; i < iterations; i++) {
      sum += queue.deque();
    }
    return sum;
  }
}
