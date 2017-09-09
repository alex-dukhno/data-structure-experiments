extern crate alloc;

use self::alloc::raw_vec::RawVec;

use std::ptr::{self, Shared};
use std::rc::Rc;
use std::cell::RefCell;

const MIN_CAPACITY: usize = 16;
const MAX_CAPACITY: usize = usize::max_value();

use super::Queue;

type RcRefCellLink<T> = Option<Rc<RefCell<T>>>;

pub struct RcRefCellLinkLinkedArrayQueue {
    segment_capacity: usize,
    head: RcRefCellLink<RcRefCellSegment>,
    tail: RcRefCellLink<RcRefCellSegment>
}

impl RcRefCellLinkLinkedArrayQueue {
    pub fn new(segment_capacity: usize) -> RcRefCellLinkLinkedArrayQueue {
        RcRefCellLinkLinkedArrayQueue {
            segment_capacity: segment_capacity,
            head: None,
            tail: None
        }
    }
}

impl Queue for RcRefCellLinkLinkedArrayQueue {
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
                    let segment = RcRefCellSegment::new(self.segment_capacity);
                    tail.borrow_mut().next = Some(segment.clone());
                    self.tail = Some(segment);
                } else {
                    self.tail = Some(tail);
                }
            }
            None => {
                let segment = RcRefCellSegment::new(self.segment_capacity);
                self.head = Some(segment.clone());
                self.tail = Some(segment);
            }
        }
        self.tail.as_ref().map(|tail| tail.borrow_mut().write_last(item));
    }
}

struct RcRefCellSegment {
    items: RawVec<i32>,
    next: RcRefCellLink<RcRefCellSegment>,
    first: usize,
    last: usize
}

impl PartialEq for RcRefCellSegment {
    fn eq(&self, other: &RcRefCellSegment) -> bool {
        self.items.ptr() == other.items.ptr()
    }
}

