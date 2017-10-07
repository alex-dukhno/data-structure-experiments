package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;

@Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
public class LinkedArraysParallelGc extends QueueBenchmark {

  @Param({"16", "32", "64", "128", "256", "512"})
  private int segmentSize;

  private LinkedArrayQueuePrimitive primitive;
  private LinkedArrayQueueBoxed boxed;

  @Setup
  public void setUp() throws Exception {
    primitive = new LinkedArrayQueuePrimitive(segmentSize);
    boxed = new LinkedArrayQueueBoxed(segmentSize);
  }

  @Benchmark
  public int primitives() {
    return dequeMany(enqueueMany(primitive));
  }

  @Benchmark
  public int boxed() {
    return dequeMany(enqueueMany(boxed));
  }
}
