package ua.ds;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

//    ua.ds.LinkedQueue object internals:
//    OFFSET  SIZE                     TYPE DESCRIPTION                           VALUE
//         0    12                          (object header)                           N/A
//        12     4   ua.ds.LinkedQueue.Node LinkedQueue.head                          N/A
//        16     4   ua.ds.LinkedQueue.Node LinkedQueue.tail                          N/A
//        20     4                          (loss due to the next object alignment)
//    Instance size: 24 bytes
//    Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
//
//    ua.ds.LinkedQueue$Node object internals:
//    OFFSET  SIZE                     TYPE DESCRIPTION                           VALUE
//         0    12                          (object header)                           N/A
//        12     4                      int Node.item                                 N/A
//        16     4   ua.ds.LinkedQueue.Node Node.next                                 N/A
//        20     4        ua.ds.LinkedQueue Node.this$0                               N/A
//    Instance size: 24 bytes
//    Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
//
//    L1D 32KB  32768       1024    1364   2048
//    L2  256KB 262144     10921    8192  16384
//    L3  3MB   3145728   131071  262144  65536
//
//    Benchmark                   (size)  Mode  Cnt         Score         Error  Units
//    LinkedQueueBenchmark.enqueue        16  avgt   10         0.079 ±       0.001  us/op
//    LinkedQueueBenchmark.enqueue        64  avgt   10         0.305 ±       0.008  us/op
//    LinkedQueueBenchmark.enqueue       256  avgt   10         1.186 ±       0.027  us/op
//    LinkedQueueBenchmark.enqueue      1024  avgt   10         4.725 ±       0.128  us/op
//    LinkedQueueBenchmark.enqueue      4096  avgt   10        18.855 ±       0.468  us/op
//    LinkedQueueBenchmark.enqueue     16384  avgt   10        75.848 ±       2.060  us/op
//    LinkedQueueBenchmark.enqueue     65536  avgt   10       288.787 ±       9.689  us/op
//    LinkedQueueBenchmark.enqueue    262144  avgt   10      1278.428 ±      83.620  us/op
//    LinkedQueueBenchmark.enqueue   1048576  avgt   10      6777.648 ±    1888.767  us/op
//    LinkedQueueBenchmark.enqueue   4194304  avgt   10     86703.709 ±   49983.645  us/op
//    LinkedQueueBenchmark.enqueue  16777216  avgt   10   2591155.419 ± 1392119.205  us/op
//    LinkedQueueBenchmark.enqueue  67108864  avgt   10  22712012.043 ± 1142984.114  us/op
public class LinkedQueueBenchmark {

  //  Benchmark                                         (initialSize)  (iterations)  Mode  Cnt         Score         Error  Units
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A            16  avgt   10         0.081 ±       0.003  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A            64  avgt   10         0.294 ±       0.005  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A           256  avgt   10         1.160 ±       0.024  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A          1024  avgt   10         4.555 ±       0.066  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A          4096  avgt   10        18.279 ±       0.534  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A         16384  avgt   10        72.839 ±       2.172  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A         65536  avgt   10       279.199 ±      12.602  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A        262144  avgt   10      1194.873 ±      32.454  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A       1048576  avgt   10      6840.408 ±     642.689  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A       4194304  avgt   10     63883.260 ±   13037.911  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A      16777216  avgt   10   2620600.622 ± 1398097.837  us/op
//  LinkedQueueBenchmark.Enqueueing.enqueue                         N/A      67108864  avgt   10  23069717.842 ± 2146541.602  us/op
  @State(Scope.Benchmark)
  public static class Enqueueing {

    @Benchmark
    @OperationsPerInvocation(1024)
    public LinkedQueue enqueueLessThanL1Size() {
      return enqueue(1024);
    }

    @Benchmark
    @OperationsPerInvocation(1364)
    public LinkedQueue enqueueExactAsL1Size() {
      return enqueue(1364);
    }

    @Benchmark
    @OperationsPerInvocation(2048)
    public LinkedQueue enqueueMoreThanL1Size() {
      return enqueue(2048);
    }

