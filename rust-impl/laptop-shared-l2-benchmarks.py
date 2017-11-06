#!/usr/bin/env python3

from subprocess import run

run(['cargo', 'bench', '--', 'padded_016_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_032_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_048_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_064_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_080_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_096_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_112_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_128_shared_linked_queue_l2_256k_cache', '--test', '--nocapture'])
