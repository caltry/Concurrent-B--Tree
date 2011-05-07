/*
 * Sequential B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import java.util.Map;
import java.lang.reflect.Array;

public class BTreeSeq<K,V>
{
	private int numKeysPerNode = 7;
	private int size = 0;

	class Node<K,V>
	{
		private int numKeys;
		private K[] keys;
		private Node[] children;
		private Node<K,V> parent = null;

		@SuppressWarnings({"unchecked"})
		public Node( K key,
		             V value,
		             Node<K,V> parent )
		{
			// Like: keys = new K[numKeysPerNode], but working around Java's
			// type-erasure approach to generics.
			// This cast will always work because Array dynamically creates the
			// generic array of the correct type. Still, we have to do an
			// "unchecked" cast, and we don't want to be warned about it,
			// because we've already guaranteed the type safety.
			keys = (K[]) Array.newInstance( key.getClass(), numKeysPerNode );

			children = new Node[numKeysPerNode-1];

			this.parent = parent;
		}
	}

	private Node<K,V> root = null;
	
	/** {@inheritDoc} */
	public void clear()
	{
		// We'll let the garbage collector worry about it.
		root = null;
	}

	/** {@inheritDoc} */
	public boolean containsKey( K key )
	{
		assert(false);
		return false;
	}
	
	/** {@inheritDoc} */
	public boolean containsValue( V value )
	{
		assert(false);
		return false;
	}

	public V get( K key )
	{
		assert(false);
		return null;
	}

	public boolean isEmpty()
	{
		return size == 0;
	}

	public V put( K key, V value )
	{
		assert(false);
		return null;
	}

	public V remove( K key )
	{
		assert(false);
		return null;
	}

	public int size()
	{
		return this.size;
	}
}