    @Benchmark
    @OperationsPerInvocation(8192)
    public LinkedQueue enqueueLessThanL2Size() {
      return enqueue(8192);
    }

    @Benchmark
    @OperationsPerInvocation(10921)
    public LinkedQueue enqueueExactAsL2Size() {
      return enqueue(10921);
    }

    @Benchmark
    @OperationsPerInvocation(16384)
    public LinkedQueue enqueueMoreThanL2Size() {
      return enqueue(16384);
    }

    @Benchmark
    @OperationsPerInvocation(131072)
    public LinkedQueue enqueueLessThanL3Size() {
      return enqueue(131072);
    }

    @Benchmark
    @OperationsPerInvocation(349524)
    public LinkedQueue enqueueExactAsL3Size() {
      return enqueue(349524);
    }

    @Benchmark
    @OperationsPerInvocation(524288)
    public LinkedQueue enqueueMoreThanL3Size() {
      return enqueue(524288);
    }

    private LinkedQueue enqueue(int iterations) {
      LinkedQueue queue = new LinkedQueue();
      for (int i = 0; i < iterations; i++) {
        queue.enqueue(i);
      }
      return queue;
    }
  }

  //  Benchmark                                         (initialSize)  (iterations)  Mode  Cnt         Score         Error  Units
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16            16  avgt   10         0.095 ±       0.001  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16            64  avgt   10         0.368 ±       0.009  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16           256  avgt   10         2.125 ±       0.045  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16          1024  avgt   10         7.814 ±       0.233  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16          4096  avgt   10        31.537 ±       0.403  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16         16384  avgt   10       125.281 ±      10.547  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16         65536  avgt   10       476.509 ±       8.576  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16        262144  avgt   10      1889.425 ±      22.669  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16       1048576  avgt   10      7596.187 ±     169.675  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16       4194304  avgt   10     30247.455 ±     862.016  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16      16777216  avgt   10    120447.963 ±    3565.164  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             16      67108864  avgt   10    481404.548 ±    7939.096  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32            16  avgt   10         0.116 ±       0.004  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32            64  avgt   10         0.367 ±       0.008  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32           256  avgt   10         1.873 ±       0.035  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32          1024  avgt   10         7.788 ±       0.165  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32          4096  avgt   10        31.726 ±       0.712  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32         16384  avgt   10       127.643 ±      13.350  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32         65536  avgt   10       479.335 ±      12.364  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32        262144  avgt   10      1905.944 ±      85.699  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32       1048576  avgt   10      7501.356 ±     187.599  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32       4194304  avgt   10     29990.200 ±     621.559  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32      16777216  avgt   10    120772.662 ±    2523.308  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             32      67108864  avgt   10    479246.694 ±    6999.424  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64            16  avgt   10         0.097 ±       0.003  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64            64  avgt   10         0.400 ±       0.009  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64           256  avgt   10         1.940 ±       0.033  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64          1024  avgt   10         9.966 ±       0.185  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64          4096  avgt   10        32.012 ±       1.111  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64         16384  avgt   10       125.024 ±      10.623  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64         65536  avgt   10       473.072 ±       7.982  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64        262144  avgt   10      1888.214 ±      32.057  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64       1048576  avgt   10      7648.617 ±     311.588  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64       4194304  avgt   10     30240.294 ±     617.391  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64      16777216  avgt   10    160460.037 ±    2992.573  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque             64      67108864  avgt   10    487244.271 ±    9864.533  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128            16  avgt   10         0.095 ±       0.002  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128            64  avgt   10         0.365 ±       0.010  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128           256  avgt   10         1.924 ±       0.040  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128          1024  avgt   10         7.761 ±       0.147  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128          4096  avgt   10        31.773 ±       0.949  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128         16384  avgt   10       125.919 ±      13.027  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128         65536  avgt   10       475.471 ±       9.305  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128        262144  avgt   10      1879.844 ±      36.162  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128       1048576  avgt   10      7665.123 ±     468.590  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128       4194304  avgt   10     41579.734 ±    2486.765  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128      16777216  avgt   10    121020.520 ±    2395.534  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            128      67108864  avgt   10    478168.150 ±   10419.506  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256            16  avgt   10         0.113 ±       0.005  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256            64  avgt   10         0.369 ±       0.009  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256           256  avgt   10         1.904 ±       0.033  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256          1024  avgt   10         7.826 ±       0.306  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256          4096  avgt   10        31.825 ±       1.990  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256         16384  avgt   10       126.177 ±      10.535  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256         65536  avgt   10       475.290 ±      24.141  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256        262144  avgt   10      1875.821 ±      37.555  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256       1048576  avgt   10      7381.469 ±     112.046  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256       4194304  avgt   10     30289.783 ±    1148.063  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256      16777216  avgt   10    126694.368 ±   16870.377  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            256      67108864  avgt   10    489958.224 ±   10768.777  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512            16  avgt   10         0.111 ±       0.002  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512            64  avgt   10         0.439 ±       0.009  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512           256  avgt   10         1.967 ±       0.027  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512          1024  avgt   10         8.293 ±       0.193  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512          4096  avgt   10        41.303 ±       1.078  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512         16384  avgt   10       135.663 ±       6.057  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512         65536  avgt   10       628.188 ±      27.521  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512        262144  avgt   10      2084.825 ±      41.851  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512       1048576  avgt   10      8337.563 ±     172.088  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512       4194304  avgt   10     33448.897 ±     713.987  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512      16777216  avgt   10    133716.399 ±    3532.742  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque            512      67108864  avgt   10    533179.717 ±   12231.631  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024            16  avgt   10         0.130 ±       0.003  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024            64  avgt   10         0.505 ±       0.009  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024           256  avgt   10         2.035 ±       0.040  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024          1024  avgt   10         8.075 ±       0.227  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024          4096  avgt   10        34.372 ±       0.901  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024         16384  avgt   10       136.744 ±       6.542  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024         65536  avgt   10       526.494 ±      12.178  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024        262144  avgt   10      2095.823 ±      41.705  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024       1048576  avgt   10      8416.337 ±     336.780  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024       4194304  avgt   10     33425.995 ±     805.437  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024      16777216  avgt   10    133379.858 ±    2458.607  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           1024      67108864  avgt   10    535448.261 ±   11650.299  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048            16  avgt   10         0.113 ±       0.004  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048            64  avgt   10         0.485 ±       0.076  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048           256  avgt   10         2.082 ±       0.013  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048          1024  avgt   10         9.224 ±       0.121  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048          4096  avgt   10        40.963 ±       1.381  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048         16384  avgt   10       141.449 ±      20.747  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048         65536  avgt   10       537.586 ±      14.157  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048        262144  avgt   10      2151.227 ±      33.564  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048       1048576  avgt   10      8421.924 ±     215.337  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048       4194304  avgt   10     33451.816 ±     670.310  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048      16777216  avgt   10    133132.434 ±    3397.687  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           2048      67108864  avgt   10    533604.608 ±   18848.830  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096            16  avgt   10         0.113 ±       0.002  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096            64  avgt   10         0.438 ±       0.012  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096           256  avgt   10         2.257 ±       0.076  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096          1024  avgt   10         8.979 ±       0.240  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096          4096  avgt   10        34.868 ±       0.773  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096         16384  avgt   10       144.437 ±       5.934  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096         65536  avgt   10       568.059 ±      16.176  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096        262144  avgt   10      2256.002 ±      34.557  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096       1048576  avgt   10      8999.313 ±     251.027  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096       4194304  avgt   10     36094.469 ±     791.780  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096      16777216  avgt   10    143230.173 ±    2747.704  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           4096      67108864  avgt   10    577159.624 ±   20879.075  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192            16  avgt   10         0.118 ±       0.003  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192            64  avgt   10         0.449 ±       0.020  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192           256  avgt   10         2.355 ±       0.065  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192          1024  avgt   10         9.081 ±       0.239  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192          4096  avgt   10        37.727 ±       1.206  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192         16384  avgt   10       144.171 ±       5.844  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192         65536  avgt   10       605.937 ±      17.695  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192        262144  avgt   10      2453.750 ±      67.487  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192       1048576  avgt   10      9822.456 ±     330.813  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192       4194304  avgt   10     39114.077 ±     937.314  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192      16777216  avgt   10    155470.178 ±    1848.224  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque           8192      67108864  avgt   10    624932.922 ±   13023.155  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384            16  avgt   10         0.112 ±       0.003  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384            64  avgt   10         0.449 ±       0.012  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384           256  avgt   10         2.579 ±       0.096  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384          1024  avgt   10         9.166 ±       0.300  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384          4096  avgt   10        37.529 ±       0.892  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384         16384  avgt   10       140.700 ±      13.123  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384         65536  avgt   10       607.150 ±       8.780  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384        262144  avgt   10      2450.203 ±      39.337  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384       1048576  avgt   10      9831.580 ±     262.968  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384       4194304  avgt   10     39687.539 ±    1049.430  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384      16777216  avgt   10    156765.891 ±    2860.171  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          16384      67108864  avgt   10    627163.313 ±   11656.706  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678            16  avgt   10         0.113 ±       0.003  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678            64  avgt   10         0.486 ±       0.008  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678           256  avgt   10         2.081 ±       0.061  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678          1024  avgt   10         9.352 ±       0.616  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678          4096  avgt   10        37.628 ±       1.168  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678         16384  avgt   10       151.035 ±      14.557  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678         65536  avgt   10       579.731 ±      19.014  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678        262144  avgt   10      2316.784 ±      57.536  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678       1048576  avgt   10      9311.824 ±     618.621  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678       4194304  avgt   10     37373.911 ±    2867.706  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678      16777216  avgt   10    147445.849 ±    4835.149  us/op
//  LinkedQueueBenchmark.EnqueueingDequeing.enqueueDeque          32678      67108864  avgt   10    589466.921 ±   10619.514  us/op
  @State(Scope.Benchmark)
  public static class EnqueueingDequeing {

