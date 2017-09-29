package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public class PrimitiveVsBoxed extends QueueBenchmark {

  private BitMaskResizableArrayQueuePrimitive primitiveResize;
  private BitMaskNonResizableArrayQueuePrimitive primitiveNonResize;
  private BitMaskResizableArrayQueueBoxed boxedResize;
  private BitMaskNonResizableArrayQueueBoxed boxedNonResize;
  private LinkedQueueBoxed linkedQueueBoxed;
  private LinkedQueuePrimitive linkedQueuePrimitive;

  @Setup
  public void setUp() throws Exception {
    boxedNonResize = new BitMaskNonResizableArrayQueueBoxed(size);
    boxedResize = new BitMaskResizableArrayQueueBoxed();
    primitiveNonResize = new BitMaskNonResizableArrayQueuePrimitive(size);
    primitiveResize = new BitMaskResizableArrayQueuePrimitive();
    linkedQueueBoxed = new LinkedQueueBoxed();
    linkedQueuePrimitive = new LinkedQueuePrimitive();
  }

  @Benchmark
  public int primitives_non_resize() {
    return dequeMany(enqueueMany(primitiveNonResize));
  }

  @Benchmark
  public int primitives_resize() {
    return dequeMany(enqueueMany(primitiveResize));
  }

  @Benchmark
  public int boxed_non_resize() {
    return dequeMany(enqueueMany(boxedNonResize));
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
