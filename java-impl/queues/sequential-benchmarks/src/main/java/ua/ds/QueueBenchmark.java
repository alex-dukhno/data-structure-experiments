package ua.ds;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Random;

@State(Scope.Benchmark)
public abstract class QueueBenchmark extends QueueMethods {

  @Param({""+K, ""+2*K, ""+4*K, ""+8*K, ""+16*K, ""+32*K, ""+64*K, ""+128*K, ""+256*K, ""+512*K, ""+M, ""+2*M, ""+4*M, ""+8*M, ""+16*M, ""+32*M})
  int size;

  @Setup(Level.Invocation)
  public void populateData() {
    Random r = new Random();
    data = new int[size];
    for (int i = 0; i < data.length; i++) {
      data[i] = r.nextInt();
    }
  }
}
