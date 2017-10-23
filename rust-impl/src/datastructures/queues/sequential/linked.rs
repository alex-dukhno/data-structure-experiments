use std::fmt;

use std::rc::Rc;
use std::cell::RefCell;
use std::ptr::Shared;

use super::Queue;

type RcRefCellLink<T> = Option<Rc<RefCell<T>>>;

#[derive(Debug)]
struct RefCellNode<E> {
    item: E,
    next: RcRefCellLink<RefCellNode<E>>
}

impl <E> RefCellNode<E> {
    fn new(item: E) -> Rc<RefCell<RefCellNode<E>>> {
        Rc::new(
            RefCell::new(RefCellNode {
                item: item,
                next: None
            })
        )
    }
}

#[derive(Default, Debug)]
pub struct RcRefCellLinkedQueue<E> {
    head: RcRefCellLink<RefCellNode<E>>,
    tail: RcRefCellLink<RefCellNode<E>>
}

impl <E> RcRefCellLinkedQueue<E> {
    pub fn new() -> RcRefCellLinkedQueue<E> {
        RcRefCellLinkedQueue {
            head: None,
            tail: None
        }
    }
}

impl <E> Queue<E> for RcRefCellLinkedQueue<E> {
    fn deque(&mut self) -> Option<E> {
        self.head.take().map(|old_head| {
            match old_head.borrow_mut().next.take() {
                Some(new_head) => self.head = Some(new_head),
                None => { self.tail.take(); }
            }
            Rc::try_unwrap(old_head).ok().unwrap().into_inner().item
        })
    }

    fn enqueue(&mut self, item: E) {
        let node = RefCellNode::new(item);
        match self.tail.take() {
            Some(old_tail) => old_tail.borrow_mut().next = Some(node.clone()),
            None => self.head = Some(node.clone())
        }
        self.tail = Some(node)
    }
}

impl <E> Drop for RcRefCellLinkedQueue<E> {
    fn drop(&mut self) {
        while let Some(_) = self.deque() {}
    }
}

type SharedLink<T> = Option<Shared<T>>;

struct SharedNode<E> {
    item: E,
    next: SharedLink<SharedNode<E>>
}

impl <E> SharedNode<E> {
    fn new(item: E) -> SharedLink<SharedNode<E>> {
        Shared::new(
            Box::into_raw(Box::new(SharedNode {
                item: item,
                next: None
            }))
        )
    }
}

impl <E: fmt::Debug> fmt::Debug for SharedNode<E> {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{:?} => ", self.item)
    }
}

pub struct SharedLinkedQueue<E: Copy> {
    head: SharedLink<SharedNode<E>>,
    tail: SharedLink<SharedNode<E>>
}

impl <E: Copy> SharedLinkedQueue<E> {
    pub fn new() -> SharedLinkedQueue<E> {
        SharedLinkedQueue {
            head: None,
            tail: None
        }
    }
}

impl <E: fmt::Debug + Copy> fmt::Debug for SharedLinkedQueue<E> {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        let mut node = self.head;
        while let Some(item) = node {
            unsafe {
                write!(f, "{:?}", item.as_ref())?;
                node = item.as_ref().next
            }
        }
        Ok(())
    }
}

impl <E: Copy> Queue<E> for SharedLinkedQueue<E> {
    fn deque(&mut self) -> Option<E> {
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

    fn enqueue(&mut self, item: E) {
        let node = SharedNode::new(item);
        match self.tail.take() {
            Some(mut tail) => unsafe { tail.as_mut().next = node.clone(); },
            None => self.head = node.clone()
        }
        self.tail = node
    }
}

impl <E: Copy> Drop for SharedLinkedQueue<E> {
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
            let mut queue: RcRefCellLinkedQueue<i32> = RcRefCellLinkedQueue::new();

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
            let mut queue: SharedLinkedQueue<i32> = SharedLinkedQueue::new();

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
