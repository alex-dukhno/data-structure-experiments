package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class PrimitiveVsBoxed extends QueueBenchmark {

  @Benchmark
  public int primitives() {
    return dequeMany(enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int boxed() {
    return dequeMany(enqueueMany(new NonResizableArrayQueueBoxed(size)));
  }
}
