#![feature(iterator_step_by, fn_traits, unboxed_closures)]

extern crate datastructures;
extern crate criterion;

use std::marker::PhantomData;

use criterion::Criterion;

use datastructures::queues::sequential::array::{NonResizableArrayQueue, ResizableArrayQueue};
use datastructures::queues::Queue;

#[test]
fn non_resizable_array_queue_baseline() {
    let input = generate_input(10, 26);
    Criterion::default()
        .bench_function_over_inputs(
            "non-resizable-array-queue-baseline",
            |b, &&size| {
                let queue: NonResizableArrayQueue<(i64)> = NonResizableArrayQueue::new(size);
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
fn resizable_array_queue_baseline() {
    let input = generate_input(10, 26);
    Criterion::default()
        .bench_function_over_inputs(
            "resizable-array-queue-baseline",
            |b, &&size| {
                let queue: ResizableArrayQueue<(i64)> = ResizableArrayQueue::new(size);
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
fn generate_next_tuple_1(tuple: (i64)) -> (i64) {
    (tuple + 1)
}

fn generate_input(min_size: u32, max_size: u32) -> Vec<usize> {
    (min_size..max_size).map(|size| 2usize.pow(size))
        .collect::<Vec<usize>>()
}
