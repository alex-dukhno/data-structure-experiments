extern crate alloc;

use self::alloc::raw_vec::RawVec;

use std::ptr::{self, Shared};
use std::rc::Rc;
use std::cell::RefCell;

const MIN_CAPACITY: usize = 16;
const MAX_CAPACITY: usize = usize::max_value();

use super::Queue;

type RcRefCellLink<T> = Option<Rc<RefCell<T>>>;

pub struct LinkedArrayQueue {
    segment_capacity: usize,
    head: RcRefCellLink<Segment>,
    tail: RcRefCellLink<Segment>
}

impl LinkedArrayQueue {
    pub fn new(segment_capacity: usize) -> LinkedArrayQueue {
        LinkedArrayQueue {
            segment_capacity: segment_capacity,
            head: None,
            tail: None
        }
    }
}

impl Queue for LinkedArrayQueue {
    fn deque(&mut self) -> Option<i32> {
        self.head.take().and_then(
            |head| {
                if head.borrow().is_empty() && self.tail.as_ref().map_or(true, |tail| tail == &head) {
                    self.head = Some(head);
                    None
                } else if head.borrow().is_exhausted() {
                    head.borrow_mut().next.take().map(|next| {
                        self.head = Some(next.clone());
                        next.borrow_mut().read_first()
                    })
                } else {
                    self.head = Some(head.clone());
                    Some(head.borrow_mut().read_first())
                }
            }
        )
    }

    fn enqueue(&mut self, item: i32) {
        match self.tail.take() {
            Some(tail) => {
                if tail.borrow().is_full() {
                    let segment = Segment::new(self.segment_capacity);
                    tail.borrow_mut().next = Some(segment.clone());
                    self.tail = Some(segment);
                } else {
                    self.tail = Some(tail);
                }
            }
            None => {
                let segment = Segment::new(self.segment_capacity);
                self.head = Some(segment.clone());
                self.tail = Some(segment);
            }
        }
        self.tail.as_ref().map(|tail| tail.borrow_mut().write_last(item));
    }
}

struct Segment {
    items: RawVec<i32>,
    next: RcRefCellLink<Segment>,
    first: usize,
    last: usize
}

impl PartialEq for Segment {
    fn eq(&self, other: &Segment) -> bool {
        self.items.ptr() == other.items.ptr()
    }
}

impl Segment {
    fn new(capacity: usize) -> Rc<RefCell<Segment>> {
        Rc::new(
            RefCell::new(
                Segment {
                    items: RawVec::with_capacity(capacity),
                    next: None,
                    first: 0,
                    last: 0
                }
            )
        )
    }

    fn read_first(&mut self) -> i32 {
        self.first += 1;
        unsafe {
            let index_to_read = self.items.ptr().offset(self.first as isize);
            ptr::read(index_to_read)
        }
    }

    fn write_last(&mut self, item: i32) {
        self.last += 1;
        unsafe {
            let index_to_write = self.items.ptr().offset(self.last as isize);
            ptr::write(index_to_write, item);
        }
    }

    fn is_empty(&self) -> bool {
        self.first == self.last
    }

    fn is_exhausted(&self) -> bool {
        self.first == self.items.cap() - 1
    }

    fn is_full(&self) -> bool {
        self.last == self.items.cap() - 1
    }
}

pub struct ArrayQueue {
    head: usize,
    tail: usize,
    mask: usize,
    data: RawVec<i32>,
    capacity: usize
}

impl ArrayQueue {
    pub fn new(capacity: usize) -> ArrayQueue {
        ArrayQueue {
            head: 0,
            tail: capacity - 1,
            mask: capacity - 1,
            data: RawVec::with_capacity(capacity),
            capacity: capacity
        }
    }

    fn size(&self) -> usize {
        self.mask - (self.tail.wrapping_sub(self.head) & self.mask)
    }

    fn quarter(&self) -> usize {
        self.capacity >> 2
    }

    fn filled_by_quarter(&self) -> bool {
        self.size() == self.quarter()
    }

    fn resize(&mut self) {
        let new_capacity = self.capacity << 1;
        self.data = self.copy_items(new_capacity);
        self.update_cursor(new_capacity);
    }

    fn copy_items(&mut self, new_capacity: usize) -> RawVec<i32> {
        let new_data = RawVec::with_capacity(new_capacity);
        let new_mask = new_capacity - 1;
        let mut new_item_index = (new_mask.wrapping_sub(self.mask).wrapping_add(self.tail) + 1) & new_mask;
        let mut item_index = (self.tail + 1) & self.mask;
        loop {
            if item_index == self.head {
                break;
            }
            unsafe {
                let index_to_write = new_data.ptr().offset(new_item_index as isize);
                let index_to_read = self.data.ptr().offset(item_index as isize);
                ptr::write(index_to_write, ptr::read(index_to_read));
            }
            item_index = (item_index + 1) & self.mask;
            new_item_index = (new_item_index + 1) & new_mask;
        }
        new_data
    }

    fn update_cursor(&mut self, new_capacity: usize) {
        let old_mask = self.mask;
        self.mask = new_capacity - 1;
        self.tail = (self.mask.wrapping_sub(old_mask).wrapping_add(self.tail)) & self.mask;
        self.head &= new_capacity - 1;
        self.capacity = new_capacity;
    }
}

