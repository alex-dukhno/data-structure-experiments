package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class PredefinedSizeVsUnknown extends QueueBenchmark {
  @Benchmark
  public int predefined() {
    return dequeManySum(enqueueMany(new BitMaskResizableArrayQueue(size)));
  }

  @Benchmark
  public int unknown() {
    return dequeManySum(enqueueMany(new BitMaskResizableArrayQueue()));
  }

  @Benchmark
  public int predefined_while() {
    return dequeManySumWhile(enqueueMany(new BitMaskResizableArrayQueue(size)));
  }

  @Benchmark
  public int unknown_while() {
    return dequeManySumWhile(enqueueMany(new BitMaskResizableArrayQueue()));
  }
}
