#![feature(iterator_step_by, fn_traits, unboxed_closures)]

extern crate datastructures;
extern crate criterion;

use std::marker::PhantomData;

use criterion::Criterion;

use datastructures::queues::sequential::linked::{RcRefCellLinkedQueue, SharedLinkedQueue};
use datastructures::queues::Queue;

const SMALL_STEP: usize = 64;
const MEDIUM_STEP: usize = 128;
const LARGE_STEP: usize = 256;

const KILO: usize = 1024;
const MEGA: usize = KILO * KILO;

#[test]
fn padded_048_rc_ref_cell_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 48, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-048-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_ref_cell_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 64, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-064-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_4,
                        generate_next_tuple_4
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_rc_ref_cell_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 80, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-080-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_6,
                        generate_next_tuple_6
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_rc_ref_cell_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 96, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-096-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_8,
                        generate_next_tuple_8
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_rc_ref_cell_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 112, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-112-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_10,
                        generate_next_tuple_10
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_ref_cell_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 128, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-128-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_12,
                        generate_next_tuple_12
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 16, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-016-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_1,
                        generate_next_tuple_1
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0));
                        queue_consumer.deque_all((0))
                    });
                },
                &input
            );
}

#[test]
fn padded_032_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 32, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-032-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 48, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-048-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_5,
                        generate_next_tuple_5
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 64, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-064-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_7,
                        generate_next_tuple_7
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 80, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-080-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_9,
                        generate_next_tuple_9
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 96, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-096-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_11,
                        generate_next_tuple_11
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 112, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-112-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_13,
                        generate_next_tuple_13
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_l1_32k_cache() {
    let input = generate_input(KILO, 128, SMALL_STEP, 4 * 32 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-128-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_15,
                        generate_next_tuple_15
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_rc_ref_cell_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 48, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-048-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_ref_cell_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 64, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-064-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_4,
                        generate_next_tuple_4
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_rc_ref_cell_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 80, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-080-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_6,
                        generate_next_tuple_6
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_rc_ref_cell_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 96, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-096-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_8,
                        generate_next_tuple_8
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_rc_ref_cell_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 112, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-112-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_10,
                        generate_next_tuple_10
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_ref_cell_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 128, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-128-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_12,
                        generate_next_tuple_12
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 16, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-016-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_1,
                        generate_next_tuple_1
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0));
                        queue_consumer.deque_all((0))
                    });
                },
                &input
            );
}

#[test]
fn padded_032_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 32, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-032-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 48, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-048-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_5,
                        generate_next_tuple_5
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 64, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-064-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_7,
                        generate_next_tuple_7
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 80, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-080-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_9,
                        generate_next_tuple_9
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 96, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-096-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_11,
                        generate_next_tuple_11
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 112, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-112-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_13,
                        generate_next_tuple_13
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_l2_256k_cache() {
    let input = generate_input(32 * KILO, 128, MEDIUM_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-128-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_15,
                        generate_next_tuple_15
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_rc_ref_cell_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 48, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-048-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_ref_cell_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 64, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-064-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_4,
                        generate_next_tuple_4
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_rc_ref_cell_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 80, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-080-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_6,
                        generate_next_tuple_6
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_rc_ref_cell_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 96, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-096-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_8,
                        generate_next_tuple_8
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_rc_ref_cell_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 112, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-112-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_10,
                        generate_next_tuple_10
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_ref_cell_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 128, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-128-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_12,
                        generate_next_tuple_12
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 16, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-016-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_1,
                        generate_next_tuple_1
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0));
                        queue_consumer.deque_all((0))
                    });
                },
                &input
            );
}

