package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.CompilerControl.Mode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jol.info.ClassLayout;

import java.util.Random;

@State(Scope.Benchmark)
public class ContiguousAllocation {

  private static final int PRIME = 393342739;
  private static final int M = 1024 * 1024;

  @Param({""+M, ""+2*M, ""+4*M})
  private int size;

  private Integer[] items;
  private LinkedQueueBoxed boxed1;
  private LinkedQueueBoxed boxed2;

  @Setup(Level.Invocation)
  public void setUp() throws Exception {
    Random r = new Random();

    items = new Integer[size];
    for (int i = 0; i < size; i++) {
      items[i] = r.nextInt();
    }

    boxed1 = new LinkedQueueBoxed();
    boxed2 = new LinkedQueueBoxed();
    int pos = 1;
    int mask = size - 1;
    for (int i = 0; i < size; i++) {
      pos = (pos * PRIME) & mask;
      boxed1.enqueue(new Integer(items[i].intValue()));
      boxed2.enqueue(items[pos]);
    }
  }

  @Benchmark
  public Integer deque_boxed1(Blackhole blackhole) {
    return dequeMany(blackhole, boxed1);
  }

  @Benchmark
  public Integer deque_boxed2(Blackhole blackhole) {
    return dequeMany(blackhole, boxed2);
  }

  private Integer dequeMany(Blackhole blackhole, SequentialQueueBoxed queue) {
    Integer sum = 0;
    Integer item;
    while ((item = queue.deque()) != null) {
      sum += item;
    }
    return sum;
  }
}
