extern crate alloc;

use self::alloc::raw_vec::RawVec;

use std::ptr;
use std::usize;

use super::Queue;

const MIN_CAPACITY: usize = 16;
const MAX_CAPACITY: usize = usize::MAX;

pub struct ResizableArrayQueue<T> {
    head: usize,
    tail: usize,
    size: usize,
    data: RawVec<T>
}

impl <T> ResizableArrayQueue<T> {
    pub fn new(capacity: usize) -> Self {
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

impl <T> Queue<T> for ResizableArrayQueue<T> {
    fn deque(&mut self) -> Option<T> {
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
            if self.size > MIN_CAPACITY && self.size == (capacity / 4) {
                self.resize(capacity / 2);
            }
            Some(item)
        }
    }

    fn enqueue(&mut self, item: T) {
        let capacity = self.data.cap();
        if self.size == capacity && capacity < MAX_CAPACITY {
            self.resize(capacity * 2);
        }
        unsafe {
            let to_write = self.data.ptr().offset(self.tail as isize);
            ptr::write(to_write, item);
        }
        self.size += 1;
        self.tail = (self.tail + 1) & (self.data.cap() - 1);
    }
}

pub struct NonResizableArrayQueue<T> {
    head: usize,
    tail: usize,
    size: usize,
    data: RawVec<T>
}

impl <T> NonResizableArrayQueue<T> {
    pub fn new(capacity: usize) -> Self {
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

impl <T> Queue<T> for NonResizableArrayQueue<T> {
    fn deque(&mut self) -> Option<T> {
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

    fn enqueue(&mut self, item: T) {
        unsafe {
            let to_write = self.data.ptr().offset(self.tail as isize);
            ptr::write(to_write, item);
        }
        self.size += 1;
        self.tail = (self.tail + 1) & (self.data.cap() - 1);
    }
}

#[cfg(test)]
mod tests {
    mod non_resizable_array_queue {
        use super::super::*;

        #[test]
        fn deque_from_empty_queue() {
            let mut queue: NonResizableArrayQueue<i32> = NonResizableArrayQueue::new(16);

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
            let mut queue: ResizableArrayQueue<i32> = ResizableArrayQueue::new(16);

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
}
