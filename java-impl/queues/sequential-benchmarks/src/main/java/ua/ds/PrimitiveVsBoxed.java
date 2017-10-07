package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Setup;

public class PrimitiveVsBoxed extends QueueBenchmark {

  private BitMaskResizableNotShrinkArrayQueuePrimitive primitiveResize;
  private BitMaskResizableNotShrinkArrayQueueBoxed boxedResize;
  private BitMaskResizableNotShrinkArrayQueueBoxed boxedResizeParallel;
  private LinkedQueueBoxed linkedQueueBoxed;
  private LinkedQueueBoxed linkedQueueBoxedParallel;
  private LinkedQueuePrimitive linkedQueuePrimitive;
  private LinkedQueuePrimitive linkedQueuePrimitiveParallel;

  @Setup
  public void setUp() throws Exception {
    boxedResize = new BitMaskResizableNotShrinkArrayQueueBoxed();
    boxedResizeParallel = new BitMaskResizableNotShrinkArrayQueueBoxed();
    primitiveResize = new BitMaskResizableNotShrinkArrayQueuePrimitive();
    linkedQueueBoxed = new LinkedQueueBoxed();
    linkedQueueBoxedParallel = new LinkedQueueBoxed();
    linkedQueuePrimitive = new LinkedQueuePrimitive();
    linkedQueuePrimitiveParallel = new LinkedQueuePrimitive();
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
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int boxed_resize_parallel_gc() {
    return dequeMany(enqueueMany(boxedResizeParallel));
  }

  @Benchmark
  public int primitives_linked() {
    return dequeMany(enqueueMany(linkedQueuePrimitive));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int primitives_linked_parallel_gc() {
    return dequeMany(enqueueMany(linkedQueuePrimitiveParallel));
  }

  @Benchmark
  public int boxed_linked() {
    return dequeMany(enqueueMany(linkedQueueBoxed));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int boxed_linked_parallel_gc() {
    return dequeMany(enqueueMany(linkedQueueBoxedParallel));
  }
}
