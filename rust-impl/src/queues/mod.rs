pub mod sequential;

pub trait Queue {
    fn enqueue(&mut self, item: i32);

    fn deque(&mut self) -> Option<i32>;
}