#[test]
fn padded_032_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 32, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-032-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 48, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-048-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_5,
                        generate_next_tuple_5
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 64, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-064-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_7,
                        generate_next_tuple_7
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 80, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-080-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_9,
                        generate_next_tuple_9
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 96, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-096-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_11,
                        generate_next_tuple_11
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 112, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-112-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_13,
                        generate_next_tuple_13
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_l3_3m_cache() {
    let input = generate_input(256 * KILO, 128, LARGE_STEP, 4 * 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-128-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_15,
                        generate_next_tuple_15
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_rc_ref_cell_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 48, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-048-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_ref_cell_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 64, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-064-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_4,
                        generate_next_tuple_4
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_rc_ref_cell_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 80, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-080-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_6,
                        generate_next_tuple_6
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_rc_ref_cell_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 96, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-096-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_8,
                        generate_next_tuple_8
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_rc_ref_cell_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 112, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-112-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_10,
                        generate_next_tuple_10
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_ref_cell_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 128, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-128-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_12,
                        generate_next_tuple_12
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 16, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-016-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_1,
                        generate_next_tuple_1
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0));
                        queue_consumer.deque_all((0))
                    });
                },
                &input
            );
}

#[test]
fn padded_032_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 32, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-032-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 48, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-048-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_5,
                        generate_next_tuple_5
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 64, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-064-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_7,
                        generate_next_tuple_7
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 80, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-080-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_9,
                        generate_next_tuple_9
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 96, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-096-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_11,
                        generate_next_tuple_11
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 112, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-112-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_13,
                        generate_next_tuple_13
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_l1_256k_cache() {
    let input = generate_input(KILO, 128, SMALL_STEP, 4 * 256 * KILO);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-128-bytes-node-l1-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_15,
                        generate_next_tuple_15
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_rc_ref_cell_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 48, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-048-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_ref_cell_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 64, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-064-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_4,
                        generate_next_tuple_4
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_rc_ref_cell_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 80, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-080-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_6,
                        generate_next_tuple_6
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_rc_ref_cell_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 96, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-096-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_8,
                        generate_next_tuple_8
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_rc_ref_cell_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 112, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-112-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_10,
                        generate_next_tuple_10
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_ref_cell_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 128, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-128-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_12,
                        generate_next_tuple_12
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 16, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-016-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_1,
                        generate_next_tuple_1
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0));
                        queue_consumer.deque_all((0))
                    });
                },
                &input
            );
}

#[test]
fn padded_032_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 32, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-032-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 48, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-048-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_5,
                        generate_next_tuple_5
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 64, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-064-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_7,
                        generate_next_tuple_7
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 80, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-080-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_9,
                        generate_next_tuple_9
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 96, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-096-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_11,
                        generate_next_tuple_11
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 112, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-112-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_13,
                        generate_next_tuple_13
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_l2_1m_cache() {
    let input = generate_input(32 * KILO, 128, MEDIUM_STEP, 4 * 1 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-128-bytes-node-l2-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_15,
                        generate_next_tuple_15
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_rc_ref_cell_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 48, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-048-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_ref_cell_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 64, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-064-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_4,
                        generate_next_tuple_4
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_rc_ref_cell_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 80, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-080-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_6,
                        generate_next_tuple_6
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_rc_ref_cell_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 96, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-096-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_8,
                        generate_next_tuple_8
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_rc_ref_cell_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 112, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-112-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_10,
                        generate_next_tuple_10
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_ref_cell_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 128, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-enqueue-dequeue-128-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_12,
                        generate_next_tuple_12
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 16, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-016-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_1,
                        generate_next_tuple_1
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0));
                        queue_consumer.deque_all((0))
                    });
                },
                &input
            );
}

