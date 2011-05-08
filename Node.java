/*
 * Sequential B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import java.lang.reflect.Array;

/** 
 * A B-Tree node.
 */
public abstract class Node<K extends Comparable,V>
{
	protected static final int numKeysPerNode = 7;

	protected int numKeys;
	protected K[] keys;
	protected Node<K,V> parent = null;
    protected Node<K,V> next = null;

	@SuppressWarnings({"unchecked"})
	public Node( K key,
				 Node<K,V> parent )
	{
		// Like: keys = new K[numKeysPerNode], but working around Java's
		// type-erasure approach to generics.
		// This cast will always work because Array dynamically creates the
		// generic array of the correct type. Still, we have to do an
		// "unchecked" cast, and we don't want to be warned about it,
		// because we've already guaranteed the type safety.
		keys = (K[])(Array.newInstance( key.getClass(), numKeysPerNode ));
        keys[0] = key;
        numKeys = 1;
		this.parent = parent;
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
	 */
	public abstract Union<Node<K,V>,V> getChild( K key );

    /**
     * Splits a node into two nodes, returning the second node.
     */
    public abstract Union<Node<K,V>,V> split();
}
