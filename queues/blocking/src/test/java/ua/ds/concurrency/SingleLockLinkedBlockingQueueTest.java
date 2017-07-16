package ua.ds.concurrency;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class SingleLockLinkedBlockingQueueTest {

    private SingleLockLinkedBlockingQueue queue;

    @Before
    public void setUp() throws Exception {
        queue = new SingleLockLinkedBlockingQueue();
    }

    @Test
    public void addOneItem() throws Exception {
        queue.enqueue(10);

        assertThat(queue.deque(), is(10));
    }

    @Test
    public void addManyItems() throws Exception {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        assertThat(queue.deque(), is(10));
        assertThat(queue.deque(), is(20));
        assertThat(queue.deque(), is(30));
    }

    @Test
    public void addOneByOne() throws Exception {
        queue.enqueue(10);

        assertThat(queue.deque(), is(10));

        queue.enqueue(20);

        assertThat(queue.deque(), is(20));

        queue.enqueue(30);

        assertThat(queue.deque(), is(30));
    }
}
