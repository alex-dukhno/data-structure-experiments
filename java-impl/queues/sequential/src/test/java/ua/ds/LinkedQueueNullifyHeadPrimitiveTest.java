package ua.ds;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedQueueNullifyHeadPrimitiveTest {

  private LinkedQueueNullifyHeadPrimitive queue;

  @BeforeEach
  void setUp() throws Exception {
    queue = new LinkedQueueNullifyHeadPrimitive();
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
