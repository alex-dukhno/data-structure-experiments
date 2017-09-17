package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jol.info.ClassLayout;

import java.util.Random;

public class ContiguousAllocation extends QueueBenchmark {

  @Param({"1", "4", "16", "64", "256"})
  private int numberOfObjects;

  private Padding[] paddings;
  private LinkedQueuePrimitive primitives;
  private LinkedQueueBoxed boxed;

  @Setup
  public void setUp() throws Exception {
    Random r = new Random();

    paddings = new Padding[numberOfObjects];

    primitives = new LinkedQueuePrimitive();
    for (int i = 0; i < size; i++) {
      primitives.enqueue(r.nextInt());
      for (int j = 0; j < numberOfObjects; j++) {
        paddings[j] = new Padding();
      }
    }

    boxed = new LinkedQueueBoxed();
    for (int i = 0; i < size; i++) {
      boxed.enqueue(r.nextInt());
      for (int j = 0; j < numberOfObjects; j++) {
        paddings[j] = new Padding();
      }
    }
  }

  @Benchmark
  public int deque_primitives() {
    return dequeManySumWhile(primitives);
  }

  @Benchmark
  public int deque_boxed() {
    return dequeManySumWhile(boxed);
  }

  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(Padding.class).toPrintable());
  }

  private static class Padding {
    int i1;
  }
}
