package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class ConditionVsBitMask extends QueueBenchmark {

  @Benchmark
  public void mask(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new BitMaskResizableArrayQueue()));
  }

  @Benchmark
  public void condition(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new ConditionalResizableArrayQueue()));
  }
}
