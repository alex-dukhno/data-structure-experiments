package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public class ConditionVsBitMask extends QueueBenchmark {

  private BitMaskResizableArrayQueuePrimitive bitMaskResizable;
  private BitMaskResizableArrayQueuePrimitive bitMaskResizablePredefSize;
  private ConditionalResizableArrayQueuePrimitive conditionalResizable;
  private ConditionalResizableArrayQueuePrimitive conditionalResizablePredefSize;

  @Setup
  public void setUp() throws Exception {
    bitMaskResizable = new BitMaskResizableArrayQueuePrimitive();
    bitMaskResizablePredefSize = new BitMaskResizableArrayQueuePrimitive(size);
    conditionalResizable = new ConditionalResizableArrayQueuePrimitive();
    conditionalResizablePredefSize = new ConditionalResizableArrayQueuePrimitive(size);
  }

  @Benchmark
  public int bit_mask() {
    return dequeMany(enqueueMany(bitMaskResizable));
  }

  @Benchmark
  public int bit_mask_predef_size() {
    return dequeMany(enqueueMany(bitMaskResizablePredefSize));
  }

  @Benchmark
  public int condition() {
    return dequeMany(enqueueMany(conditionalResizable));
  }

  @Benchmark
  public int condition_predef_size() {
    return dequeMany(enqueueMany(conditionalResizablePredefSize));
  }
}
