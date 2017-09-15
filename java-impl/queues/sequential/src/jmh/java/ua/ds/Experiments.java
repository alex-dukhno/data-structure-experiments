package ua.ds;

import java.util.ArrayDeque;
import java.util.LinkedList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class Experiments extends QueueBenchmark {

  @Benchmark
  public void linked(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public int linked_sum() {
    return dequeManySum(enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public int linked_sum_while() {
    return dequeManySumWhile(enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public void linked_jdk(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new LinkedList<>()));
  }

  @Benchmark
  public void arrayBase_cond(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int arrayBase_cond_sum() {
    return dequeManySum(enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int arrayBase_cond_sum_while() {
    return dequeManySumWhile(enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public void arrayBase_mask(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableBitAndArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int arrayBase_mask_sum() {
    return dequeManySum(enqueueMany(new NonResizableBitAndArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int arrayBase_mask_sum_while() {
    return dequeManySumWhile(enqueueMany(new NonResizableBitAndArrayQueuePrimitive(size)));
  }

  @Benchmark
  public void arrayBase_jdk(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new ArrayDeque<>(size)));
  }

  @Benchmark
  public void resizable_mask(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new BitAndResizableArrayQueue(size)));
  }

  @Benchmark
  public void resizable_cond(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new BranchResizableArrayQueue(size)));
  }

  @Benchmark
  public void directMemory(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableDirectBufferQueue(size)));
  }

  @Benchmark
  public void heapMemory(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableHeapBufferQueue(size)));
  }

  @Benchmark
  public int mask_sum() {
    return dequeManySum(enqueueMany(new BitAndResizableArrayQueue(size)));
  }

  @Benchmark
  public int condition_sum() {
    return dequeManySum(enqueueMany(new BranchResizableArrayQueue(size)));
  }

  @Benchmark
  public int primitives_sum() {
    return dequeManySum(enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int boxed_sum() {
    return dequeManySum(enqueueMany(new NonResizableArrayQueueBoxed(size)));
  }
}
