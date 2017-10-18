use std::rc::Rc;
use std::cell::RefCell;
use std::ptr::Shared;

use super::Queue;

type RcRefCellLink<T> = Option<Rc<RefCell<T>>>;

struct RefCellNode {
    item: i32,
    next: RcRefCellLink<RefCellNode>
}

impl RefCellNode {
    fn new(item: i32) -> Rc<RefCell<RefCellNode>> {
        Rc::new(
            RefCell::new(RefCellNode {
                item: item,
                next: None
            })
        )
    }
}

#[derive(Default)]
pub struct RcRefCellLinkedQueue {
    head: RcRefCellLink<RefCellNode>,
    tail: RcRefCellLink<RefCellNode>
}

impl RcRefCellLinkedQueue {
    pub fn new() -> RcRefCellLinkedQueue {
        RcRefCellLinkedQueue {
            head: None,
            tail: None
        }
    }
}

impl Queue for RcRefCellLinkedQueue {
    fn deque(&mut self) -> Option<i32> {
        self.head.take().map(|old_head| {
            match old_head.borrow_mut().next.take() {
                Some(new_head) => self.head = Some(new_head),
                None => { self.tail.take(); }
            }
            Rc::try_unwrap(old_head).ok().unwrap().into_inner().item
        })
    }

    fn enqueue(&mut self, item: i32) {
        let node = RefCellNode::new(item);
        match self.tail.take() {
            Some(old_tail) => old_tail.borrow_mut().next = Some(node.clone()),
            None => self.head = Some(node.clone())
        }
        self.tail = Some(node)
    }
}

impl Drop for RcRefCellLinkedQueue {
    fn drop(&mut self) {
        while let Some(_) = self.deque() {}
    }
}

struct RefCellNodePadded64 {
    item: i32,
    next: RcRefCellLink<RefCellNodePadded64>,
    p0: i64,
    p1: i64,
    p2: i64
}

impl RefCellNodePadded64 {
    fn new(item: i32) -> Rc<RefCell<RefCellNodePadded64>> {
        Rc::new(
            RefCell::new(RefCellNodePadded64 {
                item: item,
                next: None,
                p0: 0,
                p1: 1,
                p2: 2
            })
        )
    }
}

#[derive(Default)]
pub struct RcRefCellLinkedQueuePadded64 {
    head: RcRefCellLink<RefCellNodePadded64>,
    tail: RcRefCellLink<RefCellNodePadded64>,
    p0: i64,
    p1: i64,
    p2: i64,
    p3: i64,
    p4: i64,
    p5: i64
}

impl RcRefCellLinkedQueuePadded64 {
    pub fn new() -> RcRefCellLinkedQueuePadded64 {
        RcRefCellLinkedQueuePadded64 {
            head: None,
            tail: None,
            p0: 0,
            p1: 1,
            p2: 2,
            p3: 3,
            p4: 4,
            p5: 5
        }
    }
}

impl Queue for RcRefCellLinkedQueuePadded64 {
    fn deque(&mut self) -> Option<i32> {
        self.head.take().map(|old_head| {
            match old_head.borrow_mut().next.take() {
                Some(new_head) => self.head = Some(new_head),
                None => { self.tail.take(); }
            }
            Rc::try_unwrap(old_head).ok().unwrap().into_inner().item
        })
    }

    fn enqueue(&mut self, item: i32) {
        let node = RefCellNodePadded64::new(item);
        match self.tail.take() {
            Some(old_tail) => old_tail.borrow_mut().next = Some(node.clone()),
            None => self.head = Some(node.clone())
        }
        self.tail = Some(node)
    }
}

impl Drop for RcRefCellLinkedQueuePadded64 {
    fn drop(&mut self) {
        while let Some(_) = self.deque() {}
    }
}

type SharedLink<T> = Option<Shared<T>>;

struct SharedNode {
    item: i32,
    next: SharedLink<SharedNode>
}

impl SharedNode {
    fn new(item: i32) -> SharedLink<SharedNode> {
        Shared::new(
            Box::into_raw(Box::new(SharedNode {
                item: item,
                next: None
            }))
        )
    }
}

