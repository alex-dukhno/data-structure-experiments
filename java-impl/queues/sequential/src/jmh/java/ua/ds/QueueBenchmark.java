package ua.ds;

import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Queue;
import java.util.Random;

@State(Scope.Benchmark)
public abstract class QueueBenchmark {
  private static final int K = 1024;

  @Param({""+K, ""+2*K, ""+4*K, ""+8*K, ""+16*K, ""+32*K, ""+64*K, ""+128*K, ""+256*K, ""+512*K, ""+K*K})
  int size;

  private int[] data;

  @Setup(Level.Iteration)
  public void populateData() {
    Random r = new Random();
    data = new int[size];
    for (int i = 0; i < data.length; i++) {
      data[i] = r.nextInt();
    }
  }

  final SequentialQueue enqueueMany(SequentialQueue queue) {
    for (int i = 0; i < size; i++) {
      queue.enqueue(data[i]);
    }
    return queue;
  }

  final void dequeMany(Blackhole blackhole, SequentialQueue queue) {
    for (int i = 0; i < size; i++) {
      blackhole.consume(queue.deque());
    }
  }

  final int dequeManySum(SequentialQueue queue) {
    int sum = 0;
    for (int i = 0; i < size; i++) {
      sum += queue.deque();
    }
    return sum;
  }

  final int dequeManySumWhile(SequentialQueue queue) {
    int sum = 0;
    int item;
    while ((item = queue.deque()) != -1) {
      sum += item;
    }
    return sum;
  }

  final Queue<Integer> enqueueMany(Queue<Integer> queue) {
    for (int i = 0; i < size; i++) {
      queue.add(data[i]);
    }
    return queue;
  }

  final void dequeMany(Blackhole blackhole, Queue<Integer> queue) {
    for (int i = 0; i < size; i++) {
      blackhole.consume(queue.poll());
    }
  }
}
