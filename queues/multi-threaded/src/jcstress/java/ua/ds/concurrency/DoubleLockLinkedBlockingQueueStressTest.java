package ua.ds.concurrency;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IIII_Result;
import org.openjdk.jcstress.infra.results.I_Result;

public class DoubleLockLinkedBlockingQueueStressTest {

  @JCStressTest
  @Outcome(id = "1", expect = Expect.ACCEPTABLE, desc = "subscriber gets published value")
  @Outcome(expect = Expect.FORBIDDEN)
  @State
  public static class SinglePublishing {
    private DoubleLockLinkedBlockingQueue queue = new DoubleLockLinkedBlockingQueue();

    @Actor
    public void publisher()  {
      try {
        queue.enqueue(1);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    @Actor
    public void subscriber(I_Result r) {
      try {
        r.r1 = queue.deque();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @JCStressTest
  @Outcome(id = {"1, 2, 3, 4"}, expect = Expect.ACCEPTABLE, desc = "subscriber gets all published values in order")
  @Outcome(expect = Expect.FORBIDDEN)
  @State
  public static class FourShotsPublishing {
    private DoubleLockLinkedBlockingQueue queue = new DoubleLockLinkedBlockingQueue();

    @Actor
    public void publisher() {
      try {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    @Actor
    public void subscriber(IIII_Result r) {
      try {
        r.r1 = queue.deque();
        r.r2 = queue.deque();
        r.r3 = queue.deque();
        r.r4 = queue.deque();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @JCStressTest
  @Outcome(id = {"1, 2, 3, 4"}, expect = Expect.ACCEPTABLE, desc = "pub[1] -> 1 | pub[1] -> 2 | pub[2] -> 3 | pub[2] -> 4")
  @Outcome(id = {"1, 3, 2, 4"}, expect = Expect.ACCEPTABLE, desc = "pub[1] -> 1 | pub[2] -> 3 | pub[1] -> 2 | pub[2] -> 4")
  @Outcome(id = {"1, 3, 4, 2"}, expect = Expect.ACCEPTABLE, desc = "pub[1] -> 1 | pub[2] -> 3 | pub[1] -> 4 | pub[2] -> 2")
  @Outcome(id = {"3, 1, 2, 4"}, expect = Expect.ACCEPTABLE, desc = "pub[2] -> 3 | pub[1] -> 1 | pub[1] -> 2 | pub[2] -> 4")
  @Outcome(id = {"3, 1, 4, 2"}, expect = Expect.ACCEPTABLE, desc = "pub[2] -> 3 | pub[1] -> 1 | pub[1] -> 4 | pub[2] -> 2")
  @Outcome(id = {"3, 4, 1, 2"}, expect = Expect.ACCEPTABLE, desc = "pub[2] -> 3 | pub[2] -> 4 | pub[1] -> 1 | pub[2] -> 2")
  @Outcome(expect = Expect.FORBIDDEN)
  @State
  public static class TwoByTwoShotsPublishing {
    private DoubleLockLinkedBlockingQueue queue = new DoubleLockLinkedBlockingQueue();

    @Actor
    public void publisher1() {
      try {
        queue.enqueue(1);
        queue.enqueue(2);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    @Actor
    public void publisher2() {
      try {
        queue.enqueue(3);
        queue.enqueue(4);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    @Actor
    public void subscriber(IIII_Result r) {
      try {
        r.r1 = queue.deque();
        r.r2 = queue.deque();
        r.r3 = queue.deque();
        r.r4 = queue.deque();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
