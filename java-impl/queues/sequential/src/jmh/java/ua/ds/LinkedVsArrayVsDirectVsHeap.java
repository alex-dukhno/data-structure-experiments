package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.ArrayDeque;
import java.util.LinkedList;

public class LinkedVsArrayVsDirectVsHeap extends QueueBenchmark {

  @Benchmark
  public int linked() {
    return dequeMany(enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public int linked_jdk() {
    return dequeMany(enqueueMany(new LinkedList<>()));
  }

  @Benchmark
  public int arrayBase() {
    return dequeMany(enqueueMany(new NonResizableArrayQueuePrimitive(size)));
  }

  @Benchmark
  public int arrayBase_jdk() {
    return dequeMany(enqueueMany(new ArrayDeque<>(size)));
  }

  @Benchmark
  public int resizable_mask() {
    return dequeMany(enqueueMany(new BitAndResizableArrayQueue(size)));
  }

  @Benchmark
  public int resizable_cond() {
    return dequeMany(enqueueMany(new BranchResizableArrayQueue(size)));
  }

  @Benchmark
  public void empty_baseline() {
  }

  @Benchmark
  public int sum_baseline() {
    return 1 + 1;
  }

  @Benchmark
  public int const_baseline() {
    return 1;
  }

  @Benchmark
  public int directMemory() {
    return dequeMany(enqueueMany(new NonResizableDirectBufferQueue(size)));
  }

  @Benchmark
  public int heapMemory() {
    return dequeMany(enqueueMany(new NonResizableHeapBufferQueue(size)));
  }

  @Benchmark
  public int baseline() {
    int sum = 0;
    for (int aData : data) {
      sum += aData;
    }
    return sum;
  }
}
