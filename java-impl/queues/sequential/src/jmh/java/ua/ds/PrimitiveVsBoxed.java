package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

public class PrimitiveVsBoxed extends QueueBenchmark {

  @Benchmark
  public int primitives_array() {
    return dequeManySum(enqueueMany(new BitMaskNonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int boxed_array() {
    return dequeManySum(enqueueMany(new BitMaskNonResizableArrayQueueBoxed(size)));
  }

  @Benchmark
  public int primitives_array_while() {
    return dequeManySumWhile(enqueueMany(new BitMaskNonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int boxed_array_while() {
    return dequeManySumWhile(enqueueMany(new BitMaskNonResizableArrayQueueBoxed(size)));
  }

  @Benchmark
  public int primitives_linked() {
    return dequeManySum(enqueueMany(new LinkedQueuePrimitive()));
  }

  @Benchmark
  public int boxed_linked() {
    return dequeManySum(enqueueMany(new LinkedQueueBoxed()));
  }

  @Benchmark
  public int primitives_linked_while() {
    return dequeManySumWhile(enqueueMany(new LinkedQueuePrimitive()));
  }

  @Benchmark
  public int boxed_linked_while() {
    return dequeManySumWhile(enqueueMany(new LinkedQueueBoxed()));
  }
}