pub struct SharedLinkedQueue {
    head: SharedLink<SharedNode>,
    tail: SharedLink<SharedNode>
}

impl SharedLinkedQueue {
    pub fn new() -> SharedLinkedQueue {
        SharedLinkedQueue {
            head: None,
            tail: None
        }
    }
}

impl Queue for SharedLinkedQueue {
    fn deque(&mut self) -> Option<i32> {
        self.head.take().map(|mut head| unsafe {
            match head.as_mut().next.take() {
                Some(new_head) => self.head = Some(new_head),
                None => self.tail = None
            }
            let item = head.as_ref().item;
            drop(Box::from_raw(head.as_ptr()));
            item
        })
    }

    fn enqueue(&mut self, item: i32) {
        let node = SharedNode::new(item);
        match self.tail.take() {
            Some(mut tail) => unsafe { tail.as_mut().next = node.clone(); },
            None => self.head = node.clone()
        }
        self.tail = node
    }
}

impl Drop for SharedLinkedQueue {
    fn drop(&mut self) {
        while let Some(_) = self.deque() {}
    }
}

struct SharedNodePadded32 {
    item: i32,
    next: SharedLink<SharedNodePadded32>,
    p0: i64,
    p1: i64
}

impl SharedNodePadded32 {
    fn new(item: i32) -> SharedLink<SharedNodePadded32> {
        Shared::new(
            Box::into_raw(Box::new(SharedNodePadded32 {
                item: item,
                next: None,
                p0: 0,
                p1: 1
            }))
        )
    }
}

pub struct SharedLinkedQueuePadded32 {
    head: SharedLink<SharedNodePadded32>,
    tail: SharedLink<SharedNodePadded32>,
    p0: i64,
    p1: i64
}

impl SharedLinkedQueuePadded32 {
    pub fn new() -> SharedLinkedQueuePadded32 {
        SharedLinkedQueuePadded32 {
            head: None,
            tail: None,
            p0: 0,
            p1: 1
        }
    }
}

impl Queue for SharedLinkedQueuePadded32 {
    fn deque(&mut self) -> Option<i32> {
        self.head.take().map(|mut head| unsafe {
            match head.as_mut().next.take() {
                Some(new_head) => self.head = Some(new_head),
                None => self.tail = None
            }
            let item = head.as_ref().item;
            drop(Box::from_raw(head.as_ptr()));
            item
        })
    }

    fn enqueue(&mut self, item: i32) {
        let node = SharedNodePadded32::new(item);
        match self.tail.take() {
            Some(mut tail) => unsafe { tail.as_mut().next = node.clone(); },
            None => self.head = node.clone()
        }
        self.tail = node
    }
}

impl Drop for SharedLinkedQueuePadded32 {
    fn drop(&mut self) {
        while let Some(_) = self.deque() {}
    }
}

struct SharedNodePadded64 {
    item: i32,
    next: SharedLink<SharedNodePadded64>,
    p0: i64,
    p1: i64,
    p2: i64,
    p3: i64,
    p4: i64,
    p5: i64
}

impl SharedNodePadded64 {
    fn new(item: i32) -> SharedLink<SharedNodePadded64> {
        Shared::new(
            Box::into_raw(Box::new(SharedNodePadded64 {
                item: item,
                next: None,
                p0: 0,
                p1: 1,
                p2: 2,
                p3: 3,
                p4: 4,
                p5: 5
            }))
        )
    }
}

pub struct SharedLinkedQueuePadded64 {
    head: SharedLink<SharedNodePadded64>,
    tail: SharedLink<SharedNodePadded64>,
    p0: i64,
    p1: i64,
    p2: i64,
    p3: i64,
    p4: i64,
    p5: i64
}

impl SharedLinkedQueuePadded64 {
    pub fn new() -> SharedLinkedQueuePadded64 {
        SharedLinkedQueuePadded64 {
            head: None,
            tail: None,
            p0: 0,
            p1: 1,
            p2: 2,
            p3: 3,
            p4: 4,
            p5: 5
        }
    }
}

