package ua.ds.experiments.n01;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.QueueBenchmark;
import ua.ds.array.primitive.ConditionalNonResizableArrayQueuePrimitive;
import ua.ds.linked.primitive.LinkedQueuePrimitive;

public class LinkedVsArray extends QueueBenchmark {

  private LinkedQueuePrimitive linked;
  private LinkedQueuePrimitive linkedCms;
  private LinkedQueuePrimitive linkedParallel;
  private ConditionalNonResizableArrayQueuePrimitive array;

  @Setup
  public void setUp() {
    linked = new LinkedQueuePrimitive();
    linkedCms = new LinkedQueuePrimitive();
    linkedParallel = new LinkedQueuePrimitive();
    array = new ConditionalNonResizableArrayQueuePrimitive(size);
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseG1GC")
  public int linked_g1() {
    return dequeMany(enqueueMany(linked));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int linked_parallel() {
    return dequeMany(enqueueMany(linkedParallel));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseConcMarkSweepGC")
  public int linked_cms() {
    return dequeMany(enqueueMany(linkedCms));
  }

  @Benchmark
  public int array() {
    return dequeMany(enqueueMany(array));
  }
}