impl Queue for ArrayQueue {
    fn deque(&mut self) -> Option<i32> {
        if (self.tail.wrapping_sub(self.head) & self.mask) == self.mask {
            None
        } else {
            self.tail = (self.tail + 1) & self.mask;
            let item = unsafe {
                let item_index = self.data.ptr().offset(self.tail as isize);
                ptr::read(item_index)
            };
            if self.filled_by_quarter() && self.capacity > MIN_CAPACITY {
                let new_capacity = self.capacity >> 1;
                self.data = self.copy_items(new_capacity);
                self.update_cursor(new_capacity);
            }
            Some(item)
        }
    }

    fn enqueue(&mut self, item: i32) {
        if self.head == self.tail && self.capacity < MAX_CAPACITY {
            self.resize();
        }
        let index = self.head;
        self.head = (self.head + 1) & self.mask;
        unsafe {
            let index_to_write = self.data.ptr().offset(index as isize);
            ptr::write(index_to_write, item)
        }
    }
}

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
        while let Some(_) = self.deque() {
        }
    }
}

type SharedLink = Option<Shared<SharedNode>>;

struct SharedNode {
    item: i32,
    next: SharedLink
}

impl SharedNode {
    fn new(item: i32) -> SharedLink {
        Shared::new(
            Box::into_raw(Box::new(SharedNode {
                item: item,
                next: None
            }))
        )
    }
}

pub struct SharedLinkedQueue {
    head: SharedLink,
    tail: SharedLink
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
            head.as_ref().item
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
        while let Some(_) = self.deque() {
        }
    }
}

#[cfg(test)]
mod tests {
    mod linked_shared_queue {
        use super::super::*;

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

    mod linked_array_queue {
        use super::super::*;

        #[test]
        fn deque_from_empty_queue() {
            let mut queue = LinkedArrayQueue::new(16);

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_item() {
            let mut queue = LinkedArrayQueue::new(16);

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_item_many_times() {
            let mut queue = LinkedArrayQueue::new(16);
            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));

            queue.enqueue(20);

            assert_eq!(queue.deque(), Some(20));

            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_many_items_deque_all() {
            let mut queue = LinkedArrayQueue::new(16);
            queue.enqueue(10);
            queue.enqueue(20);
            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), Some(20));
            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_items_more_than_segment_capacity() {
            let mut queue = LinkedArrayQueue::new(16);
            for i in 0..20 {
                queue.enqueue(i);
            }

            for i in 0..20 {
                assert_eq!(queue.deque(), Some(i));
            }
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_more_than_one_segment_then_deque() {
            let mut queue = LinkedArrayQueue::new(16);

            for i in 0..40 {
                queue.enqueue(i);
            }

            for i in 0..40 {
                assert_eq!(queue.deque(), Some(i));
            }
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_more_than_segment_capacity() {
            let mut queue = LinkedArrayQueue::new(16);

            for i in 0..40 {
                queue.enqueue(i);
                assert_eq!(queue.deque(), Some(i));
                assert_eq!(queue.deque(), None);
            }
        }
    }

    mod array_queue {
        use super::super::*;

        #[test]
        fn deque_from_empty_queue() {
            let mut queue = ArrayQueue::new(16);

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_one_item() {
            let mut queue = ArrayQueue::new(16);

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_three_items_one_by_one() {
            let mut queue = ArrayQueue::new(16);

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
            let mut queue = ArrayQueue::new(16);

            queue.enqueue(10);
            queue.enqueue(20);
            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), Some(20));
            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_more_than_capacity() {
            let mut queue = ArrayQueue::new(16);

            for i in 0..20 {
                queue.enqueue(i);
            }

            for i in 0..20 {
                assert_eq!(queue.deque(), Some(i));
            }
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_to_double_resize_deque_to_shrink() {
            let mut queue = ArrayQueue::new(16);

            for i in 0..100 {
                queue.enqueue(i);
            }

            for i in 0..100 {
                assert_eq!(queue.deque(), Some(i));
            }
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_many_time_more_than_capacity() {
            let mut queue = ArrayQueue::new(16);
            for i in 0..40 {
                queue.enqueue(i);
            }

            for i in 0..20 {
                assert_eq!(queue.deque(), Some(i));
            }

            for i in 40..80 {
                queue.enqueue(i);
            }

            for i in 20..40 {
                assert_eq!(queue.deque(), Some(i));
            }

            for i in 80..120 {
                queue.enqueue(i);
            }

            for i in 40..60 {
                assert_eq!(queue.deque(), Some(i));
            }
        }
    }

    mod linked_ref_cell_queue {
        use super::super::*;

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
}

#[cfg(test)]
mod benchmarks {
    extern crate test;

    use super::{SharedLinkedQueue, RcRefCellLinkedQueue, Queue};
    use self::test::Bencher;

    #[bench]
    fn bench_rc_ref_cell_linked_queue(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = RcRefCellLinkedQueue::new();
            for i in 0..1048576 {
                queue.enqueue(i);
            }
            queue
        });
    }

    #[bench]
    fn bench_shared_linked_queue(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = SharedLinkedQueue::new();
            for i in 0..1048576 {
                queue.enqueue(i);
            }
            queue
        });
    }
}