impl Queue for SharedLinkedQueuePadded64 {
    fn deque(&mut self) -> Option<i32> {
        self.head.take().map(|mut head| unsafe {
            match head.as_mut().next.take() {
                Some(new_head) => self.head = Some(new_head),
                None => self.tail = None
            }
            let item = head.as_ref().item;
            drop(Box::from_raw(head.as_ptr()));
            item
        })
    }

    fn enqueue(&mut self, item: i32) {
        let node = SharedNodePadded64::new(item);
        match self.tail.take() {
            Some(mut tail) => unsafe { tail.as_mut().next = node.clone(); },
            None => self.head = node.clone()
        }
        self.tail = node
    }
}

impl Drop for SharedLinkedQueuePadded64 {
    fn drop(&mut self) {
        while let Some(_) = self.deque() {}
    }
}

#[cfg(test)]
mod tests {

    mod linked_ref_cell_queue {
        use super::super::RcRefCellLinkedQueue;
        use super::super::super::Queue;

        #[test]
        fn deque_item_from_empty_queue() {
            let mut queue = RcRefCellLinkedQueue::new();

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_one_item() {
            let mut queue = RcRefCellLinkedQueue::new();

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_three_items_one_by_one() {
            let mut queue = RcRefCellLinkedQueue::new();

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);

            queue.enqueue(20);

            assert_eq!(queue.deque(), Some(20));
            assert_eq!(queue.deque(), None);

            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_many_items_deque_many_items() {
            let mut queue = RcRefCellLinkedQueue::new();

            queue.enqueue(10);
            queue.enqueue(20);
            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), Some(20));
            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }
    }

    mod linked_shared_queue {
        use super::super::SharedLinkedQueue;
        use super::super::super::Queue;

        #[test]
        fn deque_item_from_empty_queue() {
            let mut queue = SharedLinkedQueue::new();

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_one_item() {
            let mut queue = SharedLinkedQueue::new();

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_three_items_one_by_one() {
            let mut queue = SharedLinkedQueue::new();

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);

            queue.enqueue(20);

            assert_eq!(queue.deque(), Some(20));
            assert_eq!(queue.deque(), None);

            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_many_items_deque_many_items() {
            let mut queue = SharedLinkedQueue::new();

            queue.enqueue(10);
            queue.enqueue(20);
            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), Some(20));
            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }
    }
}

#[cfg(test)]
mod benchmarks {
    extern crate test;

    use super::super::Queue;

    const K: usize = 1024;
    const _2_K: usize = 2 * K;
    const _4_K: usize = 4 * K;
    const _8_K: usize = 8 * K;
    const _16_K: usize = 16 * K;
    const _32_K: usize = 32 * K;
    const _64_K: usize = 64 * K;
    const _128_K: usize = 128 * K;
    const _256_K: usize = 256 * K;
    const _512_K: usize = 512 * K;
    const M: usize = K * K;
    const _2_M: usize = 2 * M;
    const _4_M: usize = 4 * M;
    const _8_M: usize = 8 * M;
    const _16_M: usize = 16 * M;
    const _32_M: usize = 32 * M;

    mod rc_ref_cell_linked_queue {
        use super::test::Bencher;
        use super::super::RcRefCellLinkedQueue;
        use super::*;

