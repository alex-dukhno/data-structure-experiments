package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public class SizePlusOne extends QueueBenchmark {
  private BitMaskResizableNotShrinkArrayQueueBoxed boxed;
  private BitMaskResizableNotShrinkArrayQueuePrimitive primitive;

  @Setup
  public void setUp() {
    boxed = new BitMaskResizableNotShrinkArrayQueueBoxed();
    primitive = new BitMaskResizableNotShrinkArrayQueuePrimitive();
  }

  @Benchmark
  public int primitive() {
    enqueueMany(primitive);
    enqueueOne(primitive);
    return dequeMany(primitive);
  }

  @Benchmark
  public int boxed() {
    enqueueMany(boxed);
    enqueueOne(boxed);
    return dequeMany(boxed);
  }
}