impl RcRefCellSegment {
    fn new(capacity: usize) -> Rc<RefCell<RcRefCellSegment>> {
        Rc::new(
            RefCell::new(
                RcRefCellSegment {
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

pub struct SharedLinkLinkedArrayQueue {
    segment_capacity: usize,
    head: SharedLink<SharedSegment>,
    tail: SharedLink<SharedSegment>
}

impl SharedLinkLinkedArrayQueue {
    pub fn new(segment_capacity: usize) -> SharedLinkLinkedArrayQueue {
        SharedLinkLinkedArrayQueue {
            segment_capacity: segment_capacity,
            head: None,
            tail: None
        }
    }
}

impl Queue for SharedLinkLinkedArrayQueue {
    fn deque(&mut self) -> Option<i32> {
        self.head.take().and_then(
            |mut head| unsafe {
                if head.as_ref().is_empty() && self.tail.as_ref().map_or(true, |tail| tail.as_ptr() == head.as_ptr()) {
                    self.head = Some(head);
                    None
                } else if head.as_ref().is_exhausted() {
                    let ret = head.as_mut().next.take().map(|mut next| {
                        self.head = Some(next.clone());
                        next.as_mut().read_first()
                    });
                    drop(Box::from_raw(head.as_ptr()));
                    ret
                } else {
                    self.head = Some(head.clone());
                    Some(head.as_mut().read_first())
                }
            }
        )
    }

    fn enqueue(&mut self, item: i32) {
        unsafe {
            match self.tail.take() {
                Some(mut tail) => {
                    if tail.as_ref().is_full() {
                        let segment = SharedSegment::new(self.segment_capacity);
                        tail.as_mut().next = segment.clone();
                        self.tail = segment;
                    } else {
                        self.tail = Some(tail);
                    }
                }
                None => {
                    let segment = SharedSegment::new(self.segment_capacity);
                    self.head = segment.clone();
                    self.tail = segment;
                }
            }
            self.tail.as_mut().map(|tail| tail.as_mut().write_last(item));
        }
    }
}

impl Drop for SharedLinkLinkedArrayQueue {
    fn drop(&mut self) {
        while let Some(_) = self.deque() {}
    }
}

struct SharedSegment {
    items: RawVec<i32>,
    next: SharedLink<SharedSegment>,
    first: usize,
    last: usize
}

impl PartialEq for SharedSegment {
    fn eq(&self, other: &SharedSegment) -> bool {
        self.items.ptr() == other.items.ptr()
    }
}

impl SharedSegment {
    fn new(capacity: usize) -> SharedLink<SharedSegment> {
        Shared::new(
            Box::into_raw(Box::new(
                SharedSegment {
                    items: RawVec::with_capacity(capacity),
                    next: None,
                    first: 0,
                    last: 0
                }
            ))
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

pub struct ResizableArrayQueue {
    head: usize,
    tail: usize,
    size: usize,
    data: RawVec<i32>
}

impl ResizableArrayQueue {
    pub fn new(capacity: usize) -> ResizableArrayQueue {
        ResizableArrayQueue {
            head: 0,
            tail: 0,
            size: 0,
            data: RawVec::with_capacity(capacity),
        }
    }

    fn resize(&mut self, new_capacity: usize) {
        let new_data = RawVec::with_capacity(new_capacity);
        let mask = self.data.cap() - 1;
        for i in 0..self.size {
            unsafe {
                let to_write = new_data.ptr().offset(i as isize);
                let to_read = self.data.ptr().offset(((self.head + i) & mask) as isize);
                let item = ptr::read(to_read);
                ptr::write(to_write, item);
            }
        }
        self.data = new_data;
        self.head = 0;
        self.tail = self.size;
    }

    fn is_empty(&self) -> bool {
        self.size == 0
    }
}

impl Queue for ResizableArrayQueue {
    fn deque(&mut self) -> Option<i32> {
        if self.is_empty() {
            None
        } else {
            let item = unsafe {
                let to_read = self.data.ptr().offset(self.head as isize);
                ptr::read(to_read)
            };
            self.size -= 1;
            self.head = (self.head + 1) & (self.data.cap() - 1);
            let capacity = self.data.cap();
            if self.size > MIN_CAPACITY && self.size == (capacity >> 2) {
                self.resize(capacity >> 1);
            }
            Some(item)
        }
    }

    fn enqueue(&mut self, item: i32) {
        let capacity = self.data.cap();
        if self.size == capacity && capacity < MAX_CAPACITY {
            self.resize(capacity << 1);
        }
        unsafe {
            let to_write = self.data.ptr().offset(self.tail as isize);
            ptr::write(to_write, item);
        }
        self.tail = (self.tail + 1) & (self.data.cap() - 1);
        self.size += 1;
    }
}

pub struct NonResizableArrayQueue {
    head: usize,
    tail: usize,
    size: usize,
    data: RawVec<i32>
}

impl NonResizableArrayQueue {
    pub fn new(capacity: usize) -> NonResizableArrayQueue {
        NonResizableArrayQueue {
            head: 0,
            tail: 0,
            size: 0,
            data: RawVec::with_capacity(capacity),
        }
    }

    fn is_empty(&self) -> bool {
        self.size == 0
    }
}

impl Queue for NonResizableArrayQueue {
    fn deque(&mut self) -> Option<i32> {
        if self.is_empty() {
            None
        } else {
            let item = unsafe {
                let to_read = self.data.ptr().offset(self.head as isize);
                ptr::read(to_read)
            };
            self.size -= 1;
            self.head = (self.head + 1) & (self.data.cap() - 1);
            Some(item)
        }
    }

    fn enqueue(&mut self, item: i32) {
        unsafe {
            let to_write = self.data.ptr().offset(self.tail as isize);
            ptr::write(to_write, item);
        }
        self.tail = (self.tail + 1) & (self.data.cap() - 1);
        self.size += 1;
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

    mod rc_ref_cell_link_linked_array_queue {
        use super::super::*;

        #[test]
        fn deque_from_empty_queue() {
            let mut queue = RcRefCellLinkLinkedArrayQueue::new(16);

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_item() {
            let mut queue = RcRefCellLinkLinkedArrayQueue::new(16);

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_item_many_times() {
            let mut queue = RcRefCellLinkLinkedArrayQueue::new(16);
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
            let mut queue = RcRefCellLinkLinkedArrayQueue::new(16);
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
            let mut queue = RcRefCellLinkLinkedArrayQueue::new(16);
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
            let mut queue = RcRefCellLinkLinkedArrayQueue::new(16);

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
            let mut queue = RcRefCellLinkLinkedArrayQueue::new(16);

            for i in 0..40 {
                queue.enqueue(i);
                assert_eq!(queue.deque(), Some(i));
                assert_eq!(queue.deque(), None);
            }
        }
    }

    mod shared_link_linked_array_queue {
        use super::super::*;

        #[test]
        fn deque_from_empty_queue() {
            let mut queue = SharedLinkLinkedArrayQueue::new(16);

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_item() {
            let mut queue = SharedLinkLinkedArrayQueue::new(16);

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_deque_item_many_times() {
            let mut queue = SharedLinkLinkedArrayQueue::new(16);
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
            let mut queue = SharedLinkLinkedArrayQueue::new(16);
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
            let mut queue = SharedLinkLinkedArrayQueue::new(16);
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
            let mut queue = SharedLinkLinkedArrayQueue::new(16);

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
            let mut queue = SharedLinkLinkedArrayQueue::new(16);

            for i in 0..40 {
                queue.enqueue(i);
                assert_eq!(queue.deque(), Some(i));
                assert_eq!(queue.deque(), None);
            }
        }
    }

    mod non_resizable_array_queue {
        use super::super::*;

        #[test]
        fn deque_from_empty_queue() {
            let mut queue = NonResizableArrayQueue::new(16);

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_one_item() {
            let mut queue = NonResizableArrayQueue::new(16);

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_three_items_one_by_one() {
            let mut queue = NonResizableArrayQueue::new(16);

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
            let mut queue = NonResizableArrayQueue::new(16);

            queue.enqueue(10);
            queue.enqueue(20);
            queue.enqueue(30);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), Some(20));
            assert_eq!(queue.deque(), Some(30));
            assert_eq!(queue.deque(), None);
        }
    }

    mod resizable_array_queue {
        use super::super::*;

        #[test]
        fn deque_from_empty_queue() {
            let mut queue = ResizableArrayQueue::new(16);

            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_one_item() {
            let mut queue = ResizableArrayQueue::new(16);

            queue.enqueue(10);

            assert_eq!(queue.deque(), Some(10));
            assert_eq!(queue.deque(), None);
        }

        #[test]
        fn enqueue_three_items_one_by_one() {
            let mut queue = ResizableArrayQueue::new(16);

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
            let mut queue = ResizableArrayQueue::new(16);

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
            let mut queue = ResizableArrayQueue::new(16);

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
            let mut queue = ResizableArrayQueue::new(16);

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
            let mut queue = ResizableArrayQueue::new(16);
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

    use super::Queue;

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

    mod rc_ref_cell_linked_queue {
        use super::test::Bencher;
        use super::super::RcRefCellLinkedQueue;
        use super::*;

        #[bench]
        fn size_0001k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0002k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0004k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0008k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0016k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0032k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0064k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0128k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0256k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0512k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_1024k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkedQueue::new();
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            });
        }
    }

    mod shared_linked_queue {
        use super::test::Bencher;
        use super::super::SharedLinkedQueue;
        use super::*;

        #[bench]
        fn size_0001k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0002k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0004k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0008k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0016k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0032k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0064k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0128k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0256k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_0512k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            });
        }

        #[bench]
        fn size_1024k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkedQueue::new();
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            });
        }
    }

    mod std_resizable_array_queue {
        use super::test::Bencher;
        use super::*;
        use std::collections::VecDeque;

        #[bench]
        fn size_0001k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(K);
                enqueue_many_std(&mut queue, K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0002k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_2_K);
                enqueue_many_std(&mut queue, _2_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0004k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_4_K);
                enqueue_many_std(&mut queue, _4_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0008k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_8_K);
                enqueue_many_std(&mut queue, _8_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0016k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_16_K);
                enqueue_many_std(&mut queue, _16_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0032k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_32_K);
                enqueue_many_std(&mut queue, _32_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0064k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_64_K);
                enqueue_many_std(&mut queue, _64_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0128k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_128_K);
                enqueue_many_std(&mut queue, _128_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0256k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_256_K);
                enqueue_many_std(&mut queue, _256_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_0512k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(_512_K);
                enqueue_many_std(&mut queue, _512_K);
                deque_many_std(&mut queue)
            })
        }

        #[bench]
        fn size_1024k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = VecDeque::with_capacity(M);
                enqueue_many_std(&mut queue, M);
                deque_many_std(&mut queue)
            })
        }

        fn enqueue_many_std(queue: &mut VecDeque<i32>, size: usize) {
            for item in 0..size {
                queue.push_back(item as i32);
            }
        }

        fn deque_many_std(queue: &mut VecDeque<i32>) -> i32 {
            let mut sum = 0;
            while let Some(item) = queue.pop_front() {
                sum += item;
            }
            sum
        }
    }

    mod resizable_array_queue {
    use super::test::Bencher;
    use super::super::ResizableArrayQueue;
    use super::*;

    #[bench]
    fn size_0001k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(K);
            enqueue_many(&mut queue, K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0002k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_2_K);
            enqueue_many(&mut queue, _2_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0004k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_4_K);
            enqueue_many(&mut queue, _4_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0008k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_8_K);
            enqueue_many(&mut queue, _8_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0016k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_16_K);
            enqueue_many(&mut queue, _16_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0032k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_32_K);
            enqueue_many(&mut queue, _32_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0064k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_64_K);
            enqueue_many(&mut queue, _64_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0128k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_128_K);
            enqueue_many(&mut queue, _128_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0256k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_256_K);
            enqueue_many(&mut queue, _256_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_0512k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(_512_K);
            enqueue_many(&mut queue, _512_K);
            deque_many(&mut queue)
        })
    }

    #[bench]
    fn size_1024k(b: &mut Bencher) {
        b.iter(|| {
            let mut queue = ResizableArrayQueue::new(M);
            enqueue_many(&mut queue, M);
            deque_many(&mut queue)
        })
    }
}

