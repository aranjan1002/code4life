import java.util.HashMap;
import java.util.Vector;

/**
 * This is a data structure that gives O(1) inserts and O(log n) deletes.
 * @author jamoozy
 */
public class FibonacciHeap<T>
{
  /** Buckets of roots by rank. */
  private final Vector<Vector<Node>> buckets;

  /** Map of objects --&gt; nodes. */
  private final HashMap<T,Node> map;

  /** Minimum-valued node. */
  private Node min;

  /**
   * Creates a new fibonacci heap.
   */
  public FibonacciHeap()
  {
    buckets = new Vector<Vector<Node>>();
    map = new HashMap<T,Node>();
  }

  /**
   * Inserts a new object into this heap.
   * @param key
   *          The value of the object.
   * @param obj
   *          The object to insert.
   */
  public void insert(int key,T obj)
  {
    Node node = new Node(key, obj);
    addTo(0, node);
    map.put(obj, node);
  }

  /**
   * Deletes and returns the object with the lowest key.
   * @return The object with the lowest key.
   */
  public T deleteMin()
  {
    // Do the rank-based comparison.
    for (int b = 0; b < buckets.size(); b++)
    {
      Vector<Node> bucket = buckets.get(b);
      while (bucket.size() > 1)
      {
        Node node1 = bucket.remove(0);
        Node node2 = bucket.remove(0);
        int comparison = node1.compareTo(node2);
        if (comparison < 0)
          node1.merge(node2);
        else
          node2.merge(node1);
      }
    }

    // Find the lowest root.
    min = null;
    for (Vector<Node> bucket : buckets)
      for (Node root : bucket)
        if (min == null || min.compareTo(root) > 0)
          min = root;
    remove(min);
    return min.data;
  }

  /**
   * Update the key of the object.
   * @param key
   *          The new key.
   * @param obj
   *          The associated object.
   */
  public void update(int key, T obj)
  {
    Node node = map.get(obj);
    if (key < node.key)
    {
      node.key = key;
      if (node.parent != null)
        node.cut();
    }
  }

  /**
   * Remove the passed node from the graph entirely.
   * @param node
   *          The node to remove.
   */
  private void remove(Node node)
  {
    buckets.get(node.rank).remove(node);
    while (node.first_kid != null)
      node.first_kid.cut();
  }

  /**
   * Removes the node associated with the passed object.
   * @param obj
   *          The object of the node to remove.
   */
  public void remove(T obj)
  {
    Node node = map.get(obj);
    remove(node);
  }

  /**
   * Add the given node to the bucket with the given index.
   * @param b
   *          Bucket index.
   * @param root
   *          The node to add.
   */
  private void addTo(int b, Node root)
  {
    // Ensure this is big enough.
    for (int i = buckets.size(); i <= b; i++)
      buckets.add(new Vector<Node>(100));

    // Add it to the proper bucket.
    buckets.get(b).add(root);
  }

  /**
   * A node in this fibonacci heap.
   */
  private class Node
  {
    /** Parent node. */
    Node parent = null;

    /** "left-most" child node. */
    Node first_kid = null;

    /** Previous node with same parent. */
    Node left = null;

    /** Next node with same parent. */
    Node right = null;

    /** The key (probably a number). */
    int key = Integer.MAX_VALUE;

    /** The data (anything). */
    T data = null;

    /** The number of children this has. */
    int rank = 0;

    /** Sad when child is lost. */
    boolean sad = false;

    /**
     * Creates a new node.
     * @param data
     *          The data this node holds.
     */
    Node(int key, T data)
    {
      this.key = key;
      this.data = data;
    }

    /**
     * Determines if this is marked (lost a child).
     * @return Whether this has lost a child.
     */
    boolean isMarked()
    {
      if (parent == null)
        sad = false;
      return sad;
    }

    /**
     * Decreases the rank of this an its parents by delta.
     * @param delta
     *          The amount to decrease by.
     */
    void decreaseRank(int delta)
    {
      parent.decreaseRank(delta);
      rank -= delta;
    }

    /**
     * Compares this node to that node.
     * @param that
     *          The other node.
     * @returns A negative number, 0, or a positive number if this is less
     * than, equal to, or greater than that.
     *
     * @see {@link java.util.Comparable#compareTo(T)}
     */
    int compareTo(Node that)
    {
      return this.key - that.key;
    }

    /**
     * Merges that node to be a child of this node.
     * @param that
     *          The newest child node.
     */
    void merge(Node that)
    {
      if (this.first_kid != null)
        this.first_kid.left = that;
      that.right = this.first_kid;
      this.first_kid = that;
      that.parent = this;
      this.rank++;
      FibonacciHeap.this.addTo(rank, this);
    }

    /**
     * Allow this node to mourn the loss of a child ::sniff:: ...
     */
    void mourn()
    {
      if (--rank < 0)
        throw new RuntimeException("0 > rank = " + rank);
      else if (parent == null)
        sad = false;
      else if (sad)
        cut();
      else
        sad = true;
    }

    /**
     * Cut this from its current tree, make it its own node.
     */
    void cut()
    {
      if (parent == null)
        throw new RuntimeException("Root being cut.");

      sad = false;
      parent.mourn();
      if (parent.first_kid == this)
        parent.first_kid = right;
      else
        if (left != null)
          left.right = right;
      if (right != null)
        right.left = left;
      parent = null;

      FibonacciHeap.this.addTo(rank, this);
    }
  }

  /** For testing. */
  public static void main(String[] args)
  {
    FibonacciHeap<String> fh = new FibonacciHeap<String>();
    String[] str = new String[] { "John", "Mary", "Sue", "Ashley", "Andrew" };
    
    System.out.println("testing now");

    fh.insert(20, str[0]);
    fh.insert(60, str[1]);
    fh.insert(60, str[2]);
    fh.insert(40, str[3]);
    fh.insert(10, str[4]);

    String out = fh.deleteMin();
    if (!out.equals(str[4]))
      System.err.printf("Test 1 failed: %s for %s\n", out, str[4]);

    fh.update(20, str[0]);
    fh.update(20, str[1]);
    fh.update(50, str[2]);
    fh.update(10, str[3]);

    out = fh.deleteMin();
    if (!out.equals(str[3]))
      System.err.printf("Test 2 failed: %s for %s\n", out, str[3]);

    fh.update(10, str[0]);
    fh.update(20, str[1]);
    fh.update(30, str[2]);

    out = fh.deleteMin();
    if (!out.equals(str[0]))
      System.err.printf("Test 3 failed: %s for %s\n", out, str[0]);

    System.out.println("k done");
  }
}
