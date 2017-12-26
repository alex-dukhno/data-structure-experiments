package ua.ds.experiments.n01;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Setup;

import ua.ds.QueueBenchmark;
import ua.ds.array.primitive.ConditionalNonResizableArrayQueuePrimitive;
import ua.ds.linked.primitive.LinkedQueuePrimitive;

public class LinkedVsArray extends QueueBenchmark {

  private LinkedQueuePrimitive linked;
  private ConditionalNonResizableArrayQueuePrimitive array;

  @Setup
  public void setUp() {
    linked = new LinkedQueuePrimitive();
    array = new ConditionalNonResizableArrayQueuePrimitive(size);
  }

  @Benchmark
  public int linked() {
    return dequeMany(enqueueMany(linked));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int linked_parallel() {
    return dequeMany(enqueueMany(linked));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseConcMarkSweepGC")
  public int linked_cms() {
    return dequeMany(enqueueMany(linked));
  }

  @Benchmark
  public int array() {
    return dequeMany(enqueueMany(array));
  }
}
