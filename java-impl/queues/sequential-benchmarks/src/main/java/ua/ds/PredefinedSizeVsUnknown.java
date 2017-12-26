package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.array.primitive.BitMaskResizableArrayQueuePrimitive;
import ua.ds.array.primitive.ConditionalResizableArrayQueuePrimitive;

public class PredefinedSizeVsUnknown extends QueueBenchmark {

  private BitMaskResizableArrayQueuePrimitive bitMaskResizableArrayPrimitive;
  private ConditionalResizableArrayQueuePrimitive conditionalResizableQueuePrimitive;
  private BitMaskResizableArrayQueuePrimitive bitMaskResizableArrayPrimitivePredefSize;
  private ConditionalResizableArrayQueuePrimitive conditionalResizableQueuePrimitivePredefSize;

  @Setup
  public void setUp() throws Exception {
    bitMaskResizableArrayPrimitivePredefSize = new BitMaskResizableArrayQueuePrimitive(size);
    bitMaskResizableArrayPrimitive = new BitMaskResizableArrayQueuePrimitive();
    conditionalResizableQueuePrimitivePredefSize = new ConditionalResizableArrayQueuePrimitive(size);
    conditionalResizableQueuePrimitive = new ConditionalResizableArrayQueuePrimitive();
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
