package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Warmup;

@Fork(3)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
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
