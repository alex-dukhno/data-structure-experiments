package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class PredefinedSizeVsUnknown extends QueueBenchmark {
  @Benchmark
  public void predefined(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new BitMaskResizableArrayQueue(size)));
  }

  @Benchmark
  public void unknown(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new BitMaskResizableArrayQueue()));
  }
}
