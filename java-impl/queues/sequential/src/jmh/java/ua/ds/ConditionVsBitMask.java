package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class ConditionVsBitMask extends QueueBenchmark {

  @Benchmark
  public int bit_mask() {
    return dequeManySum(enqueueMany(new BitMaskResizableArrayQueue()));
  }

  @Benchmark
  public int condition() {
    return dequeManySum(enqueueMany(new ConditionalResizableArrayQueue()));
  }

  @Benchmark
  public int bit_mask_while() {
    return dequeManySumWhile(enqueueMany(new BitMaskResizableArrayQueue()));
  }

  @Benchmark
  public int condition_while() {
    return dequeManySumWhile(enqueueMany(new ConditionalResizableArrayQueue()));
  }
}
