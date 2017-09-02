package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;

public class LinkedArrays extends QueueBenchmark {

  @Param({"16", "32", "64", "128", "256", "512"})
  private int segmentSize;

  @Benchmark
  public int linkedArrays() {
    return dequeMany(enqueueMany(new LinkedArrayQueue(segmentSize)));
  }
}
