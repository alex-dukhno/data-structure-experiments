package ua.ds.concurrency;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IIII_Result;
import org.openjdk.jcstress.infra.results.I_Result;

public class SingleLockLinkedBlockingQueueStressTest {
/*
    @JCStressTest
    @Outcome(id = "1", expect = Expect.ACCEPTABLE, desc = "subscriber gets published value")
    @Outcome(expect = Expect.FORBIDDEN)
    @State
    public static class SinglePublishing {
        private SingleLockLinkedBlockingQueue queue = new SingleLockLinkedBlockingQueue();

        @Actor
        public void publisher() {
            queue.enqueue(1);
        }

        @Actor
        public void subscriber(I_Result r) {
            r.r1 = queue.deque();
        }
    }

    @JCStressTest
    @Outcome(id = {"1, 2, 3, 4"}, expect = Expect.ACCEPTABLE, desc = "subscriber gets all published values in order")
    @Outcome(expect = Expect.FORBIDDEN)
    @State
    public static class FourShotsPublishing {
        private SingleLockLinkedBlockingQueue queue = new SingleLockLinkedBlockingQueue();

        @Actor
        public void publisher() {
            queue.enqueue(1);
            queue.enqueue(2);
            queue.enqueue(3);
            queue.enqueue(4);
        }

        @Actor
        public void subscriber(IIII_Result r) {
            r.r1 = queue.deque();
            r.r2 = queue.deque();
            r.r3 = queue.deque();
            r.r4 = queue.deque();
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
        private SingleLockLinkedBlockingQueue queue = new SingleLockLinkedBlockingQueue();

        @Actor
        public void publisher1() {
            queue.enqueue(1);
            queue.enqueue(2);
        }

        @Actor
        public void publisher2() {
            queue.enqueue(3);
            queue.enqueue(4);
        }

        @Actor
        public void subscriber(IIII_Result r) {
            r.r1 = queue.deque();
            r.r2 = queue.deque();
            r.r3 = queue.deque();
            r.r4 = queue.deque();
        }
    }*/
}