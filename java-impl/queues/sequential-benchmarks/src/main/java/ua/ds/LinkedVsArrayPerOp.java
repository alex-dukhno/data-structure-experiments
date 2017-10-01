package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(3)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
public abstract class LinkedVsArrayPerOp extends QueueMethods {
  @Setup(Level.Invocation)
  public void populateData() {
    int size = size();
    Random r = new Random();
    data = new int[size];
    for (int i = 0; i < data.length; i++) {
      data[i] = r.nextInt();
    }
  }

  abstract int size();

  public static class Small extends LinkedVsArrayPerOp {
    static final int SIZE = K;

    private LinkedQueuePrimitive linked;
    private ConditionalNonResizableArrayQueuePrimitive array;

    @Setup
    public void setUp() {
      linked = new LinkedQueuePrimitive();
      array = new ConditionalNonResizableArrayQueuePrimitive(SIZE);
    }

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int array() {
      return dequeMany(enqueueMany(array));
    }
  }

  public static class Medium extends LinkedVsArrayPerOp {
    static final int SIZE = 32 * K;

    private LinkedQueuePrimitive linked;
    private ConditionalNonResizableArrayQueuePrimitive array;

    @Setup
    public void setUp() {
      linked = new LinkedQueuePrimitive();
      array = new ConditionalNonResizableArrayQueuePrimitive(SIZE);
    }

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int array() {
      return dequeMany(enqueueMany(array));
    }
  }

  public static class Large extends LinkedVsArrayPerOp {
    static final int SIZE = M;

    private LinkedQueuePrimitive linked;
    private ConditionalNonResizableArrayQueuePrimitive array;

    @Setup
    public void setUp() {
      linked = new LinkedQueuePrimitive();
      array = new ConditionalNonResizableArrayQueuePrimitive(SIZE);
    }

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int array() {
      return dequeMany(enqueueMany(array));
    }
  }

  public static class ExtraLarge extends LinkedVsArrayPerOp {
    static final int SIZE = 32 * M;

    private LinkedQueuePrimitive linked;
    private ConditionalNonResizableArrayQueuePrimitive array;

    @Setup
    public void setUp() {
      linked = new LinkedQueuePrimitive();
      array = new ConditionalNonResizableArrayQueuePrimitive(SIZE);
    }

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @OperationsPerInvocation(SIZE)
    public int array() {
      return dequeMany(enqueueMany(array));
    }
  }
}
