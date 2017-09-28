#!/usr/bin/env bash

cd rust-impl \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::rc_ref_cell_linked_queue | tee target/rc_ref_cell_linked_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::shared_linked_queue | tee target/shared_linked_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::non_resizable_array_queue | tee target/non_resizable_array_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::resizable_array_queue | tee target/resizable_array_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::std_resizable_array_queue | tee target/std_resizable_array_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::rc_ref_cell_linked_array_queue | tee target/rc_ref_cell_linked_array_queue \
&& RUST_TEST_THREADS=1 cargo bench benchmarks::shared_linked_array_queue | tee target/shared_linked_array_queue \
&& cd .. \
&& cd java-impl \
&& gradle clean jmhJar \
&& cd queues/sequential \
&& mkdir -p build/reports/jmh/ \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -gc true -tu us -bm avgt -rf JSON -rff build/reports/jmh/array-vs-linked-results.json LinkedVsArray \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -tu us -bm avgt -rf JSON -rff build/reports/jmh/predef-vs-unknown-results.json PredefinedSizeVsUnknown \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -tu us -bm avgt -rf JSON -rff build/reports/jmh/primitive-vs-boxed-results.json PrimitiveVsBoxed \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -tu us -bm avgt -rf JSON -rff build/reports/jmh/mask-vs-condition-results.json ConditionVsBitMask \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -tu us -bm avgt -rf JSON -rff build/reports/jmh/shrink-vs-not-shrink-results.json ShrinkVsNotShrink \
&& java -jar build/libs/sequential-1.0-jmh.jar -f 1 -wi 10 -i 10 -gc true -tu us -bm avgt -rf JSON -rff build/reports/jmh/nullify-vs-not-nullify-results.json NullifyVsNotNullify \
&& cd ../.. \
&& cd queues/blocking \
&& mkdir -p build/reports/jmh/ \
&& java -jar build/libs/blocking-1.0-jmh.jar -f 1 -i 10 -wi 10 -gc true -tu ms -bm thrpt -rf JSON -rff build/reports/jmh/lock-thrpt-results.json
