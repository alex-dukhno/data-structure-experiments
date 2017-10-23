#!/usr/bin/env python3

from subprocess import run

# run(['cargo', 'bench', '--', 'padded_048_rc_ref_cell_linked_queue_l2_1m_cache', '--test', '--nocapture'])
# run(['cargo', 'bench', '--', 'padded_064_rc_ref_cell_linked_queue_l2_1m_cache', '--test', '--nocapture'])
# run(['cargo', 'bench', '--', 'padded_080_rc_ref_cell_linked_queue_l2_1m_cache', '--test', '--nocapture'])
# run(['cargo', 'bench', '--', 'padded_096_rc_ref_cell_linked_queue_l2_1m_cache', '--test', '--nocapture'])
# run(['cargo', 'bench', '--', 'padded_112_rc_ref_cell_linked_queue_l2_1m_cache', '--test', '--nocapture'])
run(['cargo', 'bench', '--', 'padded_128_rc_ref_cell_linked_queue_l2_1m_cache', '--test', '--nocapture'])
