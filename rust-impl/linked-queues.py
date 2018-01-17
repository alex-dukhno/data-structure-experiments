#!/usr/bin/env python3

from subprocess import run

run(['cargo', 'bench', '--', 'rc_linked_queue_baseline', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_064_rc_linked_queue', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_128_rc_linked_queue', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'shared_linked_queue_baseline', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_064_shared_linked_queue', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_128_shared_linked_queue', '--test', '--nocapture'])