    mod non_resizable_array_queue {
        use super::test::Bencher;
        use super::super::NonResizableArrayQueue;
        use super::*;

        #[bench]
        fn size_0001k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(K);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_2_K);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_4_K);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_8_K);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_16_K);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_32_K);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_64_K);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_128_K);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_256_K);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(_512_K);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = NonResizableArrayQueue::new(M);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }
    }

    const SEGMENT_16_SIZE: usize = 16;
    const SEGMENT_32_SIZE: usize = 32;
    const SEGMENT_64_SIZE: usize = 64;
    const SEGMENT_128_SIZE: usize = 128;
    const SEGMENT_256_SIZE: usize = 256;
    const SEGMENT_512_SIZE: usize = 512;

    mod rc_ref_cell_linked_array_queue {
        use super::test::Bencher;
        use super::super::RcRefCellLinkLinkedArrayQueue;
        use super::*;

        #[bench]
        fn size_0001k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = RcRefCellLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }
    }

    mod shared_linked_array_queue {
        use super::test::Bencher;
        use super::super::SharedLinkLinkedArrayQueue;
        use super::*;

        #[bench]
        fn size_0001k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0016(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_16_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0032(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_32_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0064(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_64_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0128(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_128_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0256(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_256_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0001k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0002k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _2_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0004k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _4_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0008k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _8_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0016k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _16_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0032k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _32_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0064k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _64_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0128k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _128_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0256k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _256_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_0512k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, _512_K);
                deque_many(&mut queue)
            })
        }

        #[bench]
        fn size_1024k_segment_size_0512(b: &mut Bencher) {
            b.iter(|| {
                let mut queue = SharedLinkLinkedArrayQueue::new(SEGMENT_512_SIZE);
                enqueue_many(&mut queue, M);
                deque_many(&mut queue)
            })
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
