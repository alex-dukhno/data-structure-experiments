package ua.ds.concurrency;

class Node {
  //final really slows down padded queues
  int item;
  Node next;

  Node(int item) {
    this.item = item;
  }
}
