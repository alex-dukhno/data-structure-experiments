package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.array.primitive.BitMaskResizableArrayQueuePrimitive;
import ua.ds.array.primitive.ConditionalResizableArrayQueuePrimitive;

public class ConditionVsBitMask extends QueueBenchmark {

  private BitMaskResizableArrayQueuePrimitive bitMaskResizable;
  private ConditionalResizableArrayQueuePrimitive conditionalResizable;

  @Setup
  public void setUp() throws Exception {
    bitMaskResizable = new BitMaskResizableArrayQueuePrimitive();
    conditionalResizable = new ConditionalResizableArrayQueuePrimitive();
  }

  @Benchmark
  public int bit_mask() {
    return dequeMany(enqueueMany(bitMaskResizable));
  }

  @Benchmark
  public int condition() {
    return dequeMany(enqueueMany(conditionalResizable));
  }
}
