package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import ua.ds.LinkedQueuePrimitivePooled.Pool;
import ua.ds.LinkedQueuePrimitivePooled.PreInitializedPool;
import ua.ds.LinkedQueuePrimitivePooled.SimplePool;

@Fork(3)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public abstract class LinkedPooledQueuesPerOp extends QueueMethods {
  @Param({"true", "false"})
  private boolean preInit;
  
  LinkedQueuePrimitivePooled linked;

  @Setup(Level.Invocation)
  public void populateData() {
    int size = size();
    Random r = new Random();
    data = new int[size];
    for (int i = 0; i < data.length; i++) {
      data[i] = r.nextInt();
    }
    final Pool pool;
    if (preInit) {
      pool = new PreInitializedPool(size());
    } else {
      pool = new SimplePool(size());
    }
    linked = new LinkedQueuePrimitivePooled(pool);
  }

  @TearDown
  public void tearDown() {
    linked = null;
    data = null;
  }

  abstract int size();

  public static class Small extends LinkedPooledQueuesPerOp {
    static final int SIZE = K;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }

  public static class Medium extends LinkedPooledQueuesPerOp {
    static final int SIZE = 32 * K;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }

  public static class Large extends LinkedPooledQueuesPerOp {
    static final int SIZE = M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }

  public static class Large2 extends LinkedPooledQueuesPerOp {
    static final int SIZE = 2 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }

  public static class Large4 extends LinkedPooledQueuesPerOp {
    static final int SIZE = 4 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }

  public static class Large8 extends LinkedPooledQueuesPerOp {
    static final int SIZE = 8 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }

  public static class Large16 extends LinkedPooledQueuesPerOp {
    static final int SIZE = 16 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }

  public static class Large32 extends LinkedPooledQueuesPerOp {
    static final int SIZE = 32 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int linked() {
      return dequeMany(enqueueMany(linked));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(linked));
    }
  }
}