#[test]
fn padded_032_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 32, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-032-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 48, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-048-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_5,
                        generate_next_tuple_5
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 64, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-064-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_7,
                        generate_next_tuple_7
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_080_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 80, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-080-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_9,
                        generate_next_tuple_9
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_096_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 96, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-096-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_11,
                        generate_next_tuple_11
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_112_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 112, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-112-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_13,
                        generate_next_tuple_13
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_l3_8m_cache() {
    let input = generate_input(256 * KILO, 128, LARGE_STEP, 4 * 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-enqueue-dequeue-128-bytes-node-l3-cache",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_15,
                        generate_next_tuple_15
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_rc_linked_queue_small() {
    let input = generate_input(48 * KILO, 48, KILO, 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-linear-enqueue-deque-48-bytes-node-small",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_linked_queue_small() {
    let input = generate_input(64 * KILO, 64, KILO, 4 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-linear-enqueue-deque-64-bytes-node-small",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_linked_queue_small() {
    let input = generate_input(128 * KILO, 128, KILO, 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-linear-enqueue-deque-128-bytes-node-small",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_small() {
    let input = generate_input(16 * KILO, 16, KILO, MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-16-bytes-node-small",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_small() {
    let input = generate_input(48 * KILO, 48, KILO, 3 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-48-bytes-node-small",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_small() {
    let input = generate_input(64 * KILO, 64, KILO, 4 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-64-bytes-node-small",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_small() {
    let input = generate_input(128 * KILO, 128, KILO, 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-128-bytes-node-small",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_rc_linked_queue_large() {
    let input = generate_input(48 * KILO, 48, KILO, 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-linear-enqueue-deque-48-bytes-node-large",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_rc_linked_queue_large() {
    let input = generate_input(64 * KILO, 64, KILO, 4 * 8 * MEGA / 3);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-linear-enqueue-deque-64-bytes-node-large",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_rc_linked_queue_large() {
    let input = generate_input(128 * KILO, 128, KILO, 8 * 8 * MEGA / 3);
    Criterion::default()
            .bench_function_over_inputs(
                "rc-linear-enqueue-deque-128-bytes-node-large",
                |b, &&size| {
                    let queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_2,
                        generate_next_tuple_2
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0));
                        queue_consumer.deque_all((0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_016_shared_linked_queue_large() {
    let input = generate_input(16 * KILO, 16, KILO, 8 * MEGA / 3);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-16-bytes-node-large",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_048_shared_linked_queue_large() {
    let input = generate_input(48 * KILO, 48, KILO, 8 * MEGA);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-48-bytes-node-large",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_064_shared_linked_queue_large() {
    let input = generate_input(64 * KILO, 64, KILO, 4 * 8 * MEGA / 3);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-64-bytes-node-large",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

#[test]
fn padded_128_shared_linked_queue_large() {
    let input = generate_input(128 * KILO, 128, KILO, 8 * 8 * MEGA / 3);
    Criterion::default()
            .bench_function_over_inputs(
                "shared-linear-enqueue-dequeue-128-bytes-node-large",
                |b, &&size| {
                    let queue: SharedLinkedQueue<(i64, i64, i64)> = SharedLinkedQueue::new();
                    let mut queue_consumer = QueueConsumer::new(
                        queue,
                        accumulate_tuple_3,
                        generate_next_tuple_3
                    );
                    b.iter(|| {
                        queue_consumer.enqueue_many(size, (0, 0, 0));
                        queue_consumer.deque_all((0, 0, 0))
                    });
                },
                &input
            );
}

struct QueueConsumer<E, Q, C, G> where E: Copy, Q: Queue<E>, C: Fn(E, E) -> E, G: Fn(E) -> E {
    _marker: PhantomData<E>,
    queue: Q,
    consumer: C,
    generator: G
}

impl<E: Copy, Q: Queue<E>, C: Fn(E, E) -> E, G: Fn(E) -> E> QueueConsumer<E, Q, C, G> {
    fn new(queue: Q, consumer: C, generator: G) -> Self {
        QueueConsumer { _marker: PhantomData, queue, consumer, generator }
    }

    fn enqueue_many(&mut self, size: usize, start_with: E) {
        let mut item = start_with;
        for _ in 0..size {
            self.queue.enqueue(item);
            item = self.generator.call((item, ));
        }
    }

    fn deque_all(&mut self, start_with: E) -> E {
        let mut consumed = start_with;
        while let Some(item) = self.queue.deque() {
            consumed = self.consumer.call((item, consumed));
        }
        consumed
    }
}

#[inline]
fn accumulate_tuple_1(items: (i64), accs: (i64)) -> (i64) {
    (items + accs)
}

#[inline]
fn accumulate_tuple_2(items: (i64, i64), accs: (i64, i64)) -> (i64, i64) {
    let (item_0, item_1) = items;
    let (acc_0, acc_1) = accs;
    (item_0 + acc_0, item_1 + acc_1)
}

#[inline]
fn accumulate_tuple_3(items: (i64, i64, i64), accs: (i64, i64, i64)) -> (i64, i64, i64) {
    let (item_0, item_1, item_2) = items;
    let (acc_0, acc_1, acc_2) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2)
}

#[inline]
fn accumulate_tuple_4(items: (i64, i64, i64, i64), accs: (i64, i64, i64, i64)) -> (i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3) = items;
    let (acc_0, acc_1, acc_2, acc_3) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3)
}

#[inline]
fn accumulate_tuple_5(items: (i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4)
}

#[inline]
fn accumulate_tuple_6(items: (i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5)
}

#[inline]
fn accumulate_tuple_7(items: (i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6)
}

#[inline]
fn accumulate_tuple_8(items: (i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7)
}

#[inline]
fn accumulate_tuple_9(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8)
}

#[inline]
fn accumulate_tuple_10(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9)
}

#[inline]
fn accumulate_tuple_11(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10)
}

#[inline]
fn accumulate_tuple_12(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10, acc_11) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10, item_11 + acc_11)
}

#[inline]
fn accumulate_tuple_13(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11, item_12) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10, acc_11, acc_12) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10, item_11 + acc_11, item_12 + acc_12)
}

#[inline]
fn accumulate_tuple_14(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11, item_12, item_13) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10, acc_11, acc_12, acc_13) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10, item_11 + acc_11, item_12 + acc_12, item_13 + acc_13)
}

