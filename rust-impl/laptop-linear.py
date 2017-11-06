#!/usr/bin/env python3

from subprocess import run

run(['cargo', 'bench', '--', 'padded_048_rc_linked_queue_small', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_064_rc_linked_queue_small', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_128_rc_linked_queue_small', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_016_shared_linked_queue_small', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_048_shared_linked_queue_small', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_064_shared_linked_queue_small', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_128_shared_linked_queue_small', '--test', '--nocapture'])
