package ua.ds.experiments.n03;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.QueueBenchmark;
import ua.ds.array.primitive.BitMaskResizableArrayQueuePrimitive;
import ua.ds.array.primitive.BitMaskResizableNotShrinkArrayQueuePrimitive;
import ua.ds.array.primitive.BitMaskResizableNotShrinkArrayQueuePrimitiveWithIncrement;
import ua.ds.array.primitive.ConditionalResizableArrayQueuePrimitive;
import ua.ds.array.primitive.ConditionalResizableNotShrinkArrayQueuePrimitive;

public class ShrinkVsNotShrink extends QueueBenchmark {

  private BitMaskResizableArrayQueuePrimitive bitMaskShrink;
  private BitMaskResizableNotShrinkArrayQueuePrimitive bitMaskNotShrink;
  private BitMaskResizableNotShrinkArrayQueuePrimitiveWithIncrement bitMaskNotShrinkWithIncrement;
  private ConditionalResizableArrayQueuePrimitive conditionalShrink;
  private ConditionalResizableNotShrinkArrayQueuePrimitive conditionalNotShrink;

  @Setup
  public void setUp() throws Exception {
    bitMaskShrink = new BitMaskResizableArrayQueuePrimitive();
    bitMaskNotShrink = new BitMaskResizableNotShrinkArrayQueuePrimitive();
    bitMaskNotShrinkWithIncrement = new BitMaskResizableNotShrinkArrayQueuePrimitiveWithIncrement();
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
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int not_shrink_mask() {
    for (int item : data) {
      bitMaskNotShrink.enqueue(item);
    }
    int sum = 0;
    int item;
    while ((item = bitMaskNotShrink.deque()) != -1) {
      sum += item;
    }
    return sum;
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int not_shrink_mask_with_increment() {
    for (int item : data) {
      bitMaskNotShrinkWithIncrement.enqueue(item);
    }
    int sum = 0;
    int item;
    while ((item = bitMaskNotShrinkWithIncrement.deque()) != -1) {
      sum += item;
    }
    return sum;
  }

  @Benchmark
  public int not_shrink_condition() {
    return dequeMany(enqueueMany(conditionalNotShrink));
  }
}
