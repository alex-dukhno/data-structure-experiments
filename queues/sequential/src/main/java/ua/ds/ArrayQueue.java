package ua.ds;

import org.openjdk.jol.datamodel.X86_32_DataModel;
import org.openjdk.jol.datamodel.X86_64_COOPS_DataModel;
import org.openjdk.jol.datamodel.X86_64_DataModel;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.layouters.HotSpotLayouter;

public class ArrayQueue {
  private final int[] items;
  private int head;
  private int tail;
  private final int mask;

  public ArrayQueue() {
    this(16);
  }

  public ArrayQueue(int capacity) {
    items = new int[capacity];
    head = 0;
    tail = capacity - 1;
    mask = capacity - 1;
  }

  public void enqueue(int item) {
    int index = head;
    head = (head + 1) & mask;
    items[index] = item;
  }

  public int deque() {
    if (isEmpty()) return -1;
    tail = (tail + 1) & mask;
    return items[tail];
  }

  private boolean isEmpty() {
    return (tail - head & mask) == mask;
  }

  public static void main(String[] args) {
    System.out.println("ArrayQueue Object Layout");
    HotSpotLayouter x32layouter = new HotSpotLayouter(new X86_32_DataModel());
    HotSpotLayouter x64_coops_layouter = new HotSpotLayouter(new X86_64_COOPS_DataModel());
    HotSpotLayouter x64layouter = new HotSpotLayouter(new X86_64_DataModel());
    System.out.println(ClassLayout.parseClass(ArrayQueue.class, x32layouter).toPrintable());
    System.out.println(ClassLayout.parseClass(ArrayQueue.class, x64_coops_layouter).toPrintable());
    System.out.println(ClassLayout.parseClass(ArrayQueue.class, x64layouter).toPrintable());
    System.out.println(ClassLayout.parseClass(int[].class, x32layouter).toPrintable());
    System.out.println(ClassLayout.parseClass(int[].class, x64_coops_layouter).toPrintable());
    System.out.println(ClassLayout.parseClass(int[].class, x64layouter).toPrintable());
    System.out.println(ClassLayout.parseInstance(new int[16], x32layouter).toPrintable());
    System.out.println(ClassLayout.parseInstance(new int[16], x64_coops_layouter).toPrintable());
    System.out.println(ClassLayout.parseInstance(new int[16], x64layouter).toPrintable());
  }
}
