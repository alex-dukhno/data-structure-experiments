package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Setup;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

import ua.ds.array.boxed.BitMaskResizableNotShrinkArrayQueueBoxed;
import ua.ds.array.boxed.ConditionalResizableArrayQueueBoxed;
import ua.ds.array.boxed.ConditionalResizableNotShrinkArrayQueueBoxed;

public class StdQueue extends QueueBenchmark {

  private Queue<Integer> arrayDeque;
  private Queue<Integer> linkedList;
  private Queue<Integer> linkedListParallel;
  private ConditionalResizableArrayQueueBoxed conditional;
  private ConditionalResizableArrayQueueBoxed conditionalParallel;
  private ConditionalResizableNotShrinkArrayQueueBoxed conditionalNotShrink;
  private ConditionalResizableNotShrinkArrayQueueBoxed conditionalNotShrinkParallel;
  private BitMaskResizableNotShrinkArrayQueueBoxed bitMaskNotShrink;
  private BitMaskResizableNotShrinkArrayQueueBoxed bitMaskNotShrinkParallel;

  @Setup
  public void setUp() {
    arrayDeque = new ArrayDeque<>();
    linkedList = new LinkedList<>();
    linkedListParallel = new LinkedList<>();
    conditional = new ConditionalResizableArrayQueueBoxed();
    conditionalParallel = new ConditionalResizableArrayQueueBoxed();
    conditionalNotShrink = new ConditionalResizableNotShrinkArrayQueueBoxed();
    conditionalNotShrinkParallel = new ConditionalResizableNotShrinkArrayQueueBoxed();
    bitMaskNotShrink = new BitMaskResizableNotShrinkArrayQueueBoxed();
    bitMaskNotShrinkParallel = new BitMaskResizableNotShrinkArrayQueueBoxed();
  }

  @Benchmark
  public int array_std() {
    return dequeMany(enqueueMany(arrayDeque));
  }

  @Benchmark
  public int linked_std() {
    return dequeMany(enqueueMany(linkedList));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int linked_std_parallel() {
    return dequeMany(enqueueMany(linkedListParallel));
  }

  @Benchmark
  public int conditional() {
    return dequeMany(enqueueMany(conditional));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int conditional_parallel_gc() {
    return dequeMany(enqueueMany(conditionalParallel));
  }

  @Benchmark
  public int conditionalNotShrink() {
    return dequeMany(enqueueMany(conditionalNotShrink));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int conditionalNotShrink_parallel_gc() {
    return dequeMany(enqueueMany(conditionalNotShrinkParallel));
  }

  @Benchmark
  public int bitMaskNotShrink() {
    return dequeMany(enqueueMany(bitMaskNotShrink));
  }

  @Benchmark
  @Fork(value = 3, jvmArgs = "-XX:+UseParallelGC")
  public int bitMaskNotShrink_parallel_gc() {
    return dequeMany(enqueueMany(bitMaskNotShrinkParallel));
  }
}
