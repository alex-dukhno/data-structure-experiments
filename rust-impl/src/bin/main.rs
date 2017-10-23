#![feature(iterator_step_by)]

extern crate datastructures;

use datastructures::queues::sequential::linked::{RcRefCellLinkedQueue, SharedLinkedQueue};
use datastructures::queues::Queue;

pub fn main() {
    let mut queue: RcRefCellLinkedQueue<i64> = RcRefCellLinkedQueue::new();
    for i in 0..10 {
        queue.enqueue(i);
    }
    println!("{:?}", queue);
    let mut queue: RcRefCellLinkedQueue<(i64, i64)> = RcRefCellLinkedQueue::new();
    for i in 0..10 {
        queue.enqueue((i, i));
    }
    println!("{:?}", queue);
    let mut queue: RcRefCellLinkedQueue<(i64, i64, i64, i64)> = RcRefCellLinkedQueue::new();
    for i in 0..10 {
        queue.enqueue((i, i, i, i));
    }
    println!("{:?}", queue);
    let mut queue: SharedLinkedQueue<i64> = SharedLinkedQueue::new();
    println!("{:?}", queue);
    let mut queue: SharedLinkedQueue<(i64, i64)> = SharedLinkedQueue::new();
    println!("{:?}", queue);
}
