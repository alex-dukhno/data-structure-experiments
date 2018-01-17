#![feature(iterator_step_by, fn_traits, unboxed_closures)]

extern crate datastructures;
extern crate criterion;

use std::marker::PhantomData;

use criterion::Criterion;

use datastructures::queues::sequential::linked::{RcRefCellLinkedQueue, SharedLinkedQueue};
use datastructures::queues::Queue;

#[test]
fn rc_linked_queue_baseline() {
    let input = generate_input_with_strategy(10, 26, power);
    Criterion::default()
        .bench_function_over_inputs(
            "rc-linear-enqueue-deque-baseline",
            |b, &&size| {
                let queue: RcRefCellLinkedQueue<(i64)> = RcRefCellLinkedQueue::new();
                let mut queue_consumer = QueueConsumer::new(
                    queue,
                    accumulate_tuple_1,
                    generate_next_tuple_1,
                );
                b.iter(|| {
                    queue_consumer.enqueue_many(size, (0));
                    queue_consumer.deque_all((0))
                });
            },
            &input,
        );
}

#[test]
fn padded_064_rc_linked_queue() {
    let input = generate_input_with_strategy(13, 17, next);
    Criterion::default()
        .bench_function_over_inputs(
            "rc-linear-enqueue-deque-64-bytes-node",
            |b, &&size| {
                let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                let mut queue_consumer = QueueConsumer::new(
                    queue,
                    accumulate_tuple_4,
                    generate_next_tuple_4,
                );
                b.iter(|| {
                    queue_consumer.enqueue_many(size, (0, 0, 0, 0));
                    queue_consumer.deque_all((0, 0, 0, 0))
                });
            },
            &input,
        );
}

#[test]
fn padded_128_rc_linked_queue() {
    let input = generate_input_with_strategy(13, 17, next);
    Criterion::default()
        .bench_function_over_inputs(
            "rc-linear-enqueue-deque-128-bytes-node",
            |b, &&size| {
                let queue: RcRefCellLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
                let mut queue_consumer = QueueConsumer::new(
                    queue,
                    accumulate_tuple_12,
                    generate_next_tuple_12,
                );
                b.iter(|| {
                    queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                });
            },
            &input,
        );
}

#[test]
fn shared_linked_queue_baseline() {
    let input = generate_input_with_strategy(10, 26, power);
    Criterion::default()
        .bench_function_over_inputs(
            "shared-linear-enqueue-dequeue-baseline",
            |b, &&size| {
                let queue: SharedLinkedQueue<(i64)> = SharedLinkedQueue::new();
                let mut queue_consumer = QueueConsumer::new(
                    queue,
                    accumulate_tuple_1,
                    generate_next_tuple_1,
                );
                b.iter(|| {
                    queue_consumer.enqueue_many(size, (0));
                    queue_consumer.deque_all((0))
                });
            },
            &input,
        );
}

#[test]
fn padded_064_shared_linked_queue() {
    let input = generate_input_with_strategy(13, 17, next);
    Criterion::default()
        .bench_function_over_inputs(
            "shared-linear-enqueue-dequeue-64-bytes-node",
            |b, &&size| {
                let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                let mut queue_consumer = QueueConsumer::new(
                    queue,
                    accumulate_tuple_7,
                    generate_next_tuple_7,
                );
                b.iter(|| {
                    queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0));
                    queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0))
                });
            },
            &input,
        );
}

#[test]
fn padded_128_shared_linked_queue() {
    let input = generate_input_with_strategy(13, 17, next);
    Criterion::default()
        .bench_function_over_inputs(
            "shared-linear-enqueue-dequeue-128-bytes-node",
            |b, &&size| {
                let queue: SharedLinkedQueue<(i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)> = SharedLinkedQueue::new();
                let mut queue_consumer = QueueConsumer::new(
                    queue,
                    accumulate_tuple_15,
                    generate_next_tuple_15,
                );
                b.iter(|| {
                    queue_consumer.enqueue_many(size, (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    queue_consumer.deque_all((0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                });
            },
            &input,
        );
}

struct QueueConsumer<E, Q, C, G> where E: Copy, Q: Queue<E>, C: Fn(E, E) -> E, G: Fn(E) -> E {
    _marker: PhantomData<E>,
    queue: Q,
    consumer: C,
    generator: G,
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
fn accumulate_tuple_7(items: (i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6)
}

#[inline]
fn accumulate_tuple_12(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10, acc_11) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10, item_11 + acc_11)
}

#[inline]
fn accumulate_tuple_15(items: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64), accs: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (item_0, item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11, item_12, item_13, item_14) = items;
    let (acc_0, acc_1, acc_2, acc_3, acc_4, acc_5, acc_6, acc_7, acc_8, acc_9, acc_10, acc_11, acc_12, acc_13, acc_14) = accs;
    (item_0 + acc_0, item_1 + acc_1, item_2 + acc_2, item_3 + acc_3, item_4 + acc_4, item_5 + acc_5, item_6 + acc_6, item_7 + acc_7, item_8 + acc_8, item_9 + acc_9, item_10 + acc_10, item_11 + acc_11, item_12 + acc_12, item_13 + acc_13, item_14 + acc_14)
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
fn generate_next_tuple_7(tuple: (i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7)
}

#[inline]
fn generate_next_tuple_12(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10, i_11) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11, i_11 + 12)
}

#[inline]
fn generate_next_tuple_15(tuple: (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64)) -> (i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64, i64) {
    let (i_0, i_1, i_2, i_3, i_4, i_5, i_6, i_7, i_8, i_9, i_10, i_11, i_12, i_13, i_14) = tuple;
    (i_0 + 1, i_1 + 2, i_2 + 3, i_3 + 4, i_4 + 5, i_5 + 6, i_6 + 7, i_7 + 8, i_8 + 9, i_9 + 10, i_10 + 11, i_11 + 12, i_12 + 13, i_13 + 14, i_14 + 15)
}

fn generate_input(min_size: u32, max_size: u32) -> Vec<usize> {
    (min_size..max_size).map(|size| 2usize.pow(size))
        .collect::<Vec<usize>>()
}

fn generate_input_with_strategy(min_size: u32, max_size: u32, strategy: fn(u32) -> Vec<usize>) -> Vec<usize> {
    (min_size..max_size)
        .flat_map(|size| strategy(size).into_iter())
        .collect::<Vec<usize>>()
}

fn power(num: u32) -> Vec<usize> {
    let r = 2usize.pow(num);
    (r..r+1).into_iter().collect::<Vec<usize>>()
}

fn next(num: u32) -> Vec<usize> {
    let r = 2usize.pow(num);
    let l = 2usize.pow(num+1);
    (r..).step_by(1024).take_while(|i| i <= &l).collect::<Vec<usize>>()
}
