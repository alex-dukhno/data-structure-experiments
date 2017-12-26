package ua.ds.linked.primitive;

public class LinkedQueuePrimitive extends AbstractLinkedQueuePrimitive {
  @Override
  protected Node newNode() {
    return new Node();
  }
}
