#!/usr/bin/env bash

for i in 1 2 3 4 5
do
  cargo bench benchmarks::rc_ref_cell_linked_queue | tee benchmark-results/rc_ref_cell_linked_queue_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::padded_48_rc_ref_cell_linked_queue | tee benchmark-results/rc_ref_cell_linked_queue_padded_48_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::padded_64_rc_ref_cell_linked_queue | tee benchmark-results/rc_ref_cell_linked_queue_padded_64_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::shared_linked_queue | tee benchmark-results/shared_linked_queue_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::padded_32_shared_linked_queue | tee benchmark-results/shared_linked_queue_padded_32_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::padded_64_shared_linked_queue | tee benchmark-results/shared_linked_queue_padded_64_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_3m_padded_64_rc_ref_cell_linked_queue | tee benchmark-results/rc_ref_cell_linked_queue_padded_64_l3_cache_3m_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_8m_padded_64_rc_ref_cell_linked_queue | tee benchmark-results/rc_ref_cell_linked_queue_padded_64_l3_cache_8m_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_3m_shared_linked_queue | tee benchmark-results/shared_linked_queue_l3_cache_3m_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_8m_shared_linked_queue | tee benchmark-results/shared_linked_queue_l3_cache_8m_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_3m_padded_32_shared_linked_queue | tee benchmark-results/shared_linked_queue_padded_32_l3_cache_3m_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_8m_padded_32_shared_linked_queue | tee benchmark-results/shared_linked_queue_padded_32_l3_cache_8m_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_3m_padded_64_shared_linked_queue | tee benchmark-results/shared_linked_queue_padded_64_l3_cache_3m_${i}.txt
done

for i in 1 2 3 4 5
do
  cargo bench benchmarks::l3_cache_check_8m_padded_64_shared_linked_queue | tee benchmark-results/shared_linked_queue_padded_64_l3_cache_8m_${i}.txt
done

for i in 1 2 3 4 5
do
 cargo bench benchmarks::non_resizable_array_queue | tee benchmark-results/non_resizable_array_queue_${i}.txt
done

#TODO add bit mask queue and not shrinking queues (bit mask and conditional)
for i in 1 2 3 4 5
do
 cargo bench benchmarks::resizable_array_queue | tee benchmark-results/resizable_array_queue_${i}.txt
done

for i in 1 2 3 4 5
do
 cargo bench benchmarks::rc_ref_cell_linked_array_queue | tee benchmark-results/rc_ref_cell_linked_array_queue_${i}.txt
done

for i in 1 2 3 4 5
do
 cargo bench benchmarks::shared_linked_array_queue | tee benchmark-results/shared_linked_array_queue_${i}.txt
done
