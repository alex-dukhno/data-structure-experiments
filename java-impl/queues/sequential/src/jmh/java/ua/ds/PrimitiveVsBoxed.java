package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class PrimitiveVsBoxed extends QueueBenchmark {

  @Benchmark
  public void primitives(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public void boxed(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableArrayQueueBoxed(size)));
  }
}
