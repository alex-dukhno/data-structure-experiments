package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class LinkedVsArray extends QueueBenchmark {

  @Benchmark
  public void linked(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public void arrayBase(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }
}
