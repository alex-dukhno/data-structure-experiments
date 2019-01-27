package ua.ds;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.ZZ_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

public class TotalBlockingListConcurrentAdd {

  @JCStressTest
  @Outcome(id = "true, true", expect = ACCEPTABLE, desc = "Item was added")
  @State
  public static class Contains {
    private TotalBlockingList list = new TotalBlockingList();

    @Actor
    public void actor1() {
      list.add(1);
    }

    @Actor
    public void actor2() {
      list.add(2);
    }

    @Arbiter
    public void reader(ZZ_Result result) {
      result.r1 = list.contains(1);
      result.r2 = list.contains(2);
    }
  }

  @JCStressTest
  @Outcome(id = "1, 2", expect = ACCEPTABLE, desc = "Items were added")
  @Outcome(id = "2, 1", expect = ACCEPTABLE, desc = "Items were added")
  @Outcome(id = "2, -1", expect = ACCEPTABLE_INTERESTING, desc = "Second item was added")
  @Outcome(id = "1, -1", expect = ACCEPTABLE_INTERESTING, desc = "First item was added")
  @Outcome(id = "-1, -1", expect = ACCEPTABLE_INTERESTING, desc = "No item was added")
  @State
  public static class Get {
    private TotalBlockingList list = new TotalBlockingList();

    @Actor
    public void actor1() {
      list.add(1);
    }

    @Actor
    public void actor2() {
      list.add(2);
    }

    @Actor
    public void reader(II_Result result) {
      result.r1 = list.get(0).orElse(-1);
      result.r2 = list.get(1).orElse(-1);
    }
  }
}
