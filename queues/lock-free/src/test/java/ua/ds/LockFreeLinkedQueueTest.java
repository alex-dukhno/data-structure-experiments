package ua.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class LockFreeLinkedQueueTest {
  private LockFreeLinkedQueue queue;

  @BeforeEach
  void setUp() throws Exception {
    queue = new LockFreeLinkedQueue();
  }

  @Test
  void dequeFromEmptyQueue() throws Exception {
    assertThat(queue.deque(), is(-1));
  }

  @Test
  void enqueueItem() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));
  }

  @Test
  void enqueueItemManyTimes() throws Exception {
    queue.enqueue(10);
    assertThat(queue.deque(), is(10));

    queue.enqueue(20);
    assertThat(queue.deque(), is(20));

    queue.enqueue(30);
    assertThat(queue.deque(), is(30));
  }

  @Test
  void enqueueManyItems() throws Exception {
    queue.enqueue(10);
    queue.enqueue(20);
    queue.enqueue(30);

    assertThat(queue.deque(), is(10));
    assertThat(queue.deque(), is(20));
    assertThat(queue.deque(), is(30));
  }
}
