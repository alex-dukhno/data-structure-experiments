package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import ua.ds.LinkedQueuePrimitivePooled.Pool;
import ua.ds.LinkedQueuePrimitivePooled.PreInitializedPool;
import ua.ds.LinkedQueuePrimitivePooled.SimplePool;

public class LinkedPooledQueues extends QueueBenchmark {

  @Param({"true", "false"})
  private boolean preInit;

  private LinkedQueuePrimitivePooled linked;

  @Setup(Level.Invocation)
  public void setUp() {
    final Pool pool;
    if (preInit) {
      pool = new PreInitializedPool(size);
    } else {
      pool = new SimplePool(size);
    }
    linked = new LinkedQueuePrimitivePooled(pool);
  }

  @TearDown(Level.Invocation)
  public void tearDown() {
    linked = null;
  }

  @Benchmark
  public int pooled() {
    return dequeMany(enqueueMany(linked));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int pooled_parallel() {
    return dequeMany(enqueueMany(linked));
  }
}
