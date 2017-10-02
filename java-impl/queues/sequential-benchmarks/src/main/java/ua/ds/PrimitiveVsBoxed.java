package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public class PrimitiveVsBoxed extends QueueBenchmark {

  private BitMaskResizableNotShrinkArrayQueuePrimitive primitiveResize;
  private BitMaskResizableNotShrinkArrayQueueBoxed boxedResize;
  private LinkedQueueBoxed linkedQueueBoxed;
  private LinkedQueuePrimitive linkedQueuePrimitive;

  @Setup
  public void setUp() throws Exception {
    boxedResize = new BitMaskResizableNotShrinkArrayQueueBoxed();
    primitiveResize = new BitMaskResizableNotShrinkArrayQueuePrimitive();
    linkedQueueBoxed = new LinkedQueueBoxed();
    linkedQueuePrimitive = new LinkedQueuePrimitive();
  }

  @Benchmark
  public int primitives_resize() {
    return dequeMany(enqueueMany(primitiveResize));
  }

  @Benchmark
  public int boxed_resize() {
    return dequeMany(enqueueMany(boxedResize));
  }

  @Benchmark
  public int primitives_linked() {
    return dequeMany(enqueueMany(linkedQueuePrimitive));
  }

  @Benchmark
  public int boxed_linked() {
    return dequeMany(enqueueMany(linkedQueueBoxed));
  }
}
