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

public class BTreeSeq<K,V> implements BTree<K,V>
{
	private int numKeysPerNode = 7;
	private int size = 0;

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
	
	/** {@inheritDoc} */
	public V get( K key )
	{
		Node<K,V> currentNode = root;
		if( currentNode == null )
		{
			return null;
		}
		else
		{
			// FIXME
		}
		return null;
	}

	/** {@inheritDoc} */
	public boolean isEmpty()
	{
		return size == 0;
	}

	/** {@inheritDoc} */
	public V put( K key, V value )
	{
		assert(false);
		return null;
	}

	/** {@inheritDoc} */
	public V remove( K key )
	{
		assert(false);
		return null;
	}

	/** {@inheritDoc} */
	public int size()
	{
		return this.size;
	}
}
