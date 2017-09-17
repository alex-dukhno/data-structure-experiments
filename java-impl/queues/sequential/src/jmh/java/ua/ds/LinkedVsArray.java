package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class LinkedVsArray extends QueueBenchmark {

  @Benchmark
  public int linked() {
    return dequeManySum(enqueueMany(new LinkedQueuePrimitive()));
  }

  @Benchmark
  public int array() {
    return dequeManySum(enqueueMany(new ConditionalNonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int linked_while() {
    return dequeManySumWhile(enqueueMany(new LinkedQueuePrimitive()));
  }

  @Benchmark
  public int array_while() {
    return dequeManySumWhile(enqueueMany(new ConditionalNonResizableArrayQueuePrimitive(size)));
  }
}