#[inline]
fn accumulate_tuple_15(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11, item_12, item_13, item_14) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10, acc_11, acc_12, acc_13, acc_14) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10, item_11 + acc_11, item_12 + acc_12, item_13 + acc_13, item_14 + acc_14)
}

#[inline]
fn accumulate_tuple_16(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11, item_12, item_13, item_14, item_15) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10, acc_11, acc_12, acc_13, acc_14, acc_15) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10, item_11 + acc_11, item_12 + acc_12, item_13 + acc_13, item_14 + acc_14, item_15 + acc_15)
}

#[inline]
fn generate_next_tuple_1(tuple: (i64)) -> (i64) {
    (tuple + 1)
}

#[inline]
fn generate_next_tuple_2(tuple: (i64, i64)) -> (i64, i64) {
    let (i_0, i_1) = tuple;
    (i_0 + 1, i_1 + 2)
}

#[inline]
fn generate_next_tuple_3(tuple: (i64, i64, i64)) -> (i64, i64, i64) {
    let (i_0, i_1, i_2) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3)
}

#[inline]
fn generate_next_tuple_4(tuple: (i64, i64, i64, i64)) -> (i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4)
}

#[inline]
fn generate_next_tuple_5(tuple: (i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5)
}

#[inline]
fn generate_next_tuple_6(tuple: (i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6)
}

#[inline]
fn generate_next_tuple_7(tuple: (i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7)
}

#[inline]
fn generate_next_tuple_8(tuple: (i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8)
}

#[inline]
fn generate_next_tuple_9(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9)
}

#[inline]
fn generate_next_tuple_10(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10)
}

#[inline]
fn generate_next_tuple_11(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11)
}

#[inline]
fn generate_next_tuple_12(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10, i_11) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11, i_11 + 12)
}

#[inline]
fn generate_next_tuple_13(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10, i_11, i_12) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11, i_11 + 12, i_12 + 13)
}

#[inline]
fn generate_next_tuple_14(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10, i_11, i_12, i_13) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11, i_11 + 12, i_12 + 13, i_13 + 14)
}

#[inline]
fn generate_next_tuple_15(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10, i_11, i_12, i_13, i_14) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11, i_11 + 12, i_12 + 13, i_13 + 14, i_14 + 15)
}

#[inline]
fn generate_next_tuple_16(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10, i_11, i_12, i_13, i_14, i_15) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11, i_11 + 12, i_12 + 13, i_13 + 14, i_14 + 15, i_15 + 16)
}

fn generate_input(min_size: usize, node_size: usize, step_size: usize, limit: usize) -> Vec<usize> {
    (min_size..).step_by(node_size * step_size)
            .take_while(|&iter| iter <= limit)
            .map(|iter| iter / node_size)
            .collect::<Vec<usize>>()
}