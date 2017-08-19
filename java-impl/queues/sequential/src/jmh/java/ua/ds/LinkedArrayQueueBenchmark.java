package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class LinkedArrayQueueBenchmark {

  @State(Scope.Benchmark)
  public static class LinkedArrayQueueEnqueue {

    @Benchmark
    public LinkedArrayQueue _0016_00000512() {
      return enqueue(16, 512);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00000512() {
      return enqueue(32, 512);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00000512() {
      return enqueue(64, 512);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00000512() {
      return enqueue(128, 512);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00000512() {
      return enqueue(256, 512);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00001024() {
      return enqueue(16, 1024);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00001024() {
      return enqueue(32, 1024);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00001024() {
      return enqueue(64, 1024);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00001024() {
      return enqueue(128, 1024);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00001024() {
      return enqueue(256, 1024);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00002048() {
      return enqueue(16, 2048);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00002048() {
      return enqueue(32, 2048);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00002048() {
      return enqueue(64, 2048);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00002048() {
      return enqueue(128, 2048);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00002048() {
      return enqueue(256, 2048);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00004096() {
      return enqueue(16, 4096);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00004096() {
      return enqueue(32, 4096);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00004096() {
      return enqueue(64, 4096);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00004096() {
      return enqueue(128, 4096);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00004096() {
      return enqueue(256, 4096);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00008192() {
      return enqueue(16, 8192);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00008192() {
      return enqueue(32, 8192);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00008192() {
      return enqueue(64, 8192);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00008192() {
      return enqueue(128, 8192);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00008192() {
      return enqueue(256, 8192);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00016384() {
      return enqueue(16, 16384);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00016384() {
      return enqueue(32, 16384);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00016384() {
      return enqueue(64, 16384);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00016384() {
      return enqueue(128, 16384);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00016384() {
      return enqueue(256, 16384);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00032768() {
      return enqueue(16, 32768);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00032768() {
      return enqueue(32, 32768);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00032768() {
      return enqueue(64, 32768);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00032768() {
      return enqueue(128, 32768);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00032768() {
      return enqueue(256, 32768);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00065536() {
      return enqueue(16, 65536);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00065536() {
      return enqueue(32, 65536);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00065536() {
      return enqueue(64, 65536);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00065536() {
      return enqueue(128, 65536);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00065536() {
      return enqueue(256, 65536);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00131072() {
      return enqueue(16, 131072);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00131072() {
      return enqueue(32, 131072);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00131072() {
      return enqueue(64, 131072);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00131072() {
      return enqueue(128, 131072);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00131072() {
      return enqueue(256, 131072);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00262144() {
      return enqueue(16, 262144);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00262144() {
      return enqueue(32, 262144);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00262144() {
      return enqueue(64, 262144);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00262144() {
      return enqueue(128, 262144);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00262144() {
      return enqueue(256, 262144);
    }

    @Benchmark
    public LinkedArrayQueue _0016_00524288() {
      return enqueue(16, 524288);
    }

    @Benchmark
    public LinkedArrayQueue _0032_00524288() {
      return enqueue(32, 524288);
    }

    @Benchmark
    public LinkedArrayQueue _0064_00524288() {
      return enqueue(64, 524288);
    }

    @Benchmark
    public LinkedArrayQueue _0128_00524288() {
      return enqueue(128, 524288);
    }

    @Benchmark
    public LinkedArrayQueue _0256_00524288() {
      return enqueue(256, 524288);
    }

    @Benchmark
    public LinkedArrayQueue _0016_01048576() {
      return enqueue(16, 1048576);
    }

    @Benchmark
    public LinkedArrayQueue _0032_01048576() {
      return enqueue(32, 1048576);
    }

    @Benchmark
    public LinkedArrayQueue _0064_01048576() {
      return enqueue(64, 1048576);
    }

    @Benchmark
    public LinkedArrayQueue _0128_01048576() {
      return enqueue(128, 1048576);
    }

    @Benchmark
    public LinkedArrayQueue _0256_01048576() {
      return enqueue(256, 1048576);
    }
  }

  @State(Scope.Benchmark)
  public static class LinkedArrayQueueEnqueueDeque {

    @Benchmark
    public int _0016_00000512() {
      return deque(enqueue(16, 512), 512);
    }

    @Benchmark
    public int _0032_00000512() {
      return deque(enqueue(32, 512), 512);
    }

    @Benchmark
    public int _0064_00000512() {
      return deque(enqueue(64, 512), 512);
    }

    @Benchmark
    public int _0128_00000512() {
      return deque(enqueue(128, 512), 512);
    }

    @Benchmark
    public int _0256_00000512() {
      return deque(enqueue(256, 512), 512);
    }

    @Benchmark
    public int _0016_00001024() {
      return deque(enqueue(16, 1024), 1024);
    }

    @Benchmark
    public int _0032_00001024() {
      return deque(enqueue(32, 1024), 1024);
    }

    @Benchmark
    public int _0064_00001024() {
      return deque(enqueue(64, 1024), 1024);
    }

    @Benchmark
    public int _0128_00001024() {
      return deque(enqueue(128, 1024), 1024);
    }

    @Benchmark
    public int _0256_00001024() {
      return deque(enqueue(256, 1024), 1024);
    }

    @Benchmark
    public int _0016_00002048() {
      return deque(enqueue(16, 2048), 2048);
    }

    @Benchmark
    public int _0032_00002048() {
      return deque(enqueue(32, 2048), 2048);
    }

    @Benchmark
    public int _0064_00002048() {
      return deque(enqueue(64, 2048), 2048);
    }

    @Benchmark
    public int _0128_00002048() {
      return deque(enqueue(128, 2048), 2048);
    }

    @Benchmark
    public int _0256_00002048() {
      return deque(enqueue(256, 2048), 2048);
    }

    @Benchmark
    public int _0016_00004096() {
      return deque(enqueue(16, 4096), 4096);
    }

    @Benchmark
    public int _0032_00004096() {
      return deque(enqueue(32, 4096), 4096);
    }

    @Benchmark
    public int _0064_00004096() {
      return deque(enqueue(64, 4096), 4096);
    }

    @Benchmark
    public int _0128_00004096() {
      return deque(enqueue(128, 4096), 4096);
    }

    @Benchmark
    public int _0256_00004096() {
      return deque(enqueue(256, 4096), 4096);
    }

    @Benchmark
    public int _0016_00008192() {
      return deque(enqueue(16, 8192), 8192);
    }

    @Benchmark
    public int _0032_00008192() {
      return deque(enqueue(32, 8192), 8192);
    }

    @Benchmark
    public int _0064_00008192() {
      return deque(enqueue(64, 8192), 8192);
    }

    @Benchmark
    public int _0128_00008192() {
      return deque(enqueue(128, 8192), 8192);
    }

    @Benchmark
    public int _0256_00008192() {
      return deque(enqueue(256, 8192), 8192);
    }

    @Benchmark
    public int _0016_00016384() {
      return deque(enqueue(16, 16384), 16384);
    }

    @Benchmark
    public int _0032_00016384() {
      return deque(enqueue(32, 16384), 16384);
    }

    @Benchmark
    public int _0064_00016384() {
      return deque(enqueue(64, 16384), 16384);
    }

    @Benchmark
    public int _0128_00016384() {
      return deque(enqueue(128, 16384), 16384);
    }

    @Benchmark
    public int _0256_00016384() {
      return deque(enqueue(256, 16384), 16384);
    }

    @Benchmark
    public int _0016_00032768() {
      return deque(enqueue(16, 32768), 32768);
    }

    @Benchmark
    public int _0032_00032768() {
      return deque(enqueue(32, 32768), 32768);
    }

    @Benchmark
    public int _0064_00032768() {
      return deque(enqueue(64, 32768), 32768);
    }

    @Benchmark
    public int _0128_00032768() {
      return deque(enqueue(128, 32768), 32768);
    }

    @Benchmark
    public int _0256_00032768() {
      return deque(enqueue(256, 32768), 32768);
    }

    @Benchmark
    public int _0016_00065536() {
      return deque(enqueue(16, 65536), 65536);
    }

    @Benchmark
    public int _0032_00065536() {
      return deque(enqueue(32, 65536), 65536);
    }

    @Benchmark
    public int _0064_00065536() {
      return deque(enqueue(64, 65536), 65536);
    }

    @Benchmark
    public int _0128_00065536() {
      return deque(enqueue(128, 65536), 65536);
    }

    @Benchmark
    public int _0256_00065536() {
      return deque(enqueue(256, 65536), 65536);
    }

    @Benchmark
    public int _0016_00131072() {
      return deque(enqueue(16, 131072), 131072);
    }

    @Benchmark
    public int _0032_00131072() {
      return deque(enqueue(32, 131072), 131072);
    }

    @Benchmark
    public int _0064_00131072() {
      return deque(enqueue(64, 131072), 131072);
    }

    @Benchmark
    public int _0128_00131072() {
      return deque(enqueue(128, 131072), 131072);
    }

    @Benchmark
    public int _0256_00131072() {
      return deque(enqueue(256, 131072), 131072);
    }

    @Benchmark
    public int _0016_00262144() {
      return deque(enqueue(16, 262144), 262144);
    }

    @Benchmark
    public int _0032_00262144() {
      return deque(enqueue(32, 262144), 262144);
    }

    @Benchmark
    public int _0064_00262144() {
      return deque(enqueue(64, 262144), 262144);
    }

    @Benchmark
    public int _0128_00262144() {
      return deque(enqueue(128, 262144), 262144);
    }

    @Benchmark
    public int _0256_00262144() {
      return deque(enqueue(256, 262144), 262144);
    }

    @Benchmark
    public int _0016_00524288() {
      return deque(enqueue(16, 524288), 524288);
    }

    @Benchmark
    public int _0032_00524288() {
      return deque(enqueue(32, 524288), 524288);
    }

    @Benchmark
    public int _0064_00524288() {
      return deque(enqueue(64, 524288), 524288);
    }

    @Benchmark
    public int _0128_00524288() {
      return deque(enqueue(128, 524288), 524288);
    }

    @Benchmark
    public int _0256_00524288() {
      return deque(enqueue(256, 524288), 524288);
    }

    @Benchmark
    public int _0016_01048576() {
      return deque(enqueue(16, 1048576), 1048576);
    }

    @Benchmark
    public int _0032_01048576() {
      return deque(enqueue(32, 1048576), 1048576);
    }

    @Benchmark
    public int _0064_01048576() {
      return deque(enqueue(64, 1048576), 1048576);
    }

    @Benchmark
    public int _0128_01048576() {
      return deque(enqueue(128, 1048576), 1048576);
    }

    @Benchmark
    public int _0256_01048576() {
      return deque(enqueue(256, 1048576), 1048576);
    }
  }

  private static LinkedArrayQueue enqueue(int segmentSize, int iterations) {
    LinkedArrayQueue queue = new LinkedArrayQueue(segmentSize);
    for (int i = 0; i < iterations; i++) {
      queue.enqueue(i);
    }
    return queue;
  }

  private static int deque(LinkedArrayQueue queue, int iterations) {
    int sum = 0;
    for (int i = 0; i < iterations; i++) {
      sum += queue.deque();
    }
    return sum;
  }
}
