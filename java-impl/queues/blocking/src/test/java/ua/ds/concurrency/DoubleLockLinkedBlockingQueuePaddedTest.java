package ua.ds.concurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class DoubleLockLinkedBlockingQueuePaddedTest {

  private DoubleLockLinkedBlockingQueue queue;

  @BeforeEach
  void setUp() throws Exception {
    queue = new DoubleLockLinkedBlockingQueue();
  }

  @Test
  void enqueueDequeItem() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));
  }

  @Test
  void enqueueDequeItemsManyTimes() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));

    queue.enqueue(20);

    assertThat(queue.deque(), is(20));

    queue.enqueue(30);

    assertThat(queue.deque(), is(30));
  }

  @Test
  void enqueueDequeManyItems() throws Exception {
    queue.enqueue(10);
    queue.enqueue(20);
    queue.enqueue(30);

    assertThat(queue.deque(), is(10));
    assertThat(queue.deque(), is(20));
    assertThat(queue.deque(), is(30));
  }
}
