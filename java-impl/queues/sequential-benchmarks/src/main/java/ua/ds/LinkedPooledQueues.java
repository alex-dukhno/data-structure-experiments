package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import ua.ds.linked.primitive.LinkedQueuePrimitivePooled;
import ua.ds.linked.primitive.LinkedQueuePrimitivePooled.Pool;
import ua.ds.linked.primitive.LinkedQueuePrimitivePooled.PreInitializedPool;
import ua.ds.linked.primitive.LinkedQueuePrimitivePooled.SimplePool;
import ua.ds.linked.primitive.LinkedQueuePrimitivePooled.UnsafePool;

public class LinkedPooledQueues extends QueueBenchmark {

  @Param({"s", "p", "u"})
  private char poolType;

  private LinkedQueuePrimitivePooled linked;

  @Setup(Level.Invocation)
  public void setUp() {
    final Pool pool;
    switch (poolType) {
      case 'p':
        pool = new PreInitializedPool(size);
        break;
      case 'u':
        pool = new UnsafePool(size);
        break;
      default:
        pool = new SimplePool(size);
        break;
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

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseConcMarkSweepGC")
  public int pooled_cms() {
    return dequeMany(enqueueMany(linked));
  }
}
