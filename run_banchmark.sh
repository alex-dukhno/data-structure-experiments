#!/usr/bin/env bash

cd java-impl \
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
