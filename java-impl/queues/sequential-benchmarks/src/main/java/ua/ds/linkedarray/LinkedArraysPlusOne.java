package ua.ds.linkedarray;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.LinkedArrayQueueBoxed;
import ua.ds.LinkedArrayQueuePrimitive;
import ua.ds.QueueBenchmark;

public class LinkedArraysPlusOne extends QueueBenchmark {

  @Param({"16", "32", "64", "128", "256", "512"})
  private int segmentSize;

  private LinkedArrayQueuePrimitive primitive;
  private LinkedArrayQueueBoxed boxed;
  private LinkedArrayQueueBoxed boxedParallel;

  @Setup
  public void setUp() throws Exception {
    primitive = new LinkedArrayQueuePrimitive(segmentSize);
    boxed = new LinkedArrayQueueBoxed(segmentSize);
    boxedParallel = new LinkedArrayQueueBoxed(segmentSize);
  }

  @Benchmark
  public int primitives() {
    enqueueMany(primitive);
    enqueueOne(primitive);
    return dequeMany(primitive);
  }

  @Benchmark
  public int boxed() {
    enqueueMany(boxed);
    enqueueOne(boxed);
    return dequeMany(boxed);
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int boxed_parallel() {
    enqueueMany(boxedParallel);
    enqueueOne(boxedParallel);
    return dequeMany(boxedParallel);
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int boxed_cms() {
    enqueueMany(boxedParallel);
    enqueueOne(boxedParallel);
    return dequeMany(boxedParallel);
  }
}
