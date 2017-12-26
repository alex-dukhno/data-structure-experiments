#!/usr/bin/env bash

mvn clean install \
&& cd queues/sequential-benchmarks/ \
&& mkdir -p target/reports/jmh/ \
&& java -jar target/sequential-benchmarks.jar -f 2 -wi 20 -i 20 -tu us -bm avgt -rf JSON -rff target/reports/jmh/array-vs-linked-results.json n01 \
&& java -jar target/sequential-benchmarks.jar -f 2 -wi 20 -i 20 -gc true -tu us -bm avgt -rf JSON -rff target/reports/jmh/condition-vs-bit-mask-results.json n02 \
&& java -jar target/sequential-benchmarks.jar -f 2 -wi 20 -i 20 -tu us -bm avgt -rf JSON -rff target/reports/jmh/shrink-vs-not-shrink-results.json n03 \
&& java -jar target/sequential-benchmarks.jar -f 2 -wi 20 -i 20 -tu us -bm avgt -rf JSON -rff target/reports/jmh/pooled-linked-results.json n04 \
&& java -jar target/sequential-benchmarks.jar -f 2 -wi 20 -i 20 -tu us -bm avgt -rf JSON -rff target/reports/jmh/primitive-vs-boxed-results.json n05
