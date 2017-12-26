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

import ua.ds.array.boxed.BitMaskResizableNotShrinkArrayQueueBoxed;
import ua.ds.array.primitive.BitMaskResizableNotShrinkArrayQueuePrimitive;
import ua.ds.linked.boxed.LinkedQueueBoxed;

@Fork(3)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public abstract class PrimitiveVsBoxedPerOp extends QueueMethods {
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

  public static abstract class GeneralSetup extends PrimitiveVsBoxedPerOp {
    BitMaskResizableNotShrinkArrayQueuePrimitive primitiveArray;
    BitMaskResizableNotShrinkArrayQueueBoxed boxedArray;
    LinkedQueueBoxed boxedLinked;
    LinkedQueueBoxed boxedLinkedParallel;
    LinkedQueueBoxed boxedLinkedCms;

    @Setup
    public void createQueues() {
      boxedArray = new BitMaskResizableNotShrinkArrayQueueBoxed();
      primitiveArray = new BitMaskResizableNotShrinkArrayQueuePrimitive();
      boxedLinked = new LinkedQueueBoxed();
      boxedLinkedParallel = new LinkedQueueBoxed();
      boxedLinkedCms = new LinkedQueueBoxed();
    }
  }

  public static class Small extends GeneralSetup {
    static final int SIZE = K;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }

  public static class Medium extends GeneralSetup {
    static final int SIZE = 32 * K;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }

  public static class Large extends GeneralSetup {
    static final int SIZE = M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }

  public static class Large2 extends GeneralSetup {
    static final int SIZE = 2 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }

  public static class Large4 extends GeneralSetup {
    static final int SIZE = 4 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }

  public static class Large8 extends GeneralSetup {
    static final int SIZE = 8 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }

  public static class Large16 extends GeneralSetup {
    static final int SIZE = 16 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }

  public static class Large32 extends GeneralSetup {
    static final int SIZE = 32 * M;

    @Override
    int size() {
      return SIZE;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int primitives_array() {
      return dequeMany(enqueueMany(primitiveArray));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_array() {
      return dequeMany(enqueueMany(boxedArray));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_parallel() {
      return dequeMany(enqueueMany(boxedLinkedParallel));
    }

    @Benchmark
    @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
    @OperationsPerInvocation(SIZE)
    public int linked_cms() {
      return dequeMany(enqueueMany(boxedLinkedCms));
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int boxed_linked() {
      return dequeMany(enqueueMany(boxedLinked));
    }
  }
}
