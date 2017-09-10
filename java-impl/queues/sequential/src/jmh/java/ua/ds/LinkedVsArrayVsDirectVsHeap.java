package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayDeque;
import java.util.LinkedList;

public class LinkedVsArrayVsDirectVsHeap extends QueueBenchmark {

  @Benchmark
  public void linked(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new LinkedQueue()));
  }

  @Benchmark
  public void linked_jdk(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new LinkedList<>()));
  }

  @Benchmark
  public void arrayBase(Blackhole blackhole) {
    dequeMany(blackhole, enqueueMany(new NonResizableArrayQueuePrimitive(size)));
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
}
