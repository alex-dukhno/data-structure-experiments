package ua.ds;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ArrayQueueTest {

  private ArrayQueue queue;

  @Before
  public void setUp() throws Exception {
    queue = new ArrayQueue();
  }

  @Test
  public void dequeFromEmptyQueue() throws Exception {
    assertThat(queue.deque(), is(-1));
  }

  @Test
  public void enqueueDequeItem() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));
  }

  @Test
  public void enqueueDequeItems() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));

    queue.enqueue(20);

    assertThat(queue.deque(), is(20));

    queue.enqueue(30);

    assertThat(queue.deque(), is(30));
  }

  @Test
  public void enqueueManyItemsDequeManyItems() throws Exception {
    queue.enqueue(10);
    queue.enqueue(20);
    queue.enqueue(30);

    assertThat(queue.deque(), is(10));
    assertThat(queue.deque(), is(20));
    assertThat(queue.deque(), is(30));
  }

  @Test
  public void enqueueDequeMoreThanCapacity() throws Exception {
    for (int i = 0; i < 32; i++) {
      queue.enqueue(i);
      queue.deque();
    }
  }
}
