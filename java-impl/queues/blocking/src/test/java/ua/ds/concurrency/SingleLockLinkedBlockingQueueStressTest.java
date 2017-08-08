package ua.ds.concurrency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class SingleLockLinkedBlockingQueueStressTest {

  private static final int REPETITIONS = 100;
  private static final int ITEMS_PER_WRITER = 32768;
  private SingleLockLinkedBlockingQueue queue;

  private AtomicInteger readersCounter = new AtomicInteger();
  private AtomicInteger writersCounter = new AtomicInteger();
  private ExecutorService readersExecutor;
  private ExecutorService writersExecutor;

  @BeforeEach
  void setUp() throws Exception {
    queue = new SingleLockLinkedBlockingQueue();
    readersExecutor = Executors.newFixedThreadPool(
      20,
      runnable ->
        new Thread(
          runnable,
          String.format(
            "Reader-%d",
            readersCounter.incrementAndGet()
          )
        )
    );
    writersExecutor = Executors.newFixedThreadPool(
      20,
      runnable ->
        new Thread(
          runnable,
          String.format(
            "Writer-%d",
            writersCounter.incrementAndGet()
          )
        )
    );
  }

  @AfterEach
  void tearDown() throws Exception {
    readersExecutor.shutdown();
    writersExecutor.shutdown();
  }

  static Stream<Arguments> data() {
    return Stream.iterate(1, i -> i * 2)
      .limit(4)
      .<Supplier<Arguments>>map(i -> () -> Arguments.of(i, i, i * ITEMS_PER_WRITER))
      .flatMap(s -> Stream.generate(s).limit(REPETITIONS));
  }

  @ParameterizedTest
  @MethodSource("data")
  void publish_subscribe(int readers, int writers, int limit) throws Exception {

    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch stop = new CountDownLatch(readers + writers);
    AtomicInteger last = new AtomicInteger(limit);

    for (int w = 0; w < writers; w++) {
      int writer = w;
      writersExecutor.submit(
        () -> {
          start.await();

          streamOfData(writer, ITEMS_PER_WRITER).forEach(queue::enqueue);

          stop.countDown();
          return null;
        }
      );
    }

    List<Future<List<Integer>>> result = new ArrayList<>();

    for (int r = 0; r < readers; r++) {
      result.add(
        readersExecutor.submit(
          () -> {
            List<Integer> items = new ArrayList<>();
            start.await();
            int item = last.decrementAndGet();
            while (item > -1) {
              items.add(queue.deque());
              item = last.decrementAndGet();
            }
            stop.countDown();
            return items;
          }
        )
      );
    }

    start.countDown();

    stop.await();

    List<Integer> reduced = result
      .stream()
      .map(this::resultOrEmpty)
      .reduce(
        new ArrayList<>(),
        (acc, item) -> {
          acc.addAll(item);
          return acc;
        }
      );

    assertThat(
      reduced,
      containsInAnyOrder(streamOfData(0, limit).boxed().collect(toList()).toArray())
    );
  }

  private List<Integer> resultOrEmpty(Future<List<Integer>> future) {
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      return Collections.emptyList();
    }
  }

  private IntStream streamOfData(int weight, int limit) {
    return IntStream.iterate(weight * limit, i -> i + 1).limit(limit);
  }
}
