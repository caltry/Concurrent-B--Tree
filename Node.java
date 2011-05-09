/*
 * Sequential B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 * Date: April. 12, 2011
 */

import java.lang.reflect.Array;
import java.util.Arrays;

/** 
 * A B-Tree node.
 */
public abstract class Node<K extends Comparable,V>
{
	protected static final int numKeysPerNode = 2;

    protected int numKeys;
    protected K[] keys;
    protected Node<K,V> parent = null;
    protected Node<K,V> next = null;

    /**
     * Creates a node with an intial value and the given parent.
     *
     * @param key The initial key in this node.
     */
	@SuppressWarnings({"unchecked"})
    public Node( K key )
    {
        // Like: keys = new K[numKeysPerNode], but working around Java's
        // type-erasure approach to generics.
        // This cast will always work because Array dynamically creates the
        // generic array of the correct type. Still, we have to do an
        // "unchecked" cast, and we don't want to be warned about it,
        // because we've already guaranteed the type safety.
        keys = (K[])(Array.newInstance( key.getClass(), numKeysPerNode +  + 1 ));
        keys[0] = key;
        numKeys = 1;
    }

    /**
     * Creates a node with the given keys and parent.
     *
     * @param keys The keys in this node.
     * @param parent The parent node.
     */
    protected Node( K[] keys, Node<K,V> parent ) {
        this.keys = Arrays.copyOf( keys, numKeysPerNode );
        numKeys = keys.length;
        this.parent = parent;
    }

    /**
     * Obtains the number of keys in this Node.
     *
     * @return The number of keys in this node.
     */
    public int numKeys() {
        return numKeys;
    }

    /**
     * Obtains the next node on the same level as this node.
     *
     * @return The next node on the same level.
     */
    public Node<K,V> getNext() {
        return next;
    }

    /**
     * Find the lowest number in the range of Keys in this Node.
     *
     * @return The lowerbound of the values in this node.
     */
    public K lowerBound()
    {
        return keys[0];
    }

    /**
     * Find the highest number in the range of Keys in this Node.
     *
     * @return The upperbound of the values in this node.
     */
    public K upperUpper()
    {
        return keys[numKeys-1];
    }

    /**
     * Returns a child node such that K is within its bounds.
     *
     * @param key The key of the desired child.
     * @return The child to search for the key or the value corresponding to 
     * the key depending on the type of node.
	 */
    public abstract Union<Node<K,V>,V> getChild( K key );

    /**
     * Splits a node into two nodes, returning the second node.
     */
    //public abstract Union<InternalNode<K,V>,LeafNode<K,V>> split( K key, V value );
}
