package ua.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NonResizableBitAndArrayQueuePrimitiveTest {

  private NonResizableBitAndArrayQueuePrimitive queue;

  @BeforeEach
  void setUp() throws Exception {
    queue = new NonResizableBitAndArrayQueuePrimitive();
  }

  @Test
  void dequeFromEmptyQueue() throws Exception {
    assertThat(queue.deque(), is(-1));
  }

  @Test
  void enqueueDequeItem() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));
  }

  @Test
  void enqueueDequeItems() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));

    queue.enqueue(20);

    assertThat(queue.deque(), is(20));

    queue.enqueue(30);

    assertThat(queue.deque(), is(30));
  }

  @Test
  void enqueueManyItemsDequeManyItems() throws Exception {
    queue.enqueue(10);
    queue.enqueue(20);
    queue.enqueue(30);

    assertThat(queue.deque(), is(10));
    assertThat(queue.deque(), is(20));
    assertThat(queue.deque(), is(30));
  }

  @Test
  void enqueueDequeMoreThanCapacity() throws Exception {
    for (int i = 0; i < 32; i++) {
      queue.enqueue(i);
      queue.deque();
    }
  }
}
