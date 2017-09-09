#!/usr/bin/env bash

mkdir -p benchmarks-result/Intel-i7-3770-3.40GHz-Ivy-Bridge \
&& cd rust-impl \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::rc_ref_cell_linked_queue | tee ../benchmarks-result/Intel-i7-3770-3.40GHz-Ivy-Bridge/rc_ref_cell_linked_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::shared_linked_queue | tee ../benchmarks-result/Intel-i7-3770-3.40GHz-Ivy-Bridge/shared_linked_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::non_resizable_array_queue | tee ../benchmarks-result/Intel-i7-3770-3.40GHz-Ivy-Bridge/non_resizable_array_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::resizable_array_queue | tee ../benchmarks-result/Intel-i7-3770-3.40GHz-Ivy-Bridge/resizable_array_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::rc_ref_cell_linked_array_queue | tee ../benchmarks-result/Intel-i7-3770-3.40GHz-Ivy-Bridge/rc_ref_cell_linked_array_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::shared_linked_array_queue | tee ../benchmarks-result/Intel-i7-3770-3.40GHz-Ivy-Bridge/shared_linked_array_queue \
&& cd .. \
&& cd java-impl \
&& gradle clean jmhJar \
&& cd queues/sequential \
&& mkdir -p build/reports/jmh/ \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -gc true -tu ns -bm avgt -rf JSON -rff build/reports/jmh/array-vs-linked-results.json LinkedVsArrayVsDirectVsHeap \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -tu ns -bm avgt -rf JSON -rff build/reports/jmh/primitive-vs-boxed-results.json PrimitiveVsBoxed \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -tu ns -bm avgt -rf JSON -rff build/reports/jmh/mask-vs-condition-results.json ConditionVsBitMask \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -tu ns -bm avgt -rf JSON -rff build/reports/jmh/linked-arrays-results.json LinkedArrays \
&& cd ../.. \
&& cd queues/blocking \
&& mkdir -p build/reports/jmh/ \
&& java -jar build/libs/blocking-1.0-jmh.jar -f 1 -i 10 -wi 10 -gc true -tu s -bm thrpt -rf JSON -rff build/reports/jmh/lock-thrpt-results.json
