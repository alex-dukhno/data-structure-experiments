#!/usr/bin/env bash

for i in 1 2 3 4 5
do
  cargo bench benchmarks::rc_ref_cell_linked_queue | tee target/rc_ref_cell_linked_queue_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::padded_64_rc_ref_cell_linked_queue | tee target/rc_ref_cell_linked_queue_padded_64_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::shared_linked_queue | tee target/shared_linked_queue_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::padded_32_shared_linked_queue | tee target/shared_linked_queue_padded_32_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::padded_64_shared_linked_queue | tee target/shared_linked_queue_padded_64_${i}.txt
done
#
#for i in 1 2 3 4 5
#do
#  RUST_TEST_THREADS=1 cargo bench benchmarks::non_resizable_array_queue | tee target/non_resizable_array_queue_${i}.txt
#done

#TODO add bit mask queue and not shrinking queues (bit mask and conditional)
#for i in 1 2 3 4 5
#do
#  RUST_TEST_THREADS=1 cargo bench benchmarks::resizable_array_queue | tee target/resizable_array_queue_${i}.txt
#done

#for i in 1 2 3 4 5
#do
#  RUST_TEST_THREADS=1 cargo bench benchmarks::rc_ref_cell_linked_array_queue | tee target/rc_ref_cell_linked_array_queue_${i}.txt
#done

#for i in 1 2 3 4 5
#do
# RUST_TEST_THREADS=1 cargo bench benchmarks::shared_linked_array_queue | tee target/shared_linked_array_queue_${i}.txt
#done