    @Param({
        "1024", "1364", "2048",     //L1D
        "8192", "10921", "16384",   //L2
        "65536", "131071", "262144" //L3
    })
    private int initialSize;

    private LinkedQueue queue;

    @Setup
    public void createQueue() {
      queue = new LinkedQueue();
      for (int i = 0; i < initialSize; i++) {
        queue.enqueue(i);
      }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public int enqueueDeque1024() {
      return enqueueDeque(1024);
    }

    @Benchmark
    @OperationsPerInvocation(1364)
    public int enqueueDeque1364() {
      return enqueueDeque(1364);
    }

    @Benchmark
    @OperationsPerInvocation(2048)
    public int enqueueDeque2048() {
      return enqueueDeque(2048);
    }

    @Benchmark
    @OperationsPerInvocation(8192)
    public int enqueueDeque8192() {
      return enqueueDeque(8192);
    }

    @Benchmark
    @OperationsPerInvocation(10921)
    public int enqueueDeque10921() {
      return enqueueDeque(10921);
    }

    @Benchmark
    @OperationsPerInvocation(16384)
    public int enqueueDeque16384() {
      return enqueueDeque(16384);
    }

    @Benchmark
    @OperationsPerInvocation(65536)
    public int enqueueDeque65536() {
      return enqueueDeque(65536);
    }

    @Benchmark
    @OperationsPerInvocation(131071)
    public int enqueueDeque131071() {
      return enqueueDeque(131071);
    }

    @Benchmark
    @OperationsPerInvocation(262144)
    public int enqueueDeque262144() {
      return enqueueDeque(262144);
    }

    private int enqueueDeque(int iterations) {
      int sum = 0;
      for (int i = 0; i < iterations; i++) {
        sum += queue.deque();
        queue.enqueue(i);
      }
      return sum;
    }
  }
}
