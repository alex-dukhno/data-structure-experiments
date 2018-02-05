package ua.ds.experiments.n06;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import ua.ds.QueueBenchmark;
import ua.ds.array.primitive.UnsafeArrayQueuePrimitive;

public class UnsafeArrayQueues extends QueueBenchmark {
  private UnsafeArrayQueuePrimitive gFirstGc;

  @Setup
  public void setUp() throws Exception {
    gFirstGc = new UnsafeArrayQueuePrimitive();
  }

  @TearDown
  public void tearDown() throws Exception {
    gFirstGc.deallocateItems();
  }

  @Benchmark
  public int not_shrink_mask() {
    return dequeMany(enqueueMany(gFirstGc));
  }
}
