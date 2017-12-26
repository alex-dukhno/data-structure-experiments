package ua.ds.linked.primitive;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import ua.ds.SequentialQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LinkedQueuePrimitivePooledTest {

  private static Stream<SequentialQueue> queueProvider() {
    return Stream.of(
      new LinkedQueuePrimitivePooled(
        new LinkedQueuePrimitivePooled.PreInitializedPool(16)
      ),
      new LinkedQueuePrimitivePooled(
        new LinkedQueuePrimitivePooled.SimplePool(16)
      ),
      new LinkedQueuePrimitivePooled(
        new LinkedQueuePrimitivePooled.UnsafePool(16)
      )
    );
  }

  @ParameterizedTest
  @MethodSource("queueProvider")
  void dequeFromEmptyQueue(SequentialQueue queue) throws Exception {
    assertThat(queue.deque(), is(-1));
  }

  @ParameterizedTest
  @MethodSource("queueProvider")
  void enqueueItem(SequentialQueue queue) throws Exception {
    queue.enqueue(10);

    assertThat(queue.deque(), is(10));
  }

  @ParameterizedTest
  @MethodSource("queueProvider")
  void enqueueItemManyTimes(SequentialQueue queue) throws Exception {
    queue.enqueue(10);
    assertThat(queue.deque(), is(10));

    queue.enqueue(20);
    assertThat(queue.deque(), is(20));

    queue.enqueue(30);
    assertThat(queue.deque(), is(30));
  }

  @ParameterizedTest
  @MethodSource("queueProvider")
  void enqueueManyItems(SequentialQueue queue) throws Exception {
    queue.enqueue(10);
    queue.enqueue(20);
    queue.enqueue(30);

    assertThat(queue.deque(), is(10));
    assertThat(queue.deque(), is(20));
    assertThat(queue.deque(), is(30));
  }
}
