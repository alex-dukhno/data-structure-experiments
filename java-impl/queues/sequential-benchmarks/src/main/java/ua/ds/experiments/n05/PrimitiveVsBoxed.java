package ua.ds.experiments.n05;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.QueueBenchmark;
import ua.ds.array.boxed.BitMaskResizableNotShrinkArrayQueueBoxed;
import ua.ds.array.primitive.BitMaskResizableNotShrinkArrayQueuePrimitive;
import ua.ds.linked.boxed.LinkedQueueBoxed;
import ua.ds.linked.primitive.LinkedQueuePrimitive;

public class PrimitiveVsBoxed extends QueueBenchmark {

  private BitMaskResizableNotShrinkArrayQueuePrimitive primitiveResize;
  private BitMaskResizableNotShrinkArrayQueueBoxed boxedResize;
  private BitMaskResizableNotShrinkArrayQueueBoxed boxedResizeParallel;
  private BitMaskResizableNotShrinkArrayQueueBoxed boxedResizeCms;
  private LinkedQueueBoxed linkedQueueBoxed;
  private LinkedQueueBoxed linkedQueueBoxedParallel;
  private LinkedQueueBoxed linkedQueueBoxedCms;
  private LinkedQueuePrimitive linkedQueuePrimitive;
  private LinkedQueuePrimitive linkedQueuePrimitiveParallel;
  private LinkedQueuePrimitive linkedQueuePrimitiveCms;

  @Setup
  public void setUp() throws Exception {
    boxedResize = new BitMaskResizableNotShrinkArrayQueueBoxed();
    boxedResizeParallel = new BitMaskResizableNotShrinkArrayQueueBoxed();
    boxedResizeCms = new BitMaskResizableNotShrinkArrayQueueBoxed();
    primitiveResize = new BitMaskResizableNotShrinkArrayQueuePrimitive();
    linkedQueueBoxed = new LinkedQueueBoxed();
    linkedQueueBoxedParallel = new LinkedQueueBoxed();
    linkedQueueBoxedCms = new LinkedQueueBoxed();
    linkedQueuePrimitive = new LinkedQueuePrimitive();
    linkedQueuePrimitiveParallel = new LinkedQueuePrimitive();
    linkedQueuePrimitiveCms = new LinkedQueuePrimitive();
  }

  @Benchmark
  public int primitives_resize() {
    return dequeMany(enqueueMany(primitiveResize));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseG1GC")
  public int boxed_resize_g1() {
    return dequeMany(enqueueMany(boxedResize));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int boxed_resize_parallel() {
    return dequeMany(enqueueMany(boxedResizeParallel));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseConcMarkSweepGC")
  public int boxed_resize_cms() {
    return dequeMany(enqueueMany(boxedResizeCms));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseG1GC")
  public int primitives_linked_g1() {
    return dequeMany(enqueueMany(linkedQueuePrimitive));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int primitives_linked_parallel() {
    return dequeMany(enqueueMany(linkedQueuePrimitiveParallel));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseConcMarkSweepGC")
  public int primitives_linked_cms() {
    return dequeMany(enqueueMany(linkedQueuePrimitiveCms));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseG1GC")
  public int boxed_linked_g1() {
    return dequeMany(enqueueMany(linkedQueueBoxed));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int boxed_linked_parallel() {
    return dequeMany(enqueueMany(linkedQueueBoxedParallel));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseConcMarkSweepGC")
  public int boxed_linked_cms() {
    return dequeMany(enqueueMany(linkedQueueBoxedCms));
  }
}