        #[bench]
        fn size_00001k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00002k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00004k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00008k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00016k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00032k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00064k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00128k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00256k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00512k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_01024k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_02048k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_04096k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_08192k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_16384k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_32768k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_M);
                deque_many(&mut queue)
            });
        }
    }

    mod padded_64_rc_ref_cell_linked_queue {
        use super::test::Bencher;
        use super::super::RcRefCellLinkedQueuePadded64;
        use super::*;

        #[bench]
        fn size_00001k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00002k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00004k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00008k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00016k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00032k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00064k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00128k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00256k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00512k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_01024k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_02048k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_04096k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_08192k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_16384k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_32768k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_M);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_3m_padded_64_rc_ref_cell_linked_queue {
        use super::test::Bencher;
        use super::super::RcRefCellLinkedQueuePadded64;
        use super::*;

        #[bench]
        fn size_000032k_16k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l01k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r01k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l02k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r02k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l04k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r04k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l08k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r08k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l16k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r16k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l24k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _16_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r24k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _16_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l32k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r32k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _32_K);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_8m_padded_64_rc_ref_cell_linked_queue {
        use super::test::Bencher;
        use super::super::RcRefCellLinkedQueuePadded64;
        use super::*;

        #[bench]
        fn size_0000128k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l01k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r01k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l02k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r02k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l04k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r04k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l08k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r08k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l16k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r16k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l32k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r32k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l48k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _32_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r48k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _32_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l64k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r64k(b: &mut Bencher) {
            let mut queue = RcRefCellLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K);
                deque_many(&mut queue)
            });
        }
    }

    mod shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueue;
        use super::*;

        #[bench]
        fn size_00001k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00002k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00004k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00008k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00016k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00032k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00064k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00256k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00512k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_01024k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_02048k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_04096k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_08192k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_16384k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_32768k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_M);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_3m_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueue;
        use super::*;

        #[bench]
        fn size_000128k_64k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_l004k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_r004k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_l008k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_r008k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_l016k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_r016k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_l032k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_r032k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_l064k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K - _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_r064k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K + _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_l096k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K - _64_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_r096k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K + _64_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_l128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K - _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000128k_64k_r128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K + _128_K);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_8m_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueue;
        use super::*;

        #[bench]
        fn size_0000512k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l004k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r004k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l008k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r008k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l016k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r016k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l032k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r032k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l064k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r064k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l192k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _128_K - _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r192k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _128_K + _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_l256k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K - _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000512k_r256k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueue::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K + _256_K);
                deque_many(&mut queue)
            });
        }
    }

    mod padded_32_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueuePadded32;
        use super::*;

        #[bench]
        fn size_00001k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00002k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00004k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00008k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00016k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00032k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00064k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00256k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00512k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_01024k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_02048k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_04096k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_08192k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_16384k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_32768k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_M);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_3m_padded_32_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueuePadded32;
        use super::*;

        #[bench]
        fn size_000064k_32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_l02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K - _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_r02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K + _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_l04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_r04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_l08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_r08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_l16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_r16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_l32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_r32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_l48k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K - _32_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_r48k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K + _32_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_l64k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K - _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000064k_32k_r64k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K + _32_K + _64_K);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_8m_padded_32_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueuePadded32;
        use super::*;

        #[bench]
        fn size_0000256k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l64k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r64k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l96k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _64_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r96k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _64_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_l128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K - _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000256k_r128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded32::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K + _128_K);
                deque_many(&mut queue)
            });
        }
    }

    mod padded_64_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueuePadded64;
        use super::*;

        #[bench]
        fn size_00001k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00002k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00004k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00008k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00016k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00032k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00064k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00256k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_00512k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_01024k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_02048k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _2_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_04096k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _4_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_08192k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _8_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_16384k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _16_M);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_32768k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_M);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_3m_padded_64_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueuePadded64;
        use super::*;

        #[bench]
        fn size_000032k_16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l01k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r01k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l24k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _16_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r24k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _16_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_l32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_000032k_16k_r32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _32_K + _16_K + _32_K);
                deque_many(&mut queue)
            });
        }
    }

    mod l3_cache_check_8m_padded_64_shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueuePadded64;
        use super::*;

        #[bench]
        fn size_0000128k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l01k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r01k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r02k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r04k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r08k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r16k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r32k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l48k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _32_K - _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r48k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _32_K + _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_l64k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K - _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0000128k_r64k(b: &mut Bencher) {
            let mut queue = SharedLinkedQueuePadded64::new();
            b.iter(|| {
                enqueue_many(&mut queue, _128_K + _64_K);
                deque_many(&mut queue)
            });
        }
    }

    fn enqueue_many<Q: Queue>(queue: &mut Q, size: usize) {
        for item in 0..size {
            queue.enqueue(item as i32);
        }
    }

    fn deque_many<Q: Queue>(queue: &mut Q) -> i32 {
        let mut sum = 0;
        while let Some(item) = queue.deque() {
            sum += item;
        }
        sum
    }
}
