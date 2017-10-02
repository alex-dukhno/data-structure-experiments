package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Setup;

public class NullifyVsNotNullify extends QueueBenchmark {

  private LinkedQueuePrimitive notNullify;
  private LinkedQueueNullifyHeadPrimitive nullify;

  @Setup
  public void setUp() {
    notNullify = new LinkedQueuePrimitive();
    nullify = new LinkedQueueNullifyHeadPrimitive();
  }

  @Benchmark
  public int notNullify() {
    return dequeMany(enqueueMany(notNullify));
  }

  @Benchmark
  public int nullify() {
    return dequeMany(enqueueMany(nullify));
  }

  @Benchmark
  public int notNullify_per_op() {
    return dequeMany(enqueueMany(notNullify));
  }

  @Benchmark
  public int nullify_per_op() {
    return dequeMany(enqueueMany(nullify));
  }
}
