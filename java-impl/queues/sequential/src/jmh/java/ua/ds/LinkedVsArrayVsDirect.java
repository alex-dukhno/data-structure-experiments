package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class LinkedVsArrayVsDirect extends QueueBenchmark {

  @Benchmark
  public int linked() {
    return dequeMany(enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public int arrayBase() {
    return dequeMany(enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int directMemory() {
    return dequeMany(enqueueMany(new NonResizableDirectBufferQueue(size)));
  }
}
