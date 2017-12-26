package ua.ds.linked.primitive;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class LinkedQueuePrimitivePooled extends AbstractLinkedQueuePrimitive {
  private final Pool nodePool;

  public LinkedQueuePrimitivePooled(Pool nodePool) {
    this.nodePool = nodePool;
  }

  @Override
  protected Node newNode() {
    return nodePool.getNextNode();
  }

  public interface Pool {
    Node getNextNode();
  }

  public static class SimplePool implements Pool {
    private final Node[] nodes;
    private int index;

    SimplePool(int size) {
      nodes = new Node[size];
    }

    @Override
    public Node getNextNode() {
      nodes[index++] = new Node();
      return nodes[index - 1];
    }
  }

  public static class PreInitializedPool implements Pool {
    private final Node[] nodes;
    private int index;

    PreInitializedPool(int size) {
      nodes = new Node[size];
      for (int i = 0; i < size; i++) {
        nodes[i] = new Node();
      }
    }

    @Override
    public Node getNextNode() {
      return nodes[index++];
    }
  }

  public static class UnsafePool implements Pool {
    private final Node[] nodes;
    private int index;

    private static final Unsafe UNSAFE;

    static {
      try {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        UNSAFE = (Unsafe) field.get(null);
      } catch (NoSuchFieldException | IllegalAccessException ex) {
        throw new InternalError("Unsafe.theUnsafe field not available", ex);
      }
    }

    UnsafePool(int size) {
      nodes = new Node[size];
    }

    @Override
    public Node getNextNode() {
      try {
        nodes[index++] = (Node) UNSAFE.allocateInstance(Node.class);
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      }
      return nodes[index - 1];
    }
  }
}
