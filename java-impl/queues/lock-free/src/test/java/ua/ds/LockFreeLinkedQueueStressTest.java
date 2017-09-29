package ua.ds;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
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

@Disabled
class LockFreeLinkedQueueStressTest {

  private static final int REPETITIONS = 100;
  private static final int ITEMS_PER_WRITER = 32768;

  private AtomicInteger readersCounter = new AtomicInteger();
  private AtomicInteger writersCounter = new AtomicInteger();
  private ExecutorService readersExecutor;
  private ExecutorService writersExecutor;

  @BeforeEach
  void setUp() throws Exception {
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
      .<Supplier<Arguments>>map(i -> () -> Arguments.of(i, i))
      .flatMap(s -> Stream.generate(s).limit(REPETITIONS));
  }

  @ParameterizedTest
  @MethodSource("data")
  void publish_subscribe(int readers, int writers) throws Exception {
    LockFreeLinkedQueue queue = new LockFreeLinkedQueue();

    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch stop = new CountDownLatch(readers + writers);
    AtomicInteger last = new AtomicInteger(writers * ITEMS_PER_WRITER);

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
            while (last.decrementAndGet() > -1) {
              int item;
              while ((item = queue.deque()) == -1) { }
              items.add(item);
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
      .flatMap(this::resultOrEmpty)
      .sorted()
      .collect(toList());

    assertThat(
      reduced,
      containsInAnyOrder(streamOfData(0, writers * ITEMS_PER_WRITER).boxed().collect(toList()).toArray())
    );
  }

  private Stream<Integer> resultOrEmpty(Future<List<Integer>> future) {
    try {
      return future.get().stream();
    } catch (InterruptedException | ExecutionException e) {
      return Stream.empty();
    }
  }

  private IntStream streamOfData(int weight, int limit) {
    return IntStream.iterate(weight * limit, i -> i + 1).limit(limit);
  }
}
