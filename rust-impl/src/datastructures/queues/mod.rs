pub mod sequential;

pub trait Queue<E> {
    fn enqueue(&mut self, item: E);

    fn deque(&mut self) -> Option<E>;
}
