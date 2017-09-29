package ua.ds;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Queue;
import java.util.Random;

@State(Scope.Benchmark)
public abstract class QueueBenchmark {
  private static final int K = 1024;
  private static final int M = K * K;

  @Param({""+K, ""+2*K, ""+4*K, ""+8*K, ""+16*K, ""+32*K, ""+64*K, ""+128*K, ""+256*K, ""+512*K, ""+M, ""+2*M, ""+4*M, ""+8*M, ""+16*M, ""+32*M})
  int size;

  private int[] data;

  @Setup(Level.Invocation)
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

  final SequentialQueueBoxed enqueueMany(SequentialQueueBoxed queue) {
    for (int i = 0; i < size; i++) {
      queue.enqueue(data[i]);
    }
    return queue;
  }

  final int dequeMany(SequentialQueue queue) {
    int sum = 0;
    int item;
    while ((item = queue.deque()) != -1) {
      sum += item;
    }
    return sum;
  }

  final Integer dequeMany(SequentialQueueBoxed queue) {
    Integer sum = 0;
    Integer item;
    while ((item = queue.deque()) != null) {
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

  final int dequeMany(Queue<Integer> queue) {
    Integer sum = 0;
    Integer item;
    while ((item = queue.poll()) != null) {
      sum += item;
    }
    return sum;
  }
}
