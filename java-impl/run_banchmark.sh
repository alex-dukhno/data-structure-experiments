#!/usr/bin/env bash

gradle clean jmhJar \
&& cd queues/sequential \
&& mkdir -p build/reports/jmh/linked/ \
&& java -jar build/libs/sequential-1.0-jmh.jar -e 'BitAndResizableArrayQueueBenchmark|BranchResizableArrayQueueBenchmark|LinkedArrayQueueBenchmark|NonResizableArrayQueueBenchmark' -f 1 -wi 10 -i 10 -gc true -tu s -bm ss -rf JSON -rff build/reports/jmh/linked/results.json \
&& mkdir -p build/reports/jmh/array/ \
&& java -jar build/libs/sequential-1.0-jmh.jar -e 'LinkedArrayQueueBenchmark|LinkedQueueBenchmark|NonResizableArrayQueueBenchmark' -f 1 -wi 10 -i 10 -tu ms -bm thrpt -rf JSON -rff build/reports/jmh/array/results.json \
&& mkdir -p build/reports/jmh/linked-array/ \
&& java -jar build/libs/sequential-1.0-jmh.jar -e 'BitAndResizableArrayQueueBenchmark|BranchResizableArrayQueueBenchmark|LinkedQueueBenchmark|NonResizableArrayQueueBenchmark' -bm thrpt -f 1 -wi 10 -i 10 -tu s -rf JSON -rff build/reports/jmh/linked-array/results.json \
&& mkdir -p build/reports/jmh/all/ \
&& java -jar build/libs/sequential-1.0-jmh.jar -bm thrpt -bm avgt -f 1 -wi 10 -i 10 -tu s -rf JSON -rff build/reports/jmh/all/results.json \
&& cd ../.. \
&& cd queues/blocking \
&& mkdir -p build/reports/jmh/lock-thrpt \
&& java -jar build/libs/blocking-1.0-jmh.jar -e 'DoubleLockLinkedBlockingQueuePaddedBenchmarks|SingleLockLinkedBlockingQueuePaddedBenchmarks' -f 1 -i 10 -wi 10  -gc true -tu s -bm thrpt -rf JSON -rff build/reports/jmh/lock-thrpt/results.json \
&& mkdir -p build/reports/jmh/lock-padded \
&& java -jar build/libs/blocking-1.0-jmh.jar -e 'ArrayBlockingQueueBenchmarks' -f 1 -i 10 -wi 10  -gc true -tu s -bm thrpt -rf JSON -rff build/reports/jmh/lock-padded/results.json
