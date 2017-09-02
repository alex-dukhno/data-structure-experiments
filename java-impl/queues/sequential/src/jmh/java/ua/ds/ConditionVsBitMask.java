package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class ConditionVsBitMask extends QueueBenchmark {

  @Benchmark
  public int mask() {
    return dequeMany(enqueueMany(new BitAndResizableArrayQueue()));
  }

  @Benchmark
  public int condition() {
    return dequeMany(enqueueMany(new BranchResizableArrayQueue()));
  }
}
