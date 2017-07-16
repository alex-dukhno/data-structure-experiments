package ua.ds;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class LinkedArrayQueueTest {

  private LinkedArrayQueue queue;

  @Before
  public void setUp() throws Exception {
    queue = new LinkedArrayQueue();
  }
  
  @Test
  public void dequeFromEmptyQueue() throws Exception {
    assertThat(queue.deque(), is(-1));
  }

  @Test
  public void enqueueDequeItem() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));
    assertThat(queue.deque(), is(-1));
  }

  @Test
  public void enqueueDequeItemManyTimes() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));

    queue.enqueue(20);

    assertThat(queue.deque(), is(20));

    queue.enqueue(30);

    assertThat(queue.deque(), is(30));
  }

  @Test
  public void enqueueManyItemsDequeAll() throws Exception {
      queue.enqueue(10);
      queue.enqueue(20);
      queue.enqueue(30);

      assertThat(queue.deque(), is(10));
      assertThat(queue.deque(), is(20));
      assertThat(queue.deque(), is(30));
      assertThat(queue.deque(), is(-1));
  }

  @Test
  public void enqueueDequeItemsMoreThanSegmentCapacity() throws Exception {
    for (int i = 0; i < 20; i++) {
      queue.enqueue(i);
    }

    for (int i = 0; i < 20; i++) {
      assertThat(queue.deque(), is(i));
    }
    assertThat(queue.deque(), is(-1));
  }

  @Test
  public void enqueueMoreThanOneSegment_thenDeque() throws Exception {
    for (int i = 0; i < 40; i++) {
      queue.enqueue(i);
    }

    for (int i = 0; i < 40; i++) {
      assertThat(queue.deque(), is(i));
    }
    assertThat(queue.deque(), is(-1));
  }

  @Test
  public void enqueueDequeMoreThanSegmentCapacity() throws Exception {
    for(int i = 0; i < 40; i++) {
      queue.enqueue(i);
      assertThat(queue.deque(), is(i));
      assertThat(queue.deque(), is(-1));
    }
  }
}
