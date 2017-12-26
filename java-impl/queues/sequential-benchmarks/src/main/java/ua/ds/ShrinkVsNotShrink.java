package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.array.primitive.BitMaskResizableArrayQueuePrimitive;
import ua.ds.array.primitive.BitMaskResizableNotShrinkArrayQueuePrimitive;
import ua.ds.array.primitive.ConditionalResizableArrayQueuePrimitive;
import ua.ds.array.primitive.ConditionalResizableNotShrinkArrayQueuePrimitive;

public class ShrinkVsNotShrink extends QueueBenchmark {

  private BitMaskResizableArrayQueuePrimitive bitMaskShrink;
  private BitMaskResizableNotShrinkArrayQueuePrimitive bitMaskNotShrink;
  private ConditionalResizableArrayQueuePrimitive conditionalShrink;
  private ConditionalResizableNotShrinkArrayQueuePrimitive conditionalNotShrink;

  @Setup
  public void setUp() throws Exception {
    bitMaskShrink = new BitMaskResizableArrayQueuePrimitive();
    bitMaskNotShrink = new BitMaskResizableNotShrinkArrayQueuePrimitive();
    conditionalShrink = new ConditionalResizableArrayQueuePrimitive();
    conditionalNotShrink = new ConditionalResizableNotShrinkArrayQueuePrimitive();
  }

  @Benchmark
  public int shrink_mask() {
    return dequeMany(enqueueMany(bitMaskShrink));
  }

  @Benchmark
  public int shrink_condition() {
    return dequeMany(enqueueMany(conditionalShrink));
  }

  @Benchmark
  public int not_shrink_mask() {
    return dequeMany(enqueueMany(bitMaskNotShrink));
  }

  @Benchmark
  public int not_shrink_condition() {
    return dequeMany(enqueueMany(conditionalNotShrink));
  }
}
