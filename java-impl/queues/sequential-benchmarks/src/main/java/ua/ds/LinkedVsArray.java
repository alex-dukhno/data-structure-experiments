package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@Fork(3)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
public class LinkedVsArray extends QueueBenchmark {

  private LinkedQueuePrimitive linked;
  private ConditionalNonResizableArrayQueuePrimitive array;

  @Setup
  public void setUp() {
    linked = new LinkedQueuePrimitive();
    array = new ConditionalNonResizableArrayQueuePrimitive(size);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public int linked() {
    return dequeMany(enqueueMany(linked));
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public int array() {
    return dequeMany(enqueueMany(array));
  }
}
