package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class LinkedArrayQueueBenchmark {

  @State(Scope.Benchmark)
  public static class Enqueueing {

    @Benchmark
    @OperationsPerInvocation(16 * 256)
    public LinkedArrayQueue enqueue_0016_000256() {
      return enqueue(16, 256);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 292)
    public LinkedArrayQueue enqueue_0016_000292() {
      return enqueue(16, 292);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 512)
    public LinkedArrayQueue enqueue_0016_000512() {
      return enqueue(16, 512);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 128)
    public LinkedArrayQueue enqueue_0032_000128() {
      return enqueue(32, 256);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 186)
    public LinkedArrayQueue enqueue_0032_000186() {
      return enqueue(32, 256);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 256)
    public LinkedArrayQueue enqueue_0032_000256() {
      return enqueue(32, 256);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 64)
    public LinkedArrayQueue enqueue_0064_0000064() {
      return enqueue(64, 64);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 108)
    public LinkedArrayQueue enqueue_0064_000108() {
      return enqueue(64, 256);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 128)
    public LinkedArrayQueue enqueue_0064_000128() {
      return enqueue(64, 128);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 32)
    public LinkedArrayQueue enqueue_0128_000032() {
      return enqueue(128, 32);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 58)
    public LinkedArrayQueue enqueue_0128_000058() {
      return enqueue(128, 58);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 64)
    public LinkedArrayQueue enqueue_0128_000064() {
      return enqueue(128, 64);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 16)
    public LinkedArrayQueue enqueue_0256_000016() {
      return enqueue(256, 16);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 30)
    public LinkedArrayQueue enqueue_0256_000030() {
      return enqueue(256, 30);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 32)
    public LinkedArrayQueue enqueue_0256_000032() {
      return enqueue(256, 32);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 8)
    public LinkedArrayQueue enqueue_0512_000008() {
      return enqueue(512, 8);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 15)
    public LinkedArrayQueue enqueue_0512_000015() {
      return enqueue(512, 15);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 16)
    public LinkedArrayQueue enqueue_0512_000016() {
      return enqueue(512, 16);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 4)
    public LinkedArrayQueue enqueue_1024_000004() {
      return enqueue(1024, 4);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 7)
    public LinkedArrayQueue enqueue_1024_000007() {
      return enqueue(1024, 7);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 8)
    public LinkedArrayQueue enqueue_1024_000008() {
      return enqueue(1024, 8);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 2048)
    public LinkedArrayQueue enqueue_0016_002048() {
      return enqueue(16, 2048);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 2340)
    public LinkedArrayQueue enqueue_0016_002340() {
      return enqueue(16, 2340);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 4096)
    public LinkedArrayQueue enqueue_0016_004096() {
      return enqueue(16, 4096);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 1024)
    public LinkedArrayQueue enqueue_0032_001024() {
      return enqueue(16, 1024);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 1489)
    public LinkedArrayQueue enqueue_0032_001489() {
      return enqueue(16, 1489);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 2048)
    public LinkedArrayQueue enqueue_0032_002048() {
      return enqueue(16, 2048);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 512)
    public LinkedArrayQueue enqueue_0064_000512() {
      return enqueue(64, 512);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 862)
    public LinkedArrayQueue enqueue_0064_000862() {
      return enqueue(64, 862);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 1024)
    public LinkedArrayQueue enqueue_0064_001024() {
      return enqueue(64, 1024);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 256)
    public LinkedArrayQueue enqueue_0128_000256() {
      return enqueue(128, 256);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 468)
    public LinkedArrayQueue enqueue_0128_000468() {
      return enqueue(128, 468);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 512)
    public LinkedArrayQueue enqueue_0128_000512() {
      return enqueue(128, 512);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 128)
    public LinkedArrayQueue enqueue_0256_000128() {
      return enqueue(256, 128);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 244)
    public LinkedArrayQueue enqueue_0256_000244() {
      return enqueue(256, 244);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 256)
    public LinkedArrayQueue enqueue_0256_000256() {
      return enqueue(256, 256);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 128)
    public LinkedArrayQueue enqueue_0512_000128() {
      return enqueue(512, 128);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 125)
    public LinkedArrayQueue enqueue_0512_000125() {
      return enqueue(512, 125);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 256)
    public LinkedArrayQueue enqueue_0512_000256() {
      return enqueue(512, 256);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 32)
    public LinkedArrayQueue enqueue_1024_000032() {
      return enqueue(1024, 32);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 63)
    public LinkedArrayQueue enqueue_1024_000063() {
      return enqueue(1024, 63);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 64)
    public LinkedArrayQueue enqueue_1024_000064() {
      return enqueue(1024, 64);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 65536)
    public LinkedArrayQueue enqueue_0016_065536() {
      return enqueue(16, 65536);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 74898)
    public LinkedArrayQueue enqueue_0016_074898() {
      return enqueue(16, 74898);
    }

    @Benchmark
    @OperationsPerInvocation(16 * 131172)
    public LinkedArrayQueue enqueue_0016_131172() {
      return enqueue(16, 131172);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 32768)
    public LinkedArrayQueue enqueue_0032_0032768() {
      return enqueue(32, 32768);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 47662)
    public LinkedArrayQueue enqueue_0032_047662() {
      return enqueue(32, 47662);
    }

    @Benchmark
    @OperationsPerInvocation(32 * 65536)
    public LinkedArrayQueue enqueue_0032_065536() {
      return enqueue(32, 65536);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 16384)
    public LinkedArrayQueue enqueue_0064_016384() {
      return enqueue(64, 16384);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 27594)
    public LinkedArrayQueue enqueue_0064_027594() {
      return enqueue(64, 27594);
    }

    @Benchmark
    @OperationsPerInvocation(64 * 32768)
    public LinkedArrayQueue enqueue_0064_032768() {
      return enqueue(64, 32768);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 8192)
    public LinkedArrayQueue enqueue_0128_008192() {
      return enqueue(128, 8192);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 14979)
    public LinkedArrayQueue enqueue_0128_014979() {
      return enqueue(128, 14979);
    }

    @Benchmark
    @OperationsPerInvocation(128 * 16384)
    public LinkedArrayQueue enqueue_0128_016384() {
      return enqueue(128, 16384);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 4096)
    public LinkedArrayQueue enqueue_0256_004096() {
      return enqueue(256, 4096);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 7825)
    public LinkedArrayQueue enqueue_0256_007825() {
      return enqueue(256, 7825);
    }

    @Benchmark
    @OperationsPerInvocation(256 * 8192)
    public LinkedArrayQueue enqueue_0256_008192() {
      return enqueue(256, 8192);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 2048)
    public LinkedArrayQueue enqueue_0512_002048() {
      return enqueue(512, 2048);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 4002)
    public LinkedArrayQueue enqueue_0512_004002() {
      return enqueue(512, 4002);
    }

    @Benchmark
    @OperationsPerInvocation(512 * 4096)
    public LinkedArrayQueue enqueue_0512_004096() {
      return enqueue(512, 4096);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 1024)
    public LinkedArrayQueue enqueue_1024_001024() {
      return enqueue(1024, 1024);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 2024)
    public LinkedArrayQueue enqueue_1024_002024() {
      return enqueue(1024, 2024);
    }

    @Benchmark
    @OperationsPerInvocation(1024 * 2048)
    public LinkedArrayQueue enqueue_1024_002048() {
      return enqueue(1024, 2048);
    }

    private LinkedArrayQueue enqueue(int segmentSize, int numberOfSegments) {
      LinkedArrayQueue queue = new LinkedArrayQueue(segmentSize);
      for (int i = 0; i < segmentSize * numberOfSegments; i++) {
        queue.enqueue(i);
      }
      return queue;
    }
  }

  private static abstract class QueueBenchmark {

    private LinkedArrayQueue queue;

    void createQueue(int segmentSize, int numberOfSegments) {
      queue = new LinkedArrayQueue(segmentSize);
      for (int i = 0; i < segmentSize * numberOfSegments; i++) {
        queue.enqueue(i);
      }
    }

    int enqueueDeque(int segmentSize, int numberOfSegments) {
      int sum = 0;
      for (int i = segmentSize * numberOfSegments; i < 2 * segmentSize * numberOfSegments; i++) {
        sum += queue.deque();
        queue.enqueue(i);
      }
      return sum;
    }
  }

  public static class LevelOneCache {

    @State(Scope.Benchmark)
    public static class __0016__000256 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 256;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0016__000292 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 292;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0016__000512 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 512;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__000064 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 64;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__000108 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 108;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__000128 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 128;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__000016 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 16;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__000030 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 30;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__000032 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 32;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__000004 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 4;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__000007 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 7;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__000008 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 8;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }
  }

  public static class LevelTwoCache {

    @State(Scope.Benchmark)
    public static class __0016__002048 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 2048;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0016__002340 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 2340;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0016__004096 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 4096;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__000512 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 512;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__000862 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 862;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__001024 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 1024;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__000128 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 128;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__000244 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 244;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__000512 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 512;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__000032 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 32;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__000063 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 63;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__000064 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 64;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }
  }

  public static class LevelThreeCache {

    @State(Scope.Benchmark)
    public static class __0016__065536 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 65536;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0016__074898 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 74898;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0016__131172 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 16;
      private static final int NUMBER_OF_SEGMENTS = 131172;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__0016384 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 16384;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__0027594 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 27594;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0064__0032768 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 64;
      private static final int NUMBER_OF_SEGMENTS = 32768;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__008192 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 8192;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__007825 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 7825;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __0256__0016384 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 256;
      private static final int NUMBER_OF_SEGMENTS = 16384;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__002048 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 2048;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__002024 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 2024;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }

    @State(Scope.Benchmark)
    public static class __1024__004096 extends QueueBenchmark {

      private static final int SEGMENT_SIZE = 1024;
      private static final int NUMBER_OF_SEGMENTS = 4096;

      @Setup
      public void setUp() {
        createQueue(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }

      @Benchmark
      @OperationsPerInvocation(SEGMENT_SIZE * NUMBER_OF_SEGMENTS)
      public int enqueueDeque() {
        return enqueueDeque(SEGMENT_SIZE, NUMBER_OF_SEGMENTS);
      }
    }
  }
}
