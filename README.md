Data structure experiments
==========================

You might find horrible and bloody experiments on different data structures in the repository. And all this is done just to gain better understanding of its behaviour on modern hardware.

### Run Java benchmarks

```sh
$ gradle jmhJar
$ java -jar build/libs/sequential-1.0-jmh.jar -f 1 -i 5 -wi 5 -e .*ArrayQueue.* -gc true -tu us -prof hs_comp -prof hs_gc -prof hs_rt
$ java -jar build/libs/sequential-1.0-jmh.jar -f 1 -i 5 -wi 5 -gc true -tu us -bm ss -bm avgt -rf JSON -rff build/reports/jmh/time/results.json
$ java -jar build/libs/sequential-1.0-jmh.jar -f 1 -i 5 -wi 5 -gc true -tu us -bm thrpt -rf JSON -rff build/reports/jmh/ops/results.json
$ java -jar build/libs/sequential-1.0-jmh.jar -e '.*Linked.*|Non.*' -f 1 -wi 10 -i 10 -tu ms -rf JSON -rff build/reports/jmh/results.json
```

### WTF questions

For LinkedQueues the throughput benchmark shows almost linear dependency between decreasing of number of operations and increasing size of the queue, however the single shot benchmark shows that there is a time gap.
For ResizableArrayQueues the single shot benchmark shows that queue with if condition faster than with bitwise and operation, however the throughput benchmark shows the same on Intel(R) Core(TM) i7-3770 CPU @ 3.40GHz Ivy Bridge. Falsity - need more warm up iterations.