package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.array.primitive.BitMaskNonResizableArrayQueuePrimitive;
import ua.ds.array.primitive.BitMaskResizableArrayQueuePrimitive;
import ua.ds.array.primitive.ConditionalNonResizableArrayQueuePrimitive;
import ua.ds.array.primitive.ConditionalResizableArrayQueuePrimitive;
import ua.ds.linked.primitive.LinkedQueuePrimitive;

public class Experiments extends QueueBenchmark {

  private LinkedQueuePrimitive linkedPrimitive;
  private ConditionalNonResizableArrayQueuePrimitive conditionalNonResizablePrimitive;
  private BitMaskNonResizableArrayQueuePrimitive bitMaskNonResizableArrayPrimitive;
  private BitMaskResizableArrayQueuePrimitive bitMaskResizableArrayPrimitive;
  private ConditionalResizableArrayQueuePrimitive conditionalResizableQueuePrimitive;
  private BitMaskResizableArrayQueuePrimitive bitMaskResizableArrayPrimitivePredefSize;
  private ConditionalResizableArrayQueuePrimitive conditionalResizableQueuePrimitivePredefSize;

  @Setup
  public void setUp() throws Exception {
    linkedPrimitive = new LinkedQueuePrimitive();
    conditionalNonResizablePrimitive = new ConditionalNonResizableArrayQueuePrimitive(size);
    bitMaskNonResizableArrayPrimitive = new BitMaskNonResizableArrayQueuePrimitive(size);
    bitMaskResizableArrayPrimitivePredefSize = new BitMaskResizableArrayQueuePrimitive(size);
    bitMaskResizableArrayPrimitive = new BitMaskResizableArrayQueuePrimitive();
    conditionalResizableQueuePrimitivePredefSize = new ConditionalResizableArrayQueuePrimitive(size);
    conditionalResizableQueuePrimitive = new ConditionalResizableArrayQueuePrimitive();
  }

  @Benchmark
  public int linked() {
    return dequeMany(enqueueMany(linkedPrimitive));
  }

  @Benchmark
  public int array_non_resize_cond() {
    return dequeMany(enqueueMany(conditionalNonResizablePrimitive));
  }

  @Benchmark
  public int array_non_resize_mask() {
    return dequeMany(enqueueMany(bitMaskNonResizableArrayPrimitive));
  }

  @Benchmark
  public int array_resize_mask() {
    return dequeMany(enqueueMany(bitMaskResizableArrayPrimitive));
  }

  @Benchmark
  public int array_resize_condition() {
    return dequeMany(enqueueMany(conditionalResizableQueuePrimitive));
  }

  @Benchmark
  public int array_resize_mask_predef_size() {
    return dequeMany(enqueueMany(bitMaskResizableArrayPrimitivePredefSize));
  }

  @Benchmark
  public int array_resize_cond_predef_size() {
    return dequeMany(enqueueMany(conditionalResizableQueuePrimitivePredefSize));
  }
}
