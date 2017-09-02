package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class LinkedVsArray extends QueueBenchmark {

  @Benchmark
  public int linked() {
    return dequeMany(enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public int arrayBase() {
    return dequeMany(enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }
}
