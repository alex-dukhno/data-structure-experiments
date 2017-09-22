package ua.ds;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedArrayQueueBoxedTest {

  private LinkedArrayQueueBoxed queue;

  @BeforeEach
  void setUp() throws Exception {
    queue = new LinkedArrayQueueBoxed();
  }

  @Test
  void dequeFromEmptyQueue() throws Exception {
    assertThat(queue.deque(), is(nullValue()));
  }

  @Test
  void enqueueDequeItem() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));
    assertThat(queue.deque(), is(nullValue()));
  }

  @Test
  void enqueueDequeItemManyTimes() throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));

    queue.enqueue(20);

    assertThat(queue.deque(), is(20));

    queue.enqueue(30);

    assertThat(queue.deque(), is(30));
  }

  @Test
  void enqueueManyItemsDequeAll() throws Exception {
      queue.enqueue(10);
      queue.enqueue(20);
      queue.enqueue(30);

      assertThat(queue.deque(), is(10));
      assertThat(queue.deque(), is(20));
      assertThat(queue.deque(), is(30));
      assertThat(queue.deque(), is(nullValue()));
  }

  @Test
  void enqueueDequeItemsMoreThanSegmentCapacity() throws Exception {
    for (int i = 0; i < 20; i++) {
      queue.enqueue(i);
    }

    for (int i = 0; i < 20; i++) {
      assertThat(queue.deque(), is(i));
    }
    assertThat(queue.deque(), is(nullValue()));
  }

  @Test
  void enqueueMoreThanOneSegment_thenDeque() throws Exception {
    for (int i = 0; i < 40; i++) {
      queue.enqueue(i);
    }

    for (int i = 0; i < 40; i++) {
      assertThat(queue.deque(), is(i));
    }
    assertThat(queue.deque(), is(nullValue()));
  }

  @Test
  void enqueueDequeMoreThanSegmentCapacity() throws Exception {
    for(int i = 0; i < 40; i++) {
      queue.enqueue(i);
      assertThat(queue.deque(), is(i));
      assertThat(queue.deque(), is(nullValue()));
    }
  }
}
