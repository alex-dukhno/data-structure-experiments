package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;

public class LinkedArrays extends QueueBenchmark {

  @Param({"16", "32", "64", "128", "256", "512"})
  private int segmentSize;

  @Benchmark
  public void linkedArrays(Blackhole blackhole) {
     dequeMany(blackhole, enqueueMany(new LinkedArrayQueue(segmentSize)));
  }
}
